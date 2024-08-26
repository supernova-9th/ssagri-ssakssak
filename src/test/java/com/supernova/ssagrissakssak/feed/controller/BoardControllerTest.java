package com.supernova.ssagrissakssak.feed.controller;

import com.supernova.ssagrissakssak.core.enums.ContentType;
import com.supernova.ssagrissakssak.core.security.LoginUser;
import com.supernova.ssagrissakssak.feed.controller.response.BoardDetailResponse;
import com.supernova.ssagrissakssak.feed.controller.response.BoardResponse;
import com.supernova.ssagrissakssak.feed.persistence.repository.entity.HashtagEntity;
import com.supernova.ssagrissakssak.feed.service.BoardService;
import com.supernova.ssagrissakssak.mockuser.MockUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BoardController.class)
class BoardControllerTest extends RestDocsSupport {

    @MockBean
    private BoardService boardService;

    private BoardDetailResponse boardDto;
    private BoardResponse boardsDto1;
    private BoardResponse boardsDto2;

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

        // Given
        LocalDateTime now = LocalDateTime.now();
        boardDto = BoardDetailResponse.builder()
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

        boardsDto1 = BoardResponse.builder()
                .id(2L)
                .type(ContentType.FACEBOOK)
                .title("제목 테스트2")
                .content("테스트 내용 입니다.")
                .viewCount(10)
                .likeCount(5)
                .shareCount(2)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        boardsDto2 = BoardResponse.builder()
                .id(3L)
                .type(ContentType.FACEBOOK)
                .title("제목 테스트3")
                .content("테스트 내용 입니다.")
                .viewCount(10)
                .likeCount(5)
                .shareCount(2)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

    }

    @Test
    @MockUser
    void 게시물_상세조회_API() throws Exception {

        // When
        when(boardService.getBoard(1L)).thenReturn(boardDto);

        // Then
        mockMvc.perform(get("/boards/{id}", 1L)
                        .header(AUTHORIZATION, BEARER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("board-getById",
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description(ACCESS_TOKEN)
                        ),
                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER)
                                        .description("응답 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("결과"),
                                fieldWithPath("result").type(JsonFieldType.OBJECT)
                                        .description("응답 데이터"),
                                fieldWithPath("result.id").type(JsonFieldType.NUMBER)
                                        .description("게시물 아이디"),
                                fieldWithPath("result.type").type(JsonFieldType.STRING)
                                        .description("SNS 종류"),
                                fieldWithPath("result.title").type(JsonFieldType.STRING)
                                        .description("제목"),
                                fieldWithPath("result.content").type(JsonFieldType.STRING)
                                        .description("내용"),
                                fieldWithPath("result.hashtags").type(JsonFieldType.ARRAY)
                                        .description("해시 태그"),
                                fieldWithPath("result.viewCount").type(JsonFieldType.NUMBER)
                                        .description("조회 수"),
                                fieldWithPath("result.likeCount").type(JsonFieldType.NUMBER)
                                        .description("좋아요 수"),
                                fieldWithPath("result.shareCount").type(JsonFieldType.NUMBER)
                                        .description("공유 수"),
                                fieldWithPath("result.createdAt").type(JsonFieldType.STRING)
                                        .description("생성 일자"),
                                fieldWithPath("result.updatedAt").type(JsonFieldType.STRING)
                                        .description("수정 일자")
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