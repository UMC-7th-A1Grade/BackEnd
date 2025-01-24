package com.umc7th.a1grade.domain.auth.service;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

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
}
