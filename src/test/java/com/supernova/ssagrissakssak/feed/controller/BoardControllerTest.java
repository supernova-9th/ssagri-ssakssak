package com.supernova.ssagrissakssak.feed.controller;

import com.supernova.ssagrissakssak.core.enums.StatisticsTimeType;
import com.supernova.ssagrissakssak.core.enums.StatisticsType;
import com.supernova.ssagrissakssak.feed.persistence.repository.model.StatisticsDto;
import com.supernova.ssagrissakssak.feed.service.BoardStatisticsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BoardController.class)
class BoardControllerTest extends RestDocsSupport {

    @MockBean
    BoardStatisticsService boardStatisticsService;

    @Test
    @DisplayName("통계 API(DATE)")
    void getBoardStats() throws Exception {
        // given
        given(boardStatisticsService.getStatistics(any(), any(), any(), any(), any())).willReturn(
                List.of(
                        new StatisticsDto(LocalDateTime.of(2024, 8, 24, 1, 0, 0), 3L),
                        new StatisticsDto(LocalDateTime.of(2024, 8, 25, 2, 0, 0), 30L)
                )
        );

        // when & then
        mockMvc.perform(get("/boards/stats")
                .param("hashtag", "hashtag")
                .param("type", StatisticsType.COUNT.toString())
                .param("timeType", StatisticsTimeType.DATE.toString())
                .param("start", "2024-08-24")
                .param("end", "2024-08-25")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("boards-stats-date",
                        queryParameters(
                                parameterWithName("hashtag").description("해시태그"),
                                parameterWithName("type").description("조회 타입(COUNT, VIEW_COUNT, LIKE_COUNT, SHARE_COUNT)"),
                                parameterWithName("timeType").description("조회 타입(DAYS, HOUR)"),
                                parameterWithName("start").description("조회 시작 날짜"),
                                parameterWithName("end").description("조회 마지막 날짜")
                        ),
                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER)
                                        .description("응답 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("결과"),
                                fieldWithPath("result").type(JsonFieldType.ARRAY)
                                        .description("응답 데이터"),
                                fieldWithPath("result[].date").type(JsonFieldType.STRING)
                                        .description("날짜"),
                                fieldWithPath("result[].value").type(JsonFieldType.NUMBER)
                                        .description("조회 타입 결과")
                        )
                ));
    }

    @Test
    @DisplayName("통계 API(HOUR)")
    void getBoardStats2() throws Exception {
        // given
        given(boardStatisticsService.getStatistics(any(), any(), any(), any(), any())).willReturn(
                List.of(
                        new StatisticsDto(LocalDateTime.of(2024, 8, 24, 1, 0, 0), 3L),
                        new StatisticsDto(LocalDateTime.of(2024, 8, 25, 2, 0, 0), 10L),
                        new StatisticsDto(LocalDateTime.of(2024, 8, 25, 3, 0, 0), 20L)
                )
        );

        // when & then
        mockMvc.perform(get("/boards/stats")
                        .param("hashtag", "hashtag")
                        .param("type", StatisticsType.COUNT.toString())
                        .param("timeType", StatisticsTimeType.HOUR.toString())
                        .param("start", "2024-08-24")
                        .param("end", "2024-08-25")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("boards-stats-hour",
                        queryParameters(
                                parameterWithName("hashtag").description("해시태그"),
                                parameterWithName("type").description("조회 타입(COUNT, VIEW_COUNT, LIKE_COUNT, SHARE_COUNT)"),
                                parameterWithName("timeType").description("조회 타입(DAYS, HOUR)"),
                                parameterWithName("start").description("조회 시작 날짜"),
                                parameterWithName("end").description("조회 마지막 날짜")
                        ),
                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER)
                                        .description("응답 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("결과"),
                                fieldWithPath("result").type(JsonFieldType.ARRAY)
                                        .description("응답 데이터"),
                                fieldWithPath("result[].time").type(JsonFieldType.STRING)
                                        .description("날짜"),
                                fieldWithPath("result[].value").type(JsonFieldType.NUMBER)
                                        .description("조회 타입 결과")
                        )
                ));
    }
}