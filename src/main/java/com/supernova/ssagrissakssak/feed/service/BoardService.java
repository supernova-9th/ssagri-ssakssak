package com.supernova.ssagrissakssak.feed.service;

import com.supernova.ssagrissakssak.core.enums.StatType;
import com.supernova.ssagrissakssak.core.enums.StatValueType;
import com.supernova.ssagrissakssak.feed.controller.response.StatResponse;
import com.supernova.ssagrissakssak.feed.persistence.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    public List<StatResponse> getStats(String hashtag, StatType type, LocalDate start, LocalDate end, StatValueType statValueType) {
        return StatType.DATE.equals(type)
                ? boardRepository.selectStatsByDate(hashtag, start, end, statValueType)
                : boardRepository.selectStatsByHour(hashtag, start, end, statValueType);
    }
}
