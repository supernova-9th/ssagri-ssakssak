package com.supernova.ssagrissakssak.core.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supernova.ssagrissakssak.core.wrapper.ResultResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        var errorMessage = authException.getMessage();
        var errorResponse = new ResultResponse<>(HttpStatus.UNAUTHORIZED, errorMessage);
        var json = objectMapper.writeValueAsString(errorResponse);

        response.getWriter().write(json);
    }
}
