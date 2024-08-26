package com.supernova.ssagrissakssak.feed.controller;

import com.supernova.ssagrissakssak.feed.controller.request.RefreshTokenRequest;
import com.supernova.ssagrissakssak.feed.controller.response.TokenResponse;
import com.supernova.ssagrissakssak.feed.service.TokenService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TokenController.class)
class TokenControllerTest extends RestDocsSupport {

    @MockBean
    TokenService tokenService;

    @Test
    @DisplayName("토큰 재발급 테스트")
    void reissue() throws Exception {
        // given
        var request = new RefreshTokenRequest("refresh-token");
        given(tokenService.reissue(any(RefreshTokenRequest.class))).willReturn(new TokenResponse("access", "refresh"));

        // when & then
        mockMvc.perform(post("/auth/token/reissue")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("token-reissue",
                        requestFields(
                                fieldWithPath("refreshToken").type(JsonFieldType.STRING)
                                        .description("refresh token")
                        ),
                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER)
                                        .description("응답 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("결과"),
                                fieldWithPath("result.accessToken").type(JsonFieldType.STRING)
                                        .description("Access token"),
                                fieldWithPath("result.refreshToken").type(JsonFieldType.STRING)
                                        .description("Refresh token")
                        )
                ));
    }
}