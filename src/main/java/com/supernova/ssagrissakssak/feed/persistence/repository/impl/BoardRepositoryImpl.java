package com.supernova.ssagrissakssak.feed.persistence.repository.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.supernova.ssagrissakssak.core.enums.ContentType;
import com.supernova.ssagrissakssak.feed.controller.response.BoardResponse;
import com.supernova.ssagrissakssak.feed.persistence.repository.custom.BoardRepositoryCustom;
import com.supernova.ssagrissakssak.feed.persistence.repository.entity.BoardEntity;
import com.supernova.ssagrissakssak.feed.persistence.repository.entity.QBoardEntity;
import com.supernova.ssagrissakssak.feed.persistence.repository.entity.QHashtagEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.util.ObjectUtils;

import java.util.List;

@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

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

}
