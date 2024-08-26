package com.supernova.ssagrissakssak.feed.controller;

import com.supernova.ssagrissakssak.core.enums.StatType;
import com.supernova.ssagrissakssak.core.enums.StatValueType;
import com.supernova.ssagrissakssak.feed.controller.response.StatResponse;
import com.supernova.ssagrissakssak.feed.service.BoardService;
import com.supernova.ssagrissakssak.mockuser.MockUser;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BoardController.class)
@AutoConfigureMockMvc
class BoardControllerTest extends RestDocsSupport {

    @MockBean
    BoardService boardService;

    @Test
    @MockUser
    void 통계_조회() throws Exception {
        LocalDate startDate = LocalDate.now().minusDays(7);
        LocalDate endDate = LocalDate.now();

        given(boardService.getStats(anyString(), any(StatType.class), any(LocalDate.class), any(LocalDate.class), any(StatValueType.class)))
                .willReturn(Collections.singletonList(new StatResponse("2024-08-26", 10)));

        // when & then
        mockMvc.perform(get("/boards/stats")
                        .param("statType", "DATE")
                        .param("start", startDate.toString())
                        .param("end", endDate.toString())
                        .param("statValueType", "count")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("board-stats",
                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER)
                                        .description("응답 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("결과"),
                                fieldWithPath("result[].createdAt").type(JsonFieldType.STRING)
                                        .description("날짜"),
                                fieldWithPath("result[].count").type(JsonFieldType.NUMBER)
                                        .description("값")
                        )
                ));
    }
}