package com.supernova.ssagrissakssak.feed.controller;

import com.supernova.ssagrissakssak.core.enums.ContentType;
import com.supernova.ssagrissakssak.core.enums.StatisticsTimeType;
import com.supernova.ssagrissakssak.core.enums.StatisticsType;
import com.supernova.ssagrissakssak.core.exception.BoardNotFoundException;
import com.supernova.ssagrissakssak.core.exception.ExternalApiException;
import com.supernova.ssagrissakssak.feed.controller.response.BoardDetailResponse;
import com.supernova.ssagrissakssak.feed.controller.response.BoardResponse;
import com.supernova.ssagrissakssak.feed.persistence.repository.entity.HashtagEntity;
import com.supernova.ssagrissakssak.feed.persistence.repository.model.StatisticsDto;
import com.supernova.ssagrissakssak.feed.service.BoardService;
import com.supernova.ssagrissakssak.feed.service.BoardStatisticsService;
import com.supernova.ssagrissakssak.mockuser.MockUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BoardController.class)
class BoardControllerTest extends RestDocsSupport {

    @MockBean
    private BoardService boardService;

    @MockBean
    private BoardStatisticsService boardStatisticsService;

    private BoardDetailResponse dto1;

    @BeforeEach
    void setUp() {

        HashtagEntity hashtag1 = HashtagEntity.builder()
                .hashtag("#맛집")
                .boardId(1L)
                .build();

        HashtagEntity hashtag2 = HashtagEntity.builder()
                .hashtag("#여행")
                .boardId(1L)
                .build();

        List<String> hashtags = Arrays.asList(hashtag1, hashtag2)
                .stream()
                .map(HashtagEntity::getHashtag)
                .toList();

        LocalDateTime now = LocalDateTime.now();
        dto1 = BoardDetailResponse.builder()
                .id(1L)
                .type(ContentType.FACEBOOK)
                .title("제목 테스트1")
                .content("테스트 내용 입니다.")
                .hashtags(hashtags)
                .viewCount(10)
                .likeCount(5)
                .shareCount(2)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    @MockUser
    void addLikeBoardContent_Success() throws Exception {
        doNothing().when(boardService).addLikeBoardContent(eq(1L), any(Long.class));

        mockMvc.perform(post("/boards/{id}/like", 1L)
                        .header(AUTHORIZATION, BEARER_TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("success"))
                .andDo(document("board-like-success",
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description(ACCESS_TOKEN)
                        ),
                        pathParameters(
                                parameterWithName("id").description("좋아요 누른 게시물 ID")
                        ),
                        responseFields(
                                fieldWithPath("status").description("응답 상태 코드"),
                                fieldWithPath("message").description("응답 메시지")
                        )
                ));
        verify(boardService).addLikeBoardContent(eq(1L), any(Long.class));
    }

    @Test
    @MockUser
    void addLikeBoardContent_BoardNotFound() throws Exception {
        doThrow(new BoardNotFoundException()).when(boardService).addLikeBoardContent(eq(1L), any(Long.class));

        mockMvc.perform(post("/boards/{id}/like", 1L)
                        .header(AUTHORIZATION, BEARER_TOKEN))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("해당 자원을 찾을 수 없습니다."))
                .andDo(document("board-like-not-found",
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer JWT token")
                        ),
                        pathParameters(
                                parameterWithName("id").description("좋아요 누른 게시물 ID")
                        ),
                        responseFields(
                                fieldWithPath("status").description("응답 상태 코드"),
                                fieldWithPath("message").description("응답 메시지")
                        )
                ));
    }

    @Test
    @MockUser
    void addLikeBoardContent_SnsApiFailure() throws Exception {
        doThrow(new ExternalApiException("좋아요 API 호출 실패")).when(boardService).addLikeBoardContent(eq(1L), any(Long.class));

        mockMvc.perform(post("/boards/{id}/like", 1L)
                        .header(AUTHORIZATION, BEARER_TOKEN))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.message").value("외부 API 호출 중 오류가 발생했습니다."))
                .andDo(document("board-like-sns-api-failure",
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer JWT token")
                        ),
                        pathParameters(
                                parameterWithName("id").description("좋아요 누른 게시물 ID")
                        ),
                        responseFields(
                                fieldWithPath("status").description("응답 상태 코드"),
                                fieldWithPath("message").description("응답 메시지")
                        )
                ));
    }

    @Test
    @DisplayName("게시물 상세조회 API")
    @MockUser
    void 게시물_상세조회_하면_성공한다() throws Exception {
        when(boardService.getBoard(1L)).thenReturn(dto1);

        mockMvc.perform(get("/boards/{id}", 1L)
                        .header(AUTHORIZATION, BEARER_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("board-getById",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER).description("응답 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과"),
                                fieldWithPath("result").type(JsonFieldType.OBJECT).description("응답 데이터"),
                                fieldWithPath("result.id").type(JsonFieldType.NUMBER).description("게시물 아이디"),
                                fieldWithPath("result.type").type(JsonFieldType.STRING).description("SNS 종류"),
                                fieldWithPath("result.title").type(JsonFieldType.STRING).description("제목"),
                                fieldWithPath("result.content").type(JsonFieldType.STRING).description("내용"),
                                fieldWithPath("result.hashtags").type(JsonFieldType.ARRAY).description("해시 태그"),
                                fieldWithPath("result.viewCount").type(JsonFieldType.NUMBER).description("조회 수"),
                                fieldWithPath("result.likeCount").type(JsonFieldType.NUMBER).description("좋아요 수"),
                                fieldWithPath("result.shareCount").type(JsonFieldType.NUMBER).description("공유 수"),
                                fieldWithPath("result.createdAt").type(JsonFieldType.STRING).description("생성 일자"),
                                fieldWithPath("result.updatedAt").type(JsonFieldType.STRING).description("수정 일자")
                        )
                ));
    }

    @Test
    @MockUser
    void shareBoardContent_Success() throws Exception {
        doNothing().when(boardService).shareBoardContent(eq(1L), any(Long.class));

        mockMvc.perform(post("/boards/{id}/share", 1L)
                        .header(AUTHORIZATION, BEARER_TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("success"))
                .andDo(document("board-share-success",
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description(ACCESS_TOKEN)
                        ),
                        pathParameters(
                                parameterWithName("id").description("공유할 게시물 ID")
                        ),
                        responseFields(
                                fieldWithPath("status").description("응답 상태 코드"),
                                fieldWithPath("message").description("응답 메시지")
                        )
                ));

        verify(boardService).shareBoardContent(eq(1L), any(Long.class));
    }

    @Test
    @MockUser
    void shareBoardContent_BoardNotFound() throws Exception {
        doThrow(new BoardNotFoundException()).when(boardService).shareBoardContent(eq(1L), any(Long.class));

        mockMvc.perform(post("/boards/{id}/share", 1L)
                        .header(AUTHORIZATION, BEARER_TOKEN))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("해당 자원을 찾을 수 없습니다."))
                .andDo(document("board-share-not-found",
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description(ACCESS_TOKEN)
                        ),
                        pathParameters(
                                parameterWithName("id").description("공유할 게시물 ID")
                        ),
                        responseFields(
                                fieldWithPath("status").description("응답 상태 코드"),
                                fieldWithPath("message").description("응답 메시지")
                        )
                ));
    }

    @Test
    @MockUser
    void shareBoardContent_SnsApiFailure() throws Exception {
        doThrow(new ExternalApiException("공유 API 호출 실패")).when(boardService).shareBoardContent(eq(1L), any(Long.class));

        mockMvc.perform(post("/boards/{id}/share", 1L)
                        .header(AUTHORIZATION, BEARER_TOKEN))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.message").value("외부 API 호출 중 오류가 발생했습니다."))
                .andDo(document("board-share-sns-api-failure",
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description(ACCESS_TOKEN)
                        ),
                        pathParameters(
                                parameterWithName("id").description("공유할 게시물 ID")
                        ),
                        responseFields(
                                fieldWithPath("status").description("응답 상태 코드"),
                                fieldWithPath("message").description("응답 메시지")
                        )
                ));
    }

    @DisplayName("통계 API(DATE)")
    void getBoardStats() throws Exception {
        given(boardStatisticsService.getStatistics(any(), any(), any(), any(), any())).willReturn(
                List.of(
                        new StatisticsDto(LocalDateTime.of(2024, 8, 24, 1, 0, 0), 3L),
                        new StatisticsDto(LocalDateTime.of(2024, 8, 25, 2, 0, 0), 30L)
                )
        );

        mockMvc.perform(get("/boards/stats")
                        .header(AUTHORIZATION, BEARER_TOKEN)
                        .param("hashtag", "hashtag")
                        .param("type", StatisticsType.COUNT.toString())
                        .param("timeType", StatisticsTimeType.DATE.toString())
                        .param("start", "2024-08-24")
                        .param("end", "2024-08-25"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("boards-stats-date",
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description(ACCESS_TOKEN)
                        ),
                        queryParameters(
                                parameterWithName("hashtag").description("해시태그"),
                                parameterWithName("type").description("조회 타입(COUNT, VIEW_COUNT, LIKE_COUNT, SHARE_COUNT)"),
                                parameterWithName("timeType").description("조회 타입(DAYS, HOUR)"),
                                parameterWithName("start").description("조회 시작 날짜"),
                                parameterWithName("end").description("조회 마지막 날짜")
                        ),
                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER).description("응답 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과"),
                                fieldWithPath("result").type(JsonFieldType.ARRAY).description("응답 데이터"),
                                fieldWithPath("result[].time").type(JsonFieldType.STRING).description("날짜"),
                                fieldWithPath("result[].value").type(JsonFieldType.NUMBER).description("조회 타입 결과")
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
                        .header(AUTHORIZATION, BEARER_TOKEN)
                        .param("hashtag", "hashtag")
                        .param("type", StatisticsType.COUNT.toString())
                        .param("timeType", StatisticsTimeType.HOUR.toString())
                        .param("start", "2024-08-24")
                        .param("end", "2024-08-25")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("boards-stats-hour",
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description(ACCESS_TOKEN)
                        ),
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

    @Test
    @MockUser
    void 게시물_목록조회_API() throws Exception {

        // Given
        List<BoardResponse> boardResponses = Arrays.asList(boardsDto1, boardsDto2);

        // When
        when(boardService.getBoards(anyString(), anyString(), anyString(), anyString(), anyString(), anyInt(), anyInt()))
                .thenReturn(boardResponses);

        // Then
        mockMvc.perform(get("/boards")
                        .param("hashtag", "test")
                        .param("type", "FACEBOOK")
                        .param("orderBy", "created_at")
                        .param("searchBy", "title")
                        .param("search", "테스트")
                        .param("pageCount", "10")
                        .param("page", "0")
                        .header(AUTHORIZATION, BEARER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("board-getAll",
                        preprocessResponse(),
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description(ACCESS_TOKEN)
                        ),
                        queryParameters(
                                parameterWithName("hashtag").description("해시태그"),
                                parameterWithName("type").description("조회 타입(FACEBOOK, TWITTER, INSTAGRAM, THREADS)"),
                                parameterWithName("searchBy").description("검색 기준"),
                                parameterWithName("search").description("검색 값"),
                                parameterWithName("orderBy").description("정렬 기준(created_at,updated_at,like_count,share_count,view_count)"),
                                parameterWithName("pageCount").description("페이지당 게시물 갯수"),
                                parameterWithName("page").description("조회하려는 페이지")
                        ),
                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER)
                                        .description("응답 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("결과"),
                                fieldWithPath("result").type(JsonFieldType.ARRAY)
                                        .description("응답 데이터"),
                                fieldWithPath("result[].id").type(JsonFieldType.NUMBER)
                                        .description("게시물 아이디"),
                                fieldWithPath("result[].type").type(JsonFieldType.STRING)
                                        .description("SNS 종류"),
                                fieldWithPath("result[].title").type(JsonFieldType.STRING)
                                        .description("제목"),
                                fieldWithPath("result[].content").type(JsonFieldType.STRING)
                                        .description("내용"),
                                fieldWithPath("result[].viewCount").type(JsonFieldType.NUMBER)
                                        .description("조회 수"),
                                fieldWithPath("result[].likeCount").type(JsonFieldType.NUMBER)
                                        .description("좋아요 수"),
                                fieldWithPath("result[].shareCount").type(JsonFieldType.NUMBER)
                                        .description("공유 수"),
                                fieldWithPath("result[].createdAt").type(JsonFieldType.STRING)
                                        .description("생성 일자"),
                                fieldWithPath("result[].updatedAt").type(JsonFieldType.STRING)
                                        .description("수정 일자")
                        )
                ));
    }
}
