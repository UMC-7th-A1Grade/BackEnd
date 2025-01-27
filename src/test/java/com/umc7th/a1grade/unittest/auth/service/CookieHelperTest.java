package com.umc7th.a1grade.unittest.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseCookie;

import com.umc7th.a1grade.domain.auth.exception.AuthHandler;
import com.umc7th.a1grade.domain.auth.exception.status.AuthErrorStatus;
import com.umc7th.a1grade.domain.auth.service.CookieHelper;

public class CookieHelperTest {
  private final CookieHelper cookieHelper = new CookieHelper();

  @Test
  @DisplayName("[createHttpOnlyCookie] - 정상적인 HttpOnly 쿠키 생성")
  void createHttpOnlyCookie_Success() {
    // Given
    String name = "refreshToken";
    String value = "mock-refresh-token";

    // When
    ResponseCookie cookie = cookieHelper.createHttpOnlyCookie(name, value);

    // Then
    assertThat(cookie.getName()).isEqualTo(name);
    assertThat(cookie.getValue()).isEqualTo(value);
    assertThat(cookie.isHttpOnly()).isTrue();
    assertThat(cookie.isSecure()).isTrue();
    assertThat(cookie.getPath()).isEqualTo("/");
    assertThat(cookie.getMaxAge().getSeconds()).isEqualTo(7 * 24 * 60 * 60);
    assertThat(cookie.getSameSite()).isEqualTo("Strict");
  }

  @Test
  @DisplayName("[extractRefreshToken] - 정상적인 쿠키에서 refreshToken 추출")
  void extractRefreshToken_Success() {
    // Given
    String cookieHeader = "refreshToken=mock-refresh-token; Path=/; HttpOnly";

    // When
    String extractedToken = cookieHelper.extractRefreshToken(cookieHeader);

    // Then
    assertThat(extractedToken).isEqualTo("mock-refresh-token");
  }

  @Test
  @DisplayName("[extractRefreshToken] - 쿠키가 null일 경우 예외 발생")
  void extractRefreshToken_NullCookieHeader() {
    // Given
    String cookieHeader = null;

    // When & Then
    AuthHandler exception =
        assertThrows(AuthHandler.class, () -> cookieHelper.extractRefreshToken(cookieHeader));
    assertThat(exception.getErrorReason().getMessage())
        .isEqualTo(AuthErrorStatus._TOKEN_FAIL.getMessage());
  }

  @Test
  @DisplayName("[extractRefreshToken] - refreshToken이 없는 경우 예외 발생")
  void extractRefreshToken_NoRefreshToken() {
    // Given
    String cookieHeader = "invalidToken=abcd1234; Path=/; HttpOnly";

    // When
    // Then
    AuthHandler exception =
        assertThrows(AuthHandler.class, () -> cookieHelper.extractRefreshToken(cookieHeader));
    assertThat(exception.getErrorReason().getMessage())
        .isEqualTo(AuthErrorStatus._TOKEN_FAIL.getMessage());
  }

  @Test
  @DisplayName("[extractRefreshToken]- 여러 개의 쿠키 중 refreshToken 올바르게 추출")
  void extractRefreshToken_MultipleCookies() {
    // Given
    String cookieHeader =
        "sessionToken=abcd1234; refreshToken=mock-refresh-token; otherToken=xyz789";

    // When
    String extractedToken = cookieHelper.extractRefreshToken(cookieHeader);

    // Then
    assertThat(extractedToken).isEqualTo("mock-refresh-token");
  }
}
