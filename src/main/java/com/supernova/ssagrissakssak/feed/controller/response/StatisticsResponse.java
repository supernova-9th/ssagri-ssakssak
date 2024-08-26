package com.supernova.ssagrissakssak.feed.controller.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.supernova.ssagrissakssak.core.enums.StatisticsTimeType;
import com.supernova.ssagrissakssak.feed.persistence.repository.model.StatisticsDto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record StatisticsResponse(
        @JsonInclude(JsonInclude.Include.NON_NULL)
        LocalDate date,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime time,
        Long value
) {
    public static StatisticsResponse of(StatisticsTimeType timeType, StatisticsDto statistics) {
        if(timeType == StatisticsTimeType.DATE) {
            return new StatisticsResponse(
                    statistics.getDateTime().toLocalDate(),
                    null,
                    statistics.getValue()
            );
        }

        return new StatisticsResponse(
                null,
                statistics.getDateTime(),
                statistics.getValue()
        );
    }
}
