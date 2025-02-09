package com.umc7th.a1grade.domain.auth.service;

import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;

public interface TokenService {
  Map<String, String> rotateRefreshToken(String refreshToke);

  Map<String, String> validateAndExtractSocialId(String refreshToken);

  Map<String, String> createNewTokens(String socialId, boolean isProfileComplete, String tokenId);

  void logout(UserDetails userDetails, String refreshToken);
}
