package com.supernova.ssagrissakssak.feed.persistence.repository.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@AllArgsConstructor
public class StatisticsDto {
    private LocalDateTime dateTime;
    private Long value;

    public StatisticsDto(String dateTimeStr, Long value) {
        this.dateTime = parseDateTime(dateTimeStr);
        this.value = value;
    }

    private LocalDateTime parseDateTime(String dateTimeStr) {
        if (dateTimeStr.length() > 10) {
            return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }

        return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}