package com.supernova.ssagrissakssak.feed.controller;

import com.supernova.ssagrissakssak.core.enums.ContentType;
import com.supernova.ssagrissakssak.feed.controller.response.BoardDetailResponse;
import com.supernova.ssagrissakssak.feed.persistence.repository.entity.HashtagEntity;
import com.supernova.ssagrissakssak.feed.service.BoardService;
import com.supernova.ssagrissakssak.mockuser.MockUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BoardController.class)
class BoardControllerTest extends RestDocsSupport {

    @MockBean
    private BoardService boardService;

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

        // Given
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
    void 게시물_상세조회_하면_성공한다() throws Exception {

        // When
        when(boardService.getBoard(1L)).thenReturn(dto1);

        // Then
        mockMvc.perform(get("/boards/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("board-getById",
                        preprocessResponse(prettyPrint()),
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
}