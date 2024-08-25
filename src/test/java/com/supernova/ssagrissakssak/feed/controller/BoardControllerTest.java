package com.supernova.ssagrissakssak.feed.controller;

import com.supernova.ssagrissakssak.core.exception.BoardNotFoundException;
import com.supernova.ssagrissakssak.core.exception.SnsApiException;
import com.supernova.ssagrissakssak.feed.service.BoardService;
import com.supernova.ssagrissakssak.mockuser.MockUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BoardController.class)
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
class BoardControllerTest extends RestDocsSupport {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BoardService boardService;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext,
               RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    @MockUser
    void addLikeBoardContent_Success() throws Exception {
        doNothing().when(boardService).addLikeBoardContent("test123");

        mockMvc.perform(post("/boards/{contentId}/like", "test123")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("success"))
                .andDo(document("board-like-success",
                        pathParameters(
                                parameterWithName("contentId").description("좋아요 누른 게시물 ID")
                        ),
                        responseFields(
                                fieldWithPath("status").description("응답 상태 코드"),
                                fieldWithPath("message").description("응답 메시지")
                        )
                ));
    }

    @Test
    @MockUser
    void addLikeBoardContent_BoardNotFound() throws Exception {
        doThrow(new BoardNotFoundException("test123")).when(boardService).addLikeBoardContent("test123");

        mockMvc.perform(post("/boards/{contentId}/like", "test123")
                        .with(csrf()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("게시물을 찾을 수 없습니다. ContentId: test123"))
                .andDo(document("board-like-not-found",
                        pathParameters(
                                parameterWithName("contentId").description("좋아요 누른 게시물 ID")
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
        doThrow(new SnsApiException("좋아요 API 호출 실패")).when(boardService).addLikeBoardContent("test123");

        mockMvc.perform(post("/boards/{contentId}/like", "test123")
                        .with(csrf()))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.message").value("SNS API 호출 중 오류 발생: 좋아요 API 호출 실패"))
                .andDo(document("board-like-sns-api-failure",
                        pathParameters(
                                parameterWithName("contentId").description("좋아요 누른 게시물 ID")
                        ),
                        responseFields(
                                fieldWithPath("status").description("응답 상태 코드"),
                                fieldWithPath("message").description("응답 메시지")
                        )
                ));
    }
}