package com.supernova.ssagrissakssak.feed.controller;

import com.supernova.ssagrissakssak.feed.controller.request.ApproveRequestModel;
import com.supernova.ssagrissakssak.feed.controller.request.UserCreateRequest;
import com.supernova.ssagrissakssak.feed.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest extends RestDocsSupport {

    @MockBean
    UserService userService;

    @Test
    void join() throws Exception {
        // given
        var request = new UserCreateRequest("test@email.com", "password123");
        given(userService.join(any())).willReturn(1L);

        // when & then
        mockMvc.perform(post("/auth/users")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("user-create",
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING)
                                        .description("유저 이메일"),
                                fieldWithPath("password").type(JsonFieldType.STRING)
                                        .description("비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER)
                                        .description("응답 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("결과"),
                                fieldWithPath("result").type(JsonFieldType.OBJECT)
                                        .description("응답 데이터"),
                                fieldWithPath("result.id").type(JsonFieldType.NUMBER)
                                        .description("생성 아이디")
                        )
                ));
    }

    @Test
    void approve() throws Exception {
        // given
        var request = new ApproveRequestModel("test@email.com", "password123", "ABC123");

        // when & then
        mockMvc.perform(put("/auth/users/approve")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("user-approve",
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING)
                                        .description("유저 이메일"),
                                fieldWithPath("password").type(JsonFieldType.STRING)
                                        .description("비밀번호"),
                                fieldWithPath("authenticationCode").type(JsonFieldType.STRING)
                                        .description("인증 코드")
                        ),
                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER)
                                        .description("응답 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("결과")
                        )
                ));
    }
}