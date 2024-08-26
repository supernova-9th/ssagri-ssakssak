package com.supernova.ssagrissakssak.feed.persistence.repository.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.supernova.ssagrissakssak.core.enums.ContentType;
import com.supernova.ssagrissakssak.core.enums.StatisticsTimeType;
import com.supernova.ssagrissakssak.core.enums.StatisticsType;
import com.supernova.ssagrissakssak.feed.persistence.repository.custom.BoardRepositoryCustom;
import com.supernova.ssagrissakssak.feed.persistence.repository.entity.BoardEntity;
import com.supernova.ssagrissakssak.feed.persistence.repository.entity.QBoardEntity;
import com.supernova.ssagrissakssak.feed.persistence.repository.entity.QHashtagEntity;
import com.supernova.ssagrissakssak.feed.persistence.repository.model.StatisticsDto;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.supernova.ssagrissakssak.feed.persistence.repository.entity.QBoardEntity.boardEntity;
import static com.supernova.ssagrissakssak.feed.persistence.repository.entity.QHashtagEntity.hashtagEntity;

import com.querydsl.core.types.dsl.StringTemplate;
import org.springframework.util.ObjectUtils;

@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<StatisticsDto> getStatistics(String hashtag, StatisticsType type, StatisticsTimeType timeType, LocalDate start, LocalDate end) {
        return jpaQueryFactory
                .select(Projections.constructor(StatisticsDto.class,
                        getDateTimeExpression(boardEntity.createdAt, timeType),
                        type.getValueExpression()
                ))
                .from(boardEntity)
                .join(hashtagEntity).on(boardEntity.id.eq(hashtagEntity.boardId))
                .where(
                        hashtagEntity.hashtag.eq(hashtag),
                        boardEntity.createdAt.between(start.atStartOfDay(), end.plusDays(1).atStartOfDay())
                )
                .groupBy(getDateTimeExpression(boardEntity.createdAt, timeType))
                .orderBy(getDateTimeExpression(boardEntity.createdAt, timeType).asc())
                .fetch();
    }

    private StringTemplate getDateTimeExpression(DateTimePath<LocalDateTime> createdAt, StatisticsTimeType timeType) {
        if (timeType == StatisticsTimeType.DATE) {
            return Expressions.stringTemplate(
                    "DATE_FORMAT({0}, {1})",
                    createdAt,
                    "%Y-%m-%d"
            );
        } else if (timeType == StatisticsTimeType.HOUR) {
            return Expressions.stringTemplate(
                    "DATE_FORMAT({0}, {1})",
                    createdAt,
                    "%Y-%m-%d %H:00:00"
            );
        } else {
            throw new IllegalArgumentException("Invalid time type");
        }
    }

    @Override
    public List<BoardEntity> getBoards(String hashtag, String type, String orderBy, String searchBy,
                                       String search, Integer pageCount, Integer page) {

        QBoardEntity board = QBoardEntity.boardEntity;
        QHashtagEntity hashtagEntity = QHashtagEntity.hashtagEntity;

        BooleanBuilder conditions = new BooleanBuilder();

        if (!ObjectUtils.isEmpty(hashtag)) {
            conditions.and(hashtagCondition(hashtag, board, hashtagEntity));
        }

        if (!ObjectUtils.isEmpty(type)) {
            conditions.and(typeCondition(type, board));
        }

        if (!ObjectUtils.isEmpty(search) && !ObjectUtils.isEmpty(searchBy)) {
            conditions.and(searchCondition(searchBy, search, board));
        }

        // 쿼리 실행
        return jpaQueryFactory
                .select(board)
                .from(board)
                .join(hashtagEntity).on(board.id.eq(hashtagEntity.boardId))
                .where(conditions)
                .orderBy(getOrderSpecifier(orderBy, board))
                .limit(pageCount)
                .offset((page > 0 ? (page - 1) : 0) * pageCount)
                .fetch();
    }

    private BooleanExpression hashtagCondition(String hashtag, QBoardEntity board, QHashtagEntity hashtagEntity) {
        return board.id.eq(hashtagEntity.boardId)
                .and(hashtagEntity.hashtag.eq(hashtag));
    }

    private BooleanExpression typeCondition(String type, QBoardEntity board) {
        return board.type.eq(ContentType.valueOf(type));
    }

    private BooleanExpression searchCondition(String searchBy, String search, QBoardEntity board) {
        if ("title".equalsIgnoreCase(searchBy)) {
            return board.title.like("%" + search + "%");
        } else if ("content".equalsIgnoreCase(searchBy)) {
            return board.content.like("%" + search + "%");
        } else if ("title,content".equalsIgnoreCase(searchBy)) {
            return board.title.like("%" + search + "%")
                    .and(board.content.like("%" + search + "%"));
        }
        return null;
    }

    private OrderSpecifier<?> getOrderSpecifier(String orderBy, QBoardEntity board) {
        if ("created_at".equalsIgnoreCase(orderBy)) {
            return board.createdAt.desc();
        } else if ("updated_at".equalsIgnoreCase(orderBy)) {
            return board.updatedAt.desc();
        } else if ("view_count".equalsIgnoreCase(orderBy)) {
            return board.viewCount.desc();
        } else if ("like_count".equalsIgnoreCase(orderBy)) {
            return board.likeCount.desc();
        }
        return board.createdAt.desc();
    }
}
