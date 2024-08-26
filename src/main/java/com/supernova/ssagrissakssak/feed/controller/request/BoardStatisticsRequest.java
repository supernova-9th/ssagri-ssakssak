package com.supernova.ssagrissakssak.feed.controller.request;

import com.supernova.ssagrissakssak.core.enums.StatisticsTimeType;
import com.supernova.ssagrissakssak.core.enums.StatisticsType;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record BoardStatisticsRequest(
        String hashtag,
        StatisticsType type,
        StatisticsTimeType timeType,
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate start,
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate end
) {
}
