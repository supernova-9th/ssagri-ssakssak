package com.supernova.ssagrissakssak.feed.service;

import com.supernova.ssagrissakssak.core.enums.StatisticsTimeType;
import com.supernova.ssagrissakssak.core.enums.StatisticsType;
import com.supernova.ssagrissakssak.feed.persistence.repository.BoardRepository;
import com.supernova.ssagrissakssak.feed.persistence.repository.model.StatisticsDto;
import com.supernova.ssagrissakssak.feed.service.delegator.validator.StatisticsValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardStatisticsService {

    private final BoardRepository boardRepository;
    private final StatisticsValidator statisticsValidator;

    @Transactional(readOnly = true)
    public List<StatisticsDto> getStatistics(String hashtag, StatisticsType type, StatisticsTimeType timeType, LocalDate start, LocalDate end) {
        statisticsValidator.validatePeriod(start, end);

        return boardRepository.getStatistics(hashtag, type, timeType, start, end);
    }
}
