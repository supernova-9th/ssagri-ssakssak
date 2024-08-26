package com.supernova.ssagrissakssak.feed.service;

import com.supernova.ssagrissakssak.core.enums.StatType;
import com.supernova.ssagrissakssak.core.enums.StatValueType;
import com.supernova.ssagrissakssak.feed.controller.response.StatResponse;
import com.supernova.ssagrissakssak.feed.persistence.repository.BoardRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

    @Mock
    private BoardRepository boardRepository;

    @InjectMocks
    private BoardService boardService;

    @Test
    void StatType이_DATE일_때_selectStatsByDate가_호출되어야_한다() {
        // given
        String hashtag = "food";
        StatType type = StatType.DATE;
        LocalDate start = LocalDate.now().minusDays(7);
        LocalDate end = LocalDate.now();
        StatValueType statValueType = StatValueType.VIEW;

        when(boardRepository.selectStatsByDate(anyString(), any(LocalDate.class), any(LocalDate.class), any(StatValueType.class)))
                .thenReturn(Collections.singletonList(new StatResponse("2024-08-26", 10)));

        // when
        List<StatResponse> result = boardService.getStats(hashtag, type, start, end, statValueType);

        // then
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getDateTime()).isEqualTo("2024-08-26");
        assertThat(result.get(0).getCount()).isEqualTo(10);
    }

    @Test
    void StatType이_HOUR일_때_selectStatsByHour가_호출되어야_한다() {
        // given
        String hashtag = "#food";
        StatType type = StatType.HOUR;
        LocalDate start = LocalDate.now().minusDays(7);
        LocalDate end = LocalDate.now();
        StatValueType statValueType = StatValueType.VIEW;

        when(boardRepository.selectStatsByHour(anyString(), any(LocalDate.class), any(LocalDate.class), any(StatValueType.class)))
                .thenReturn(Collections.singletonList(new StatResponse("2024-08-26T12:00", 5)));

        // when
        List<StatResponse> result = boardService.getStats(hashtag, type, start, end, statValueType);

        // then
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getDateTime()).isEqualTo("2024-08-26T12:00");
        assertThat(result.get(0).getCount()).isEqualTo(5);
    }
}