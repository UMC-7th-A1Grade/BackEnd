package com.umc7th.a1grade.domain.jwt;

public interface JwtProvider {
  String createAccessToken(String socialId);

  String createRefreshToken(String socialId);

  boolean validateToken(String token);

  String extractSocialId(String token);
}
