package com.umc7th.a1grade.domain.auth.service;

import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;

public interface TokenService {
  Map<String, String> getSocialIdFronRefreshToken(String refreshToken);

  String validateAndExtractSocialId(String refreshToken);

  Map<String, String> createNewTokens(String socialId, boolean isProfileComplete);

  void logout(UserDetails userDetails);

  void addToBlacklist(String accessToken);

  boolean isBlacklisted(String accessToken);
}
