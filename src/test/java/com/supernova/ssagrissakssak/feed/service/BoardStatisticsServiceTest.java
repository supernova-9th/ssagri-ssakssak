package com.supernova.ssagrissakssak.feed.service;

import com.supernova.ssagrissakssak.core.enums.StatisticsTimeType;
import com.supernova.ssagrissakssak.core.enums.StatisticsType;
import com.supernova.ssagrissakssak.core.exception.ErrorCode;
import com.supernova.ssagrissakssak.core.exception.InvalidPeriodException;
import com.supernova.ssagrissakssak.feed.persistence.repository.BoardRepository;
import com.supernova.ssagrissakssak.feed.persistence.repository.HashtagRepository;
import com.supernova.ssagrissakssak.feed.persistence.repository.entity.BoardEntity;
import com.supernova.ssagrissakssak.feed.persistence.repository.model.StatisticsDto;
import com.supernova.ssagrissakssak.fixture.BoardFixture;
import com.supernova.ssagrissakssak.fixture.HashtagFixture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class BoardStatisticsServiceTest {

    @Autowired
    BoardStatisticsService boardStatisticsService;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private HashtagRepository hashtagRepository;

    @AfterEach
    void tearDown() {
        boardRepository.deleteAllInBatch();
        hashtagRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("viewCount를 조회한다.")
    void getStatistics() {
        // Given
        BoardEntity board1 = boardRepository.save(BoardFixture.get(10, 2, 3));
        BoardEntity board2 = boardRepository.save(BoardFixture.get(20, 2, 3));

        hashtagRepository.save(HashtagFixture.get(board1.getId()));
        hashtagRepository.save(HashtagFixture.get(board2.getId()));

        String hashtag = "test";
        StatisticsType type = StatisticsType.VIEW_COUNT;
        StatisticsTimeType timeType = StatisticsTimeType.HOUR;
        LocalDate start = LocalDate.now().minusDays(1);
        LocalDate end = LocalDate.now().plusDays(1);

        // When
        List<StatisticsDto> stats = boardStatisticsService.getStatistics(hashtag, type, timeType, start, end);

        // Then
        System.out.println(stats);
        assertThat(stats.size()).isEqualTo(1);
        StatisticsDto stat = stats.getFirst();
        assertThat(stat.getValue()).isEqualTo(board1.getViewCount() + board2.getViewCount());
    }

    @Test
    @DisplayName("시작 날짜가 종료 날짜보다 늦을 경우 예외가 발생한다")
    void getStatistics2() {
        // Given
        LocalDate start = LocalDate.of(2024, 8, 25);
        LocalDate end = LocalDate.of(2024, 8, 24);

        // When & Then
        assertThatThrownBy(() -> boardStatisticsService.getStatistics("hashtag", StatisticsType.COUNT, StatisticsTimeType.DATE, start, end))
                .isInstanceOf(InvalidPeriodException.class)
                .hasMessageContaining(ErrorCode.INVALID_PERIOD.getDefaultMessage());
    }

    @Test
    @DisplayName("조회 기간이 7일을 초과할 경우 예외가 발생한다")
    void getStatistics3() {
        // Given
        LocalDate start = LocalDate.of(2024, 8, 20);
        LocalDate end = LocalDate.of(2024, 8, 27);

        // When & Then
        assertThatThrownBy(() -> boardStatisticsService.getStatistics("hashtag", StatisticsType.COUNT, StatisticsTimeType.DATE, start, end))
                .isInstanceOf(InvalidPeriodException.class)
                .hasMessageContaining(ErrorCode.INVALID_PERIOD.getDefaultMessage());
    }

}