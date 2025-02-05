package com.umc7th.a1grade.domain.auth.controller;

import java.util.Map;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.umc7th.a1grade.domain.auth.dto.LoginResponse;
import com.umc7th.a1grade.domain.auth.exception.status.AuthErrorStatus;
import com.umc7th.a1grade.domain.auth.exception.status.AuthSuccessStatus;
import com.umc7th.a1grade.domain.auth.service.CookieHelper;
import com.umc7th.a1grade.domain.auth.service.OAuth2TokenService;
import com.umc7th.a1grade.domain.auth.service.TokenService;
import com.umc7th.a1grade.global.annotation.ApiErrorCodeExample;
import com.umc7th.a1grade.global.apiPayload.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
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

  @Operation(
      summary = "구글 로그인",
      description =
          """
        구글 OAuth 인증을 처리하여 사용자 정보를 확인하고 JWT 토큰을 생성하여 반환합니다.
        액세스 토큰은 응답 바디에 포함되며, 리프레시 토큰은 HttpOnly 쿠키에 저장됩니다.
        """)
  @Parameters({
    @Parameter(
        name = "code",
        description = "구글에서 발급된 인증 코드",
        required = true,
        example = "4adfasdfasgjhdlskkqwtjlk")
  })
  @ApiResponses({
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "200",
        description = "로그인 성공 - JWT 액세스 토큰과 사용자 정보 반환",
        content =
            @Content(
                mediaType = "application/json",
                examples =
                    @ExampleObject(
                        value =
                            """
                {
                  "isSuccess": "true",
                  "code": "AUTH2002",
                  "message": "로그인에 성공했습니다. JWT 액세스 토큰과 사용자 정보 반환 완료.",
                  "result": {
                    "accessToken": "eyJhbGciOiJIUz...",
                     "email": "user@example.com",
                     "socialId": "adfasdfasdfsdfff.."
                    }
                  }
                """))),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "400",
        description = "구글 로그인 처리 중 오류 발생",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiResponse.class),
                examples =
                    @ExampleObject(
                        value =
                            """
                {
                  "isSuccess": "false",
                  "code": "AUTH4001",
                  "message": "구글 로그인 처리 중 오류 발생"
                }
                """))),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "401",
        description = "구글 액세스 토큰 요청 실패 또는 사용자 정보 요청 실패",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiResponse.class),
                examples = {
                  @ExampleObject(
                      name = "AUTH4002",
                      value =
                          """
                {
                  "isSuccess": "false",
                  "code": "AUTH4002",
                  "message": "구글 액세스 토큰 요청 실패"
                }
                """),
                  @ExampleObject(
                      name = "AUTH4003",
                      value =
                          """
                {
                  "isSuccess": "false",
                  "code": "AUTH4003",
                  "message": "구글 사용자 정보 요청 실패"
                }
                """)
                }))
  })
  @GetMapping(value = "/google", produces = "application/json")
  public ApiResponse<LoginResponse> processGoogleLogin(
      @RequestParam("code") String code, HttpServletResponse response) {
    LoginResponse loginResponse = googleTokenService.handleLogin(code);
    ResponseCookie responseCookie =
        cookieHelper.createHttpOnlyCookie("refreshToken", loginResponse.getRefreshToken());
    response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
    return ApiResponse.of(AuthSuccessStatus._LOGIN_SUCCESS, loginResponse);
  }

  @Operation(
      summary = "액세스 토큰 재발급",
      description = """
      리프레시 토큰을 사용하여 새로운 액세스 토큰과 리프레시 토큰을 재발급합니다.
      """,
      parameters = {
        @Parameter(
            name = "Cookie",
            description = "JWT_REFRESH_TOKEN 형식의 HttpOnly 쿠키",
            required = true,
            in = ParameterIn.HEADER)
      })
  @ApiResponses({
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "200",
        description = "토큰 재발급 성공 - 새로운 액세스 토큰 반환",
        content =
            @Content(
                mediaType = "application/json",
                examples =
                    @ExampleObject(
                        value =
                            """
                {
                  "isSuccess": "true",
                  "code": "TAUTH2004",
                  "message": "토큰 재발급에 성공했습니다.",
                  "result": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
                }
                """))),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "401",
        description = "사용자 정보 없음 - 리프레시 토큰에 해당하는 사용자를 찾을 수 없음",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiResponse.class),
                examples =
                    @ExampleObject(
                        value =
                            """
                {
                  "isSuccess": "false",
                  "code": "USER4001",
                  "message": "존재하지 않는 사용자입니다."
                }
                """))),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "403",
        description = "리프레시 토큰 필요",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiResponse.class),
                examples =
                    @ExampleObject(
                        value =
                            """
                {
                  "isSuccess": "false",
                  "code": "AUTH4006",
                  "message": "리프레시 토큰이 필요합니다."
                }
                """)))
  })
  @PostMapping(value = "/token", produces = "application/json")
  public ApiResponse<String> refreshToken(
      @RequestHeader(value = "Cookie", required = false) String cookieHeader,
      HttpServletResponse response) {
    String refreshToken = cookieHelper.extractRefreshToken(cookieHeader);
    Map<String, String> tokenResponse = tokenService.getSocialIdFronRefreshToken(refreshToken);

    ResponseCookie responseCookie =
        cookieHelper.createHttpOnlyCookie("refreshToken", tokenResponse.get("refreshToken"));

    response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
    return ApiResponse.of(
        AuthSuccessStatus._TOKEN_REFRESH_SUCCESS, tokenResponse.get("accessToken"));
  }

  @PostMapping("/logout")
  @Operation(summary = "로그아웃", description = "사용자 로그아웃 입니다.")
  @ApiErrorCodeExample(AuthErrorStatus.class)
  public ApiResponse logout(
      @AuthenticationPrincipal UserDetails userDetails,
      @RequestHeader("Authorization") String accessToken) {
    tokenService.addToBlacklist(accessToken);
    tokenService.logout(userDetails);

    return ApiResponse.of(AuthSuccessStatus._LOGOUT_SUCCESS, null);
  }
}
