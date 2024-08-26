package com.supernova.ssagrissakssak.feed.persistence.repository.custom;

import com.supernova.ssagrissakssak.core.enums.StatValueType;
import com.supernova.ssagrissakssak.feed.controller.response.StatResponse;

import java.time.LocalDate;
import java.util.List;

public interface BoardRepositoryCustom {
    List<StatResponse> selectStatsByDate(String hashtag, LocalDate start, LocalDate end, StatValueType statValueType);
    List<StatResponse> selectStatsByHour(String hashtag, LocalDate start, LocalDate end, StatValueType statValueType);
}