package com.supernova.ssagrissakssak.feed.persistence.repository.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.DateTemplate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.supernova.ssagrissakssak.core.enums.StatValueType;
import com.supernova.ssagrissakssak.feed.controller.response.StatResponse;
import com.supernova.ssagrissakssak.feed.persistence.repository.custom.BoardRepositoryCustom;
import com.supernova.ssagrissakssak.feed.persistence.repository.entity.QBoardEntity;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

import static com.supernova.ssagrissakssak.feed.persistence.repository.entity.QBoardEntity.boardEntity;
import static com.supernova.ssagrissakssak.feed.persistence.repository.entity.QHashtagEntity.hashtagEntity;

@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<StatResponse> selectStatsByDate(String hashtag, LocalDate start, LocalDate end, StatValueType statValueType) {
        NumberExpression<Integer> count = getValue(boardEntity, statValueType);
        DateTemplate<Date> date = Expressions.dateTemplate(Date.class, "DATE({0})", boardEntity.createdAt);

        return queryFactory.select(Projections.constructor(StatResponse.class
                        , date.as("createdAt")
                        , count))
                .from(boardEntity)
                .join(hashtagEntity).on(boardEntity.id.eq(hashtagEntity.boardId))
                .where(hashtagEntity.hashtag.eq(hashtag)
                        .and(boardEntity.createdAt.between(start.atStartOfDay(), end.atTime(LocalTime.MAX))))
                .groupBy(date)
                .orderBy(date.asc())
                .fetch();
    }

    @Override
    public List<StatResponse> selectStatsByHour(String hashtag, LocalDate start, LocalDate end, StatValueType statValueType) {
        NumberExpression<Integer> count = getValue(boardEntity, statValueType);
        DateTemplate<String> hour = Expressions.dateTemplate(String.class, "DATE_FORMAT({0}, '%Y-%m-%d %H:00:00')", boardEntity.createdAt);

        return queryFactory.select(Projections.constructor(StatResponse.class
                        , hour.as("createdAt")
                        , count))
                .from(boardEntity)
                .join(hashtagEntity).on(boardEntity.id.eq(hashtagEntity.boardId))
                .where(hashtagEntity.hashtag.eq(hashtag)
                        .and(boardEntity.createdAt.between(start.atStartOfDay(), end.atTime(LocalTime.MAX))))
                .groupBy(hour)
                .orderBy(hour.asc())
                .fetch();
    }

    private NumberExpression<Integer> getValue(QBoardEntity board, StatValueType statValueType) {
        switch (statValueType) {
            case VIEW:
                return board.viewCount.sum();
            case LIKE:
                return board.likeCount.sum();
            case SHARE:
                return board.shareCount.sum();
            default:
                return board.id.count().intValue();
        }
    }

}