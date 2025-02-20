package com.umc7th.a1grade.domain.auth.service;

import com.umc7th.a1grade.domain.auth.dto.GoogleTokenResponse;
import com.umc7th.a1grade.domain.auth.dto.LoginResponse;
import com.umc7th.a1grade.domain.auth.dto.OAuthAttributes;

public interface OAuth2TokenService {
  GoogleTokenResponse getAccessToken(String code);

  OAuthAttributes getUserInfo(String accessToken);

  LoginResponse handleLogin(String code);

  LoginResponse handleTestLogin(String code);
}
