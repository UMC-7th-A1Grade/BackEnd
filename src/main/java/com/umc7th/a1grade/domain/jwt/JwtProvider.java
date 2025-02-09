package com.umc7th.a1grade.domain.jwt;

public interface JwtProvider {

  String createRefreshToken(String socialId, String tokenId);

  boolean validateToken(String token);

  String extractSocialId(String token);

  String createAccessToken(String socialId, boolean idProfileComplete);

  long getExpiration(String accessToken);

  String extractTokenId(String refreshToken);
}
