package com.umc7th.a1grade.domain.jwt;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc7th.a1grade.global.apiPayload.ApiResponse;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException)
      throws IOException, ServletException {

    ApiResponse<Object> apiResponse =
        ApiResponse.onFailure("AUTH401", "Unauthorized: 인증이 필요합니다.", null);

    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.getWriter().write(convertObjectToJson(apiResponse));
  }

  private String convertObjectToJson(Object object) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.writeValueAsString(object);
  }
}
