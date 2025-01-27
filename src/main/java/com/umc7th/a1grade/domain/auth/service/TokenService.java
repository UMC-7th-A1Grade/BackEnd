package com.umc7th.a1grade.domain.auth.service;

import java.util.Map;

import com.umc7th.a1grade.domain.user.entity.User;

public interface TokenService {
  Map<String, String> getSocialIdFronRefreshToken(String refreshToken);

  void validateRefreshToken(String mockRefreshToken);

  User findUserBySocialId(String socialId);

  Map<String, String> createNewTokens(String socialId);

  void updateUserRefreshToken(User user, String newRefreshToken);
}
