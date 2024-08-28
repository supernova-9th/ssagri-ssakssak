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

    /**
     * 주어진 필터링, 정렬, 검색 조건에 따라 게시물 목록을 동적으로 조회하는 메서드입니다.
     * QueryDSL을 사용하여 복잡한 쿼리를 구성하고 실행합니다.
     *
     * @param hashtag 게시물에 적용된 해시태그 (선택 사항)
     * @param type 게시물의 타입 (선택 사항)
     * @param orderBy 정렬 기준 필드 (예: "created_at", "view_count", "like_count")
     * @param searchBy 검색할 필드 (예: "title", "content")
     * @param search 검색어 (선택 사항)
     * @param pageCount 페이지당 표시할 게시물 수
     * @param page 조회할 페이지 번호
     * @return 필터링 및 검색 조건에 맞는 게시물 목록을 담은 List<BoardEntity> 객체
     */
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

    /**
     * 주어진 해시태그를 기반으로 게시물과 해시태그 엔티티 간의 조인 조건을 생성합니다.
     *
     * @param hashtag 필터링할 해시태그
     * @param board 게시물 엔티티를 나타내는 QBoardEntity 객체
     * @param hashtagEntity 해시태그 엔티티를 나타내는 QHashtagEntity 객체
     * @return 해시태그에 대한 조건을 나타내는 BooleanExpression 객체
     */
    private BooleanExpression hashtagCondition(String hashtag, QBoardEntity board, QHashtagEntity hashtagEntity) {
        return board.id.eq(hashtagEntity.boardId)
                .and(hashtagEntity.hashtag.eq(hashtag));
    }

    /**
     * 주어진 게시물 타입에 따라 필터링 조건을 생성합니다.
     *
     * @param type 필터링할 게시물 타입 (예: "FACEBOOK", "TWITTER", "INSTAGRAM", "THREADS")
     * @param board 게시물 엔티티를 나타내는 QBoardEntity 객체
     * @return 게시물 타입에 대한 조건을 나타내는 BooleanExpression 객체
     */
    private BooleanExpression typeCondition(String type, QBoardEntity board) {
        return board.type.eq(ContentType.valueOf(type));
    }

    /**
     * 주어진 검색 필드와 검색어를 기반으로 게시물에 대한 검색 조건을 생성합니다.
     *
     * @param searchBy 검색할 필드 (예: "title", "content", "title,content")
     * @param search 검색어
     * @param board 게시물 엔티티를 나타내는 QBoardEntity 객체
     * @return 검색 조건을 나타내는 BooleanExpression 객체. 검색 필드가 유효하지 않은 경우 null 반환.
     */
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

    /**
     * 주어진 정렬 기준에 따라 게시물 목록을 정렬하기 위한 OrderSpecifier 객체를 생성합니다.
     *
     * 지원되는 정렬 기준:
     * - "created_at": 게시물 생성일 기준 내림차순 정렬
     * - "updated_at": 게시물 수정일 기준 내림차순 정렬
     * - "view_count": 게시물 조회수 기준 내림차순 정렬
     * - "like_count": 게시물 좋아요 수 기준 내림차순 정렬
     *
     * 기본값은 "created_at" 기준으로 내림차순 정렬입니다.
     *
     * @param orderBy 정렬 기준 필드 (예: "created_at", "updated_at", "view_count", "like_count")
     * @param board 게시물 엔티티를 나타내는 QBoardEntity 객체
     * @return 지정된 정렬 기준에 따른 OrderSpecifier 객체. 유효하지 않은 기준인 경우 기본값으로 'created_at.desc()' 반환.
     */
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
