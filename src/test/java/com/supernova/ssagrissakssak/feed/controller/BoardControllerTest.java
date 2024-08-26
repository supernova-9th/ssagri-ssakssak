package com.supernova.ssagrissakssak.feed.controller;

import com.supernova.ssagrissakssak.core.exception.BoardNotFoundException;
import com.supernova.ssagrissakssak.core.exception.ExternalApiException;
import com.supernova.ssagrissakssak.feed.service.BoardService;
import com.supernova.ssagrissakssak.mockuser.MockUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BoardController.class)
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
class BoardControllerTest extends RestDocsSupport {

    @MockBean
    private BoardService boardService;

    private String createMockJwtToken() {
        return "mock.jwt.token";
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
        String token = createMockJwtToken();
        doThrow(new BoardNotFoundException(1L)).when(boardService).addLikeBoardContent(eq(1L), any(Long.class));

        mockMvc.perform(post("/boards/{id}/like", 1L)
                        .header(AUTHORIZATION, BEARER_TOKEN))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("게시물을 찾을 수 없습니다."))
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
        doThrow(new BoardNotFoundException(1L)).when(boardService).shareBoardContent(eq(1L), any(Long.class));

        mockMvc.perform(post("/boards/{id}/share", 1L)
                        .header(AUTHORIZATION, BEARER_TOKEN))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("게시물을 찾을 수 없습니다."))
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

}