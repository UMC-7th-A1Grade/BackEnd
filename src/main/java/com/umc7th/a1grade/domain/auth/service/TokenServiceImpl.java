package com.umc7th.a1grade.domain.auth.service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

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
  private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;
  private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 2;
  private static final int MAX_RETRY_COUNT = 5;

  @Override
  public Map<String, String> rotateRefreshToken(String refreshToken) {
    String socialId = validateAndExtractSocialId(refreshToken);
    log.info("socialId 출력: {}", socialId);

    User user =
        userRepository
            .findBySocialId(socialId)
            .orElseThrow(() -> new AuthHandler(UserErrorStatus._USER_NOT_FOUND));

    boolean isProfileComplete = user.getNickName() != null;
    Map<String, String> newTokens = createNewTokens(socialId, isProfileComplete);
    String refreshTokenKey = REFRESH_TOKEN_PREFIX + socialId;

    redisTemplate
        .opsForValue()
        .set(
            refreshTokenKey,
            newTokens.get("refreshToken"),
            REFRESH_TOKEN_EXPIRE_TIME,
            TimeUnit.MILLISECONDS);

    log.info(" Refresh Token 갱신 완료: 전{} -> 후{}", refreshToken, newTokens.get("refreshToken"));
    return newTokens;
  }

  @Override
  public void logout(UserDetails userDetails, String refreshToken) {
    if (refreshToken == null) {
      throw new AuthHandler(AuthErrorStatus._REFRESH_TOKEN_REQUIRED);
    }
    if (!jwtProvider.validateToken(refreshToken)) {
      log.error(" 로그아웃 실패 - 유효하지 않은 Refresh Token");
      throw new AuthHandler(AuthErrorStatus._LOGOUT_FAILED);
    }

    String socialId = userDetails.getUsername();
    String refreshTokenKey = REFRESH_TOKEN_PREFIX + socialId;
    String storedToken = redisTemplate.opsForValue().get(refreshTokenKey);

    if (storedToken == null) {
      log.error("로그아웃 실패 - Redis에 해당 Refresh Token이 존재하지 않음: {}", refreshTokenKey);
      throw new AuthHandler(AuthErrorStatus._LOGOUT_FAILED);
    }

    redisTemplate.delete(refreshTokenKey);
    log.info("로그아웃 - Refresh Token 삭제 완료  {}", refreshTokenKey);
  }

  public String validateAndExtractSocialId(String refreshToken) {
    if (refreshToken == null) {
      throw new AuthHandler(AuthErrorStatus._REFRESH_TOKEN_REQUIRED);
    }

    jwtProvider.validateToken(refreshToken);
    String socialId = jwtProvider.extractSocialId(refreshToken);
    log.info("아이디 {}", socialId);
    String refreshTokenKey = REFRESH_TOKEN_PREFIX + socialId;

    String storedToken = redisTemplate.opsForValue().get(refreshTokenKey);
    log.error("저장된 토큰 {}", storedToken);

    redisTemplate.delete(refreshTokenKey);
    log.info("Refresh Token 삭제 완료 : {}", refreshTokenKey);

    return socialId;
  }

  public Map<String, String> createNewTokens(String socialId, boolean isProfileComplete) {
    String newAccessToken = jwtProvider.createAccessToken(socialId, isProfileComplete);
    String newRefreshToken = jwtProvider.createRefreshToken(socialId);

    return Map.of(
        "accessToken", newAccessToken,
        "refreshToken", newRefreshToken);
  }
}
