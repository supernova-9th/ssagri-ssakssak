package com.supernova.ssagrissakssak.feed.persistence.repository.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.supernova.ssagrissakssak.core.enums.ContentType;
import com.supernova.ssagrissakssak.core.enums.StatisticsTimeType;
import com.supernova.ssagrissakssak.core.enums.StatisticsType;
import com.supernova.ssagrissakssak.feed.controller.request.BoardSearchRequest;
import com.supernova.ssagrissakssak.feed.persistence.repository.custom.BoardRepositoryCustom;
import com.supernova.ssagrissakssak.feed.persistence.repository.entity.BoardEntity;
import com.supernova.ssagrissakssak.feed.persistence.repository.entity.QBoardEntity;
import com.supernova.ssagrissakssak.feed.persistence.repository.entity.QHashtagEntity;
import com.supernova.ssagrissakssak.feed.persistence.repository.model.StatisticsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.supernova.ssagrissakssak.feed.persistence.repository.entity.QBoardEntity.boardEntity;
import static com.supernova.ssagrissakssak.feed.persistence.repository.entity.QHashtagEntity.hashtagEntity;

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
     *
     * @param searchRequest 게시물 검색 조건 (해시태그, 타입, 정렬 기준, 검색 기준 및 검색어를 포함한 객체)
     * @param pageRequest 페이징 처리 정보를 포함한 PageRequest 객체 (페이지 번호와 페이지당 표시할 게시물 수)
     * @return 필터링 및 검색 조건에 맞는 게시물 목록을 페이징된 형태로 반환하는 Page<BoardEntity> 객체
     */
    @Override
    public Page<BoardEntity> getAllBoards(BoardSearchRequest searchRequest, PageRequest pageRequest) {

        QBoardEntity board = QBoardEntity.boardEntity;
        QHashtagEntity hashtagEntity = QHashtagEntity.hashtagEntity;

        // 조건을 동적으로 추가하기 위한 BooleanBuilder 객체 생성
        BooleanBuilder conditions = new BooleanBuilder();

        // 해시태그가 비어 있지 않으면 해시태그 조건을 추가
        if (!ObjectUtils.isEmpty(searchRequest.hashtag())) {
            conditions.and(hashtagCondition(searchRequest.hashtag(), board, hashtagEntity));
        }

        // 타입이 비어 있지 않으면 SNS 타입 조건을 추가
        if (!ObjectUtils.isEmpty(searchRequest.type())) {
            conditions.and(typeCondition(searchRequest.type(), board));
        }

        // 검색 기준과 검색어가 비어 있지 않으면 검색 조건을 추가
        if (!ObjectUtils.isEmpty(searchRequest.type()) && !ObjectUtils.isEmpty(searchRequest.searchBy())) {
            conditions.and(searchCondition(searchRequest.searchBy(), searchRequest.search(), board));
        }

        JPQLQuery<BoardEntity> query = jpaQueryFactory.selectFrom(board)
                .leftJoin(hashtagEntity).on(board.id.eq(hashtagEntity.boardId)) // leftJoin으로 연결하여 해시태그가 없는 게시물도 포함
                .where(conditions)
                .orderBy(getOrderSpecifier(searchRequest.orderBy(), board))
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize());

        List<BoardEntity> boards = query
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();

        // 전체 데이터 수를 가져옴
        long total = query.fetchCount();

        Page<BoardEntity> page = new PageImpl<>(boards, pageRequest, total);

        return page;
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
     * 정렬 기준:
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
