package com.supernova.ssagrissakssak.feed.persistence.repository.custom;

import com.supernova.ssagrissakssak.core.enums.StatisticsType;
import com.supernova.ssagrissakssak.feed.persistence.repository.model.StatisticsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface BoardRepositoryCustom {
    List<StatisticsDto> getStatistics(String hashtag, StatisticsType type, LocalDateTime start, LocalDateTime end);
}
