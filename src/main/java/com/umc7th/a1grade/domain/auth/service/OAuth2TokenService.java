package com.umc7th.a1grade.domain.auth.service;

import java.util.Map;

import com.umc7th.a1grade.domain.auth.dto.GoogleTokenResponse;
import com.umc7th.a1grade.domain.auth.dto.OAuthAttributes;

public interface OAuth2TokenService {
  GoogleTokenResponse getGoogleToken(String code);

  OAuthAttributes getUserInfo(String accessToken);

  Map<String, Object> handleLogin(String code);
}
