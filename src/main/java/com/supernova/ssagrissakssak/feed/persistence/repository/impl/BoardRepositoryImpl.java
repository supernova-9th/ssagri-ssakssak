package com.supernova.ssagrissakssak.feed.persistence.repository.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.supernova.ssagrissakssak.core.enums.StatValueType;
import com.supernova.ssagrissakssak.feed.controller.response.StatResponse;
import com.supernova.ssagrissakssak.feed.persistence.repository.custom.BoardRepositoryCustom;
import com.supernova.ssagrissakssak.feed.persistence.repository.entity.QBoardEntity;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static com.supernova.ssagrissakssak.feed.persistence.repository.entity.QBoardEntity.boardEntity;
import static com.supernova.ssagrissakssak.feed.persistence.repository.entity.QHashtagEntity.hashtagEntity;

@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<StatResponse> selectStatsByDate(String hashtag, LocalDate start, LocalDate end, StatValueType statValueType) {
        NumberExpression<Integer> valueExpression = getValue(boardEntity, statValueType);

        return queryFactory.select(Projections.bean(StatResponse.class
                        , boardEntity.createdAt
                        , valueExpression))
                .from(boardEntity)
                .join(hashtagEntity).on(boardEntity.id.eq(hashtagEntity.boardId))
                .where(hashtagEntity.hashtag.eq(hashtag)
                        .and(boardEntity.createdAt.between(start.atStartOfDay(), end.atTime(LocalTime.MAX))))
                .groupBy(boardEntity.createdAt)
                .fetch();
    }

    @Override
    public List<StatResponse> selectStatsByHour(String hashtag, LocalDate start, LocalDate end, StatValueType statValueType) {
        NumberExpression<Integer> valueExpression = getValue(boardEntity, statValueType);

        return queryFactory.select(Projections.bean(StatResponse.class
                        , boardEntity.createdAt
                        , valueExpression))
                .from(boardEntity)
                .join(hashtagEntity).on(boardEntity.id.eq(hashtagEntity.boardId))
                .where(hashtagEntity.hashtag.eq(hashtag)
                        .and(boardEntity.createdAt.between(start.atStartOfDay(), end.atTime(LocalTime.MAX))))
                .groupBy(boardEntity.createdAt.hour())
                .orderBy(boardEntity.createdAt.asc())
                .fetch();
    }

    private NumberExpression<Integer> getValue(QBoardEntity board, StatValueType statValueType) {
        switch (statValueType) {
            case StatValueType.VIEW:
                return board.viewCount.sum();
            case StatValueType.LIKE:
                return board.likeCount.sum();
            case StatValueType.SHARE:
                return board.shareCount.sum();
            default:
                return board.id.count().intValue();
        }
    }

}