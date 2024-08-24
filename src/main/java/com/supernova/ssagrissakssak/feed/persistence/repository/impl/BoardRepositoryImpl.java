package com.supernova.ssagrissakssak.feed.persistence.repository.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.core.types.dsl.StringTemplate;
import com.supernova.ssagrissakssak.core.enums.StatisticsType;
import com.supernova.ssagrissakssak.feed.persistence.repository.custom.BoardRepositoryCustom;
import com.supernova.ssagrissakssak.feed.persistence.repository.model.StatisticsDto;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import static com.supernova.ssagrissakssak.feed.persistence.repository.entity.QBoardEntity.boardEntity;
import static com.supernova.ssagrissakssak.feed.persistence.repository.entity.QHashtagEntity.hashtagEntity;

@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<StatisticsDto> getStatistics(String hashtag, StatisticsType type, LocalDateTime start, LocalDateTime end) {
        return jpaQueryFactory
                .select(Projections.constructor(StatisticsDto.class,
                        getDateTimeExpression(boardEntity.createdAt, type),
                        type.getValueExpression().as("value")))
                .from(boardEntity)
                .join(hashtagEntity).on(boardEntity.id.eq(hashtagEntity.boardId))
                .where(
                        hashtagEntity.hashtag.eq(hashtag),
                        boardEntity.createdAt.between(start, end)
                )
                .groupBy(getDateTimeExpression(boardEntity.createdAt, type))
                .orderBy(getDateTimeExpression(boardEntity.createdAt, type).asc())
                .fetch();
    }

    private StringTemplate getDateTimeExpression(DateTimePath<LocalDateTime> createdAt, StatisticsType type) {
        if (type == StatisticsType.DATE) {
            return Expressions.stringTemplate(
                    "DATE_FORMAT({0}, {1})",
                    createdAt,
                    "%Y-%m-%d"
            );
        } else if (type == StatisticsType.HOUR) {
            return Expressions.stringTemplate(
                    "DATE_FORMAT({0}, {1})",
                    createdAt,
                    "%Y-%m-%d %H:00:00"
            );
        } else {
            throw new IllegalArgumentException("Invalid time type");
        }
    }
}
