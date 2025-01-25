package com.umc7th.a1grade.domain.auth.controller;

import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.umc7th.a1grade.domain.auth.dto.LoginResponse;
import com.umc7th.a1grade.domain.auth.exception.status.AuthSuccessStatus;
import com.umc7th.a1grade.domain.auth.service.CookieHelper;
import com.umc7th.a1grade.domain.auth.service.OAuth2TokenService;
import com.umc7th.a1grade.domain.auth.service.TokenService;
import com.umc7th.a1grade.global.apiPayload.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "auth", description = "로그인 및 토큰 관련 API")
@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class AuthController {
  private final OAuth2TokenService googleTokenService;
  private final CookieHelper cookieHelper;
  private final TokenService tokenService;

  @GetMapping("/google")
  @Operation(
      summary = "구글 로그인",
      description =
          "구글 OAuth 인증을 처리하여 사용자 정보를 확인하고 JWT 토큰을 생성하여 반환합니다. \n"
              + "액세스 토큰은 응답 바디에 포함되며, 리프레시 토큰은 HttpOnly 쿠키에 저장됩니다.",
      parameters = {
        @Parameter(
            name = "code",
            description = "구글에서 발급된 인증 코드",
            required = true,
            example = "%4adfasdfasgjhdlskkqwtjlk")
      })
  @ApiResponses({
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "200",
        description = "로그인에 성공했습니다. JWT 액세스 토큰과 사용자 정보 반환 완료.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = LoginResponse.class)))
  })
  public ResponseEntity<ApiResponse<LoginResponse>> processGoogleLogin(
      @RequestParam("code") String code) {
    LoginResponse response = googleTokenService.handleLogin(code);
    ResponseCookie responseCookie =
        cookieHelper.createHttpOnlyCookie("refreshToken", response.getRefreshToken());

    return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
        .body(ApiResponse.of(AuthSuccessStatus._LOGIN_SUCCESS, response));
  }

  @PostMapping("/logout")
  @Operation(summary = "로그아웃", description = "사용자 로그아웃 입니다.")
  public ApiResponse logout(@AuthenticationPrincipal UserDetails userDetails) {
    return ApiResponse.of(AuthSuccessStatus._LOGOUT_SUCCESS, null);
  }

  @PostMapping("/token")
  @Operation(
      summary = "액세스 토큰 재발급",
      description = "리프레시 토큰을 사용하여 새로운 액세스 토큰과 리프레시 토큰을 재발급합니다.",
      parameters = {
        @Parameter(
            name = "Cookie",
            description = "refreshToken=JWT_REFRESH_TOKEN 형식의 HttpOnly 쿠키",
            required = true,
            in = ParameterIn.HEADER)
      })
  @ApiResponses({
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "200",
        description = "토큰 재발급 성공. 새로운 액세스 토큰과 리프레시 토큰 반환 완료.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = String.class)))
  })
  public ResponseEntity<ApiResponse<String>> refreshToken(
      @RequestHeader(value = "Cookie", required = false) String cookieHeader) {
    String refreshToken = cookieHelper.extractRefreshToken(cookieHeader);
    Map<String, String> response = tokenService.getSocialIdFronRefreshToken(refreshToken);

    ResponseCookie responseCookie =
        cookieHelper.createHttpOnlyCookie("refreshToken", response.get("refreshToken"));

    return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
        .body(
            ApiResponse.of(AuthSuccessStatus._TOKEN_REFRESH_SUCCESS, response.get("accessToken")));
  }
}
