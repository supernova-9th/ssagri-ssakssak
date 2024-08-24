package com.supernova.ssagrissakssak.feed.persistence.repository.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class StatisticsDto {
    private LocalDateTime dateTime;
    private Long value;
}