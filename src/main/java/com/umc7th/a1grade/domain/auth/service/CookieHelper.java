package com.umc7th.a1grade.domain.auth.service;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import com.umc7th.a1grade.domain.auth.exception.AuthHandler;
import com.umc7th.a1grade.domain.auth.exception.status.AuthErrorStatus;

@Component
public class CookieHelper {
  public ResponseCookie createHttpOnlyCookie(String name, String value) {
    return ResponseCookie.from(name, value)
        .httpOnly(true)
        .secure(true)
        .path("/")
        .maxAge(7 * 24 * 60 * 60)
        .sameSite("Strict")
        .build();
  }

  public String extractRefreshToken(String cookieHeader) {
    if (cookieHeader == null || !cookieHeader.contains("refreshToken")) {
      throw new AuthHandler(AuthErrorStatus._TOKEN_FAIL);
    }
    for (String cookie : cookieHeader.split(";")) {
      if (cookie.trim().startsWith("refreshToken=")) {
        return cookie.split("=")[1].trim();
      }
    }
    throw new AuthHandler(AuthErrorStatus._TOKEN_FAIL);
  }
}
