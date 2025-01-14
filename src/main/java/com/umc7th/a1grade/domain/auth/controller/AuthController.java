package com.umc7th.a1grade.domain.auth.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.umc7th.a1grade.domain.auth.exception.status.AuthSuccessStatus;
import com.umc7th.a1grade.domain.auth.service.OAuth2TokenService;
import com.umc7th.a1grade.global.apiPayload.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "auth", description = "로그인 및 토큰 관련 API")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
  private final OAuth2TokenService googleTokenService;

  @GetMapping("/google")
  @Operation(
      summary = "구글 로그인",
      description = "구글의 OAuth 인증을 통해 사용자 정보를 처리하고 JWT 토큰을 생성하여 반환합니다.",
      parameters = {
        @Parameter(
            name = "code",
            description = "구글에서 발급된 인증 코드",
            required = true,
            example = "%4adfasdfasgjhdlskkqwtjlk")
      })
  public ApiResponse<Map<String, Object>> processGoogleLogin(@RequestParam("code") String code) {
    Map<String, Object> response = googleTokenService.handleLogin(code);
    return ApiResponse.of(AuthSuccessStatus._LOGIN_SUCCESS, response);
  }

  @PostMapping("/logout")
  @Operation(summary = "로그아웃", description = "사용자 로그아웃 입니다.")
  public ApiResponse<Object> logout() {
    return ApiResponse.of(AuthSuccessStatus._LOGOUT_SUCCESS, null);
  }

  @PostMapping("/token")
  @Operation(summary = "액세스 토큰 재발급", description = "리프레시 토큰을 사용하여 액세스 토큰을 재발급합니다.")
  public ApiResponse<Object> refreshToken() {
    return ApiResponse.of(AuthSuccessStatus._TOKEN_REFRESH_SUCCESS, null);
  }
}
