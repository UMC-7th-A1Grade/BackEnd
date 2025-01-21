package com.umc7th.a1grade.domain.auth.service;

import java.util.Map;

public interface TokenService {
  Map<String, String> getSocialIdFronRefreshToken(String refreshToken);
}
