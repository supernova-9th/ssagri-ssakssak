package com.supernova.ssagrissakssak.core.enums;

import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.supernova.ssagrissakssak.feed.persistence.repository.entity.QBoardEntity;

import java.util.function.Function;

import static com.supernova.ssagrissakssak.feed.persistence.repository.entity.QBoardEntity.boardEntity;

public enum StatisticsType {
    COUNT(SimpleExpression::count),
    VIEW_COUNT(boardEntity -> boardEntity.viewCount.sum()),
    LIKE_COUNT(boardEntity -> boardEntity.likeCount.sum()),
    SHARE_COUNT(boardEntity -> boardEntity.shareCount.sum()),
    DATE(null),
    HOUR(null);

    private final Function<QBoardEntity, NumberExpression<? extends Number>> valueExpression;

    StatisticsType(Function<QBoardEntity, NumberExpression<? extends Number>> valueExpression) {
        this.valueExpression = valueExpression;
    }

    public NumberExpression<? extends Number> getValueExpression() {
        if (valueExpression == null) {
            throw new UnsupportedOperationException("This type does not have a value expression");
        }
        return valueExpression.apply(boardEntity);
    }

    public boolean isTimeType() {
        return this == DATE || this == HOUR;
    }
}
