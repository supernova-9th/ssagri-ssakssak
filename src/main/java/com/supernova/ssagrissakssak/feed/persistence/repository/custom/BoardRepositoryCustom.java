package com.supernova.ssagrissakssak.feed.persistence.repository.custom;

import com.supernova.ssagrissakssak.core.enums.StatisticsTimeType;
import com.supernova.ssagrissakssak.core.enums.StatisticsType;
import com.supernova.ssagrissakssak.feed.persistence.repository.model.StatisticsDto;

import java.time.LocalDate;
import java.util.List;

public interface BoardRepositoryCustom {
    List<StatisticsDto> getStatistics(String hashtag, StatisticsType type, StatisticsTimeType timeType, LocalDate start, LocalDate end);
}
