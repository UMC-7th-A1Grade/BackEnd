package com.umc7th.a1grade.unittest.auth.fake;

import java.util.HashMap;
import java.util.Map;

import com.umc7th.a1grade.domain.jwt.JwtProvider;

public class FakeJwtProvider implements JwtProvider {
  private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;
  private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;

  private final Map<String, String> tokenStore = new HashMap<>();

  public String createAccessToken(String socialId) {
    String token = "mock-access-token:" + socialId;
    tokenStore.put(token, socialId);
    return token;
  }

  public String createRefreshToken(String socialId) {
    String token = "mock-refresh-token" + socialId;
    tokenStore.put(token, socialId);
    return token;
  }

  public boolean validateToken(String token) {
    return tokenStore.containsKey(token);
  }

  public String extractSocialId(String token) {
    return tokenStore.getOrDefault(token, "unknown-user");
  }

  @Override
  public String createAccessToken(String socialId, boolean idProfileComplete) {
    return null;
  }

  @Override
  public long getExpiration(String accessToken) {
    return 0;
  }
}
