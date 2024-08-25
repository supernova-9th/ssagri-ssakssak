package com.supernova.ssagrissakssak.feed.controller;

import com.supernova.ssagrissakssak.core.enums.ContentType;
import com.supernova.ssagrissakssak.feed.controller.response.BoardDetailResponse;
import com.supernova.ssagrissakssak.feed.persistence.repository.BoardRepository;
import com.supernova.ssagrissakssak.feed.service.BoardService;
import com.supernova.ssagrissakssak.mockuser.MockUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import java.time.LocalDateTime;
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
class BoardControllerTest extends RestDocsSupport{

    @MockBean
    BoardService boardService;

    @MockBean
    BoardRepository boardRepository;

    @InjectMocks
    private BoardController boardController;

    private BoardDetailResponse savedDto1;
    private BoardDetailResponse savedDto2;

    @BeforeEach
    void setUp() {

        // Given
        LocalDateTime now = LocalDateTime.now();
        savedDto1 = BoardDetailResponse.builder()
                .id(1L)
                .type(String.valueOf(ContentType.FACEBOOK))
                .title("제목 테스트1")
                .content("테스트 내용 입니다.")
                .hashtags(List.of("#테스트1", "#테스트2", "#테스트3"))
                .viewCount(10)
                .likeCount(5)
                .shareCount(2)
                .createdAt(now)
                .updatedAt(now)
                .build();

        savedDto2 = BoardDetailResponse.builder()
                .id(2L)
                .type(String.valueOf(ContentType.INSTAGRAM))
                .title("제목 테스트2")
                .content("내용 테스트2")
                .hashtags(List.of("#테스트4", "#테스트5"))
                .viewCount(20)
                .likeCount(15)
                .shareCount(8)
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    @Test
    @MockUser
    @DisplayName("게시물 ID로 조회 요청 시 올바른 응답 반환")
    void getBoard() throws Exception {

        // When
        when(boardService.getBoard(1L)).thenReturn(savedDto1);

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