package com.umc7th.a1grade.domain.auth.service;

import java.util.Map;
import java.util.UUID;
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
    Map<String, String> tokenInfo = validateAndExtractSocialId(refreshToken);
    String socialId = tokenInfo.get("socialId");
    String oldTokenId = tokenInfo.get("tokenId");

    log.info("요청 RT로부터 추출한 socialId {}, oldTokenId {}", socialId, oldTokenId);

    User user =
        userRepository
            .findBySocialId(socialId)
            .orElseThrow(() -> new AuthHandler(UserErrorStatus._USER_NOT_FOUND));

    boolean isProfileComplete = user.getNickName() != null;
    String newTokenId = UUID.randomUUID().toString();
    Map<String, String> newTokens = createNewTokens(socialId, isProfileComplete, newTokenId);
    String newRefreshTokenKey = REFRESH_TOKEN_PREFIX + socialId + ":" + newTokenId;

    redisTemplate
        .opsForValue()
        .set(
            newRefreshTokenKey,
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

    String socialId = jwtProvider.extractSocialId(refreshToken);
    String tokenId = jwtProvider.extractTokenId(refreshToken);

    String refreshTokenKey = REFRESH_TOKEN_PREFIX + socialId + ":" + tokenId;

    redisTemplate.delete(refreshTokenKey);
    log.info("로그아웃 - Refresh Token 삭제 완료  {}", refreshTokenKey);
  }

  public Map<String, String> validateAndExtractSocialId(String refreshToken) {
    if (refreshToken == null) {
      throw new AuthHandler(AuthErrorStatus._REFRESH_TOKEN_REQUIRED);
    }

    jwtProvider.validateToken(refreshToken);
    String socialId = jwtProvider.extractSocialId(refreshToken);
    String tokenId = jwtProvider.extractTokenId(refreshToken);
    log.info("아이디 {}, 토큰 아이디{}", socialId, tokenId);

    String refreshTokenKey = REFRESH_TOKEN_PREFIX + socialId + ":" + tokenId;
    String storedToken = redisTemplate.opsForValue().get(refreshTokenKey);
    log.error("저장된 토큰 {}", storedToken);

    if (storedToken == null) {
      log.error("토큰 검증 실패: 만료된 토큰");
      throw new AuthHandler(AuthErrorStatus._RTR_FAIL_DELETE);
    }

    redisTemplate.delete(refreshTokenKey);
    log.info("Refresh Token 삭제 완료 : {}", refreshTokenKey);

    return Map.of(
        "socialId", socialId,
        "tokenId", tokenId);
  }

  public Map<String, String> createNewTokens(
      String socialId, boolean isProfileComplete, String tokenId) {
    String newAccessToken = jwtProvider.createAccessToken(socialId, isProfileComplete);
    String newRefreshToken = jwtProvider.createRefreshToken(socialId, tokenId);

    return Map.of(
        "accessToken", newAccessToken,
        "refreshToken", newRefreshToken);
  }
}
