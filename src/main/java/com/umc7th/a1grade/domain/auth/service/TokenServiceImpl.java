package com.umc7th.a1grade.domain.auth.service;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.umc7th.a1grade.domain.auth.exception.AuthHandler;
import com.umc7th.a1grade.domain.auth.exception.status.AuthErrorStatus;
import com.umc7th.a1grade.domain.jwt.JwtProvider;
import com.umc7th.a1grade.domain.user.entity.User;
import com.umc7th.a1grade.domain.user.exception.status.UserErrorStatus;
import com.umc7th.a1grade.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
  private final JwtProvider jwtProvider;
  private final UserRepository userRepository;

  @Override
  public Map<String, String> getSocialIdFronRefreshToken(String refreshToken) {
    validateRefreshToken(refreshToken);
    String socialId = jwtProvider.extractSocialId(refreshToken);
    log.info("socialId 출력: {}", socialId);
    User user = findUserBySocialId(socialId);
    Map<String, String> newTokens = createNewTokens(socialId);
    updateUserRefreshToken(user, newTokens.get("RefreshToken"));
    return newTokens;
  }

  private void validateRefreshToken(String refreshToken) {
    if (refreshToken == null) {
      throw new AuthHandler(AuthErrorStatus._REFRESH_TOKEN_REQUIRED);
    }
    jwtProvider.validateToken(refreshToken);
  }

  private User findUserBySocialId(String socialId) {
    return userRepository
        .findBySocialId(socialId)
        .orElseThrow(() -> new AuthHandler(UserErrorStatus._USER_NOT_FOUND));
  }

  private Map<String, String> createNewTokens(String socialId) {
    String newAccessToken = jwtProvider.createAccessToken(socialId);
    String newRefreshToken = jwtProvider.createRefreshToken(socialId);

    return Map.of(
        "AccessToken", newAccessToken,
        "RefreshToken", newRefreshToken);
  }

  @Transactional
  public void updateUserRefreshToken(User user, String newRefreshToken) {
    user.setRefreshToken(newRefreshToken);
    userRepository.save(user);
  }
}
