package com.umc7th.a1grade.domain.auth.service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.umc7th.a1grade.global.apiPayload.ApiResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.umc7th.a1grade.domain.auth.exception.AuthHandler;
import com.umc7th.a1grade.domain.auth.exception.status.AuthErrorStatus;
import com.umc7th.a1grade.domain.jwt.JwtProvider;
import com.umc7th.a1grade.domain.user.entity.User;
import com.umc7th.a1grade.domain.user.exception.status.UserErrorStatus;
import com.umc7th.a1grade.domain.user.repository.UserRepository;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Builder
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
  private final JwtProvider jwtProvider;
  private final UserRepository userRepository;
  private final RedisTemplate<String, String> redisTemplate;

  private static final String REFRESH_TOKEN_PREFIX = "REFRESH:";
  private static final String BLACKLIST_PREFIX = "BLACKLIST:";

  @Override
  public void addToBlacklist(String accessToken) {
    if (accessToken == null || !accessToken.startsWith("Bearer ")) {
      throw new AuthHandler(AuthErrorStatus._INVALID_TOKEN);
    }
    String token = accessToken.substring(7);
    long expiration = jwtProvider.getExpiration(token);
    if (expiration > 0) {
      redisTemplate
          .opsForValue()
          .set(BLACKLIST_PREFIX + token, "logout", expiration, TimeUnit.MILLISECONDS);
    }
  }

  @Override
  public boolean isBlacklisted(String accessToken) {
    return Boolean.TRUE.equals(redisTemplate.hasKey(BLACKLIST_PREFIX + accessToken));
  }

  @Override
  public Map<String, String> getSocialIdFronRefreshToken(String refreshToken) {
    validateRefreshToken(refreshToken);
    String socialId = jwtProvider.extractSocialId(refreshToken);
    log.info("socialId 출력: {}", socialId);

    User user = findUserBySocialId(socialId);
    boolean isProfileComplete = user.getNickName() != null;

    Map<String, String> newTokens = createNewTokens(socialId, isProfileComplete);

    saveRefreshTokenToRedis(socialId, newTokens.get("refreshToken"));
    return newTokens;
  }

  public void validateRefreshToken(String refreshToken) {
    if (refreshToken == null) {
      throw new AuthHandler(AuthErrorStatus._REFRESH_TOKEN_REQUIRED);
    }
    jwtProvider.validateToken(refreshToken);

    String socialId = jwtProvider.extractSocialId(refreshToken);
    String storedToken = redisTemplate.opsForValue().get(REFRESH_TOKEN_PREFIX + socialId);

    if (storedToken == null || !storedToken.equals(refreshToken)) {
      throw new AuthHandler(AuthErrorStatus._TOKEN_FAIL);
    }
  }

  public User findUserBySocialId(String socialId) {
    return userRepository
        .findBySocialId(socialId)
        .orElseThrow(() -> new AuthHandler(UserErrorStatus._USER_NOT_FOUND));
  }

  public Map<String, String> createNewTokens(String socialId, boolean isProfileComplete) {
    String newAccessToken = jwtProvider.createAccessToken(socialId, isProfileComplete);
    String newRefreshToken = jwtProvider.createRefreshToken(socialId);

    return Map.of(
        "accessToken", newAccessToken,
        "refreshToken", newRefreshToken);
  }

  @Override
  public void logout(UserDetails userDetails) {
    if (userDetails == null) {
      throw new AuthHandler(AuthErrorStatus._TOKEN_FAIL);
    }
    String socialId = userDetails.getUsername();
    deleteRefreshToken(socialId);

    log.info("로그아웃 {} Refresh Token 삭제됨", socialId);
  }

  @Transactional
  public void saveRefreshTokenToRedis(String socialId, String newRefreshToken) {
    redisTemplate
        .opsForValue()
        .set(REFRESH_TOKEN_PREFIX + socialId, newRefreshToken, 7, TimeUnit.DAYS);
  }

  @Transactional
  public void deleteRefreshToken(String socialId) {
    redisTemplate.delete(REFRESH_TOKEN_PREFIX + socialId);
  }
}
