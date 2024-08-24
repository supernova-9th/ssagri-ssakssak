package com.supernova.ssagrissakssak.feed.persistence.repository.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.supernova.ssagrissakssak.core.enums.StatisticsType;
import com.supernova.ssagrissakssak.feed.persistence.repository.custom.BoardRepositoryCustom;
import com.supernova.ssagrissakssak.feed.persistence.repository.model.StatisticsDto;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

import static com.supernova.ssagrissakssak.feed.persistence.repository.entity.QBoardEntity.boardEntity;
import static com.supernova.ssagrissakssak.feed.persistence.repository.entity.QHashtagEntity.hashtagEntity;

@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<StatisticsDto> getStatistics(String hashtag, StatisticsType type, LocalDate start, LocalDate end) {
        return jpaQueryFactory
                .select(Projections.constructor(StatisticsDto.class,
                        boardEntity.createdAt,
                        type.getValueExpression()
                ))
                .from(boardEntity)
                .join(hashtagEntity).on(boardEntity.id.eq(hashtagEntity.boardId))
                .where(
                        hashtagEntity.hashtag.eq(hashtag),
                        boardEntity.createdAt.between(start.atStartOfDay(), end.atStartOfDay())
                )
                .groupBy(boardEntity.createdAt)
                .orderBy(boardEntity.createdAt.asc())
                .fetch();
    }
}
