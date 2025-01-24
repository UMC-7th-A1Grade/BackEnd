package com.umc7th.a1grade.unittest.auth.fake;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc7th.a1grade.domain.auth.dto.GoogleTokenResponse;
import com.umc7th.a1grade.domain.auth.dto.LoginResponse;
import com.umc7th.a1grade.domain.auth.dto.OAuthAttributes;
import com.umc7th.a1grade.domain.auth.exception.AuthHandler;
import com.umc7th.a1grade.domain.auth.exception.status.AuthErrorStatus;
import com.umc7th.a1grade.domain.auth.service.OAuth2TokenService;
import com.umc7th.a1grade.domain.user.entity.Role;
import com.umc7th.a1grade.domain.user.entity.User;
import com.umc7th.a1grade.unittest.user.fake.FakeUserRepository;

public class FakeOAuth2TokenService implements OAuth2TokenService {

  private final FakeUserRepository userRepository;
  private final FakeJwtProvider jwtProvider;
  private final FakeHttpClient fakeHttpClient;
  private final ObjectMapper objectMapper = new ObjectMapper();

  public FakeOAuth2TokenService(
      FakeUserRepository userRepository,
      FakeJwtProvider jwtProvider,
      FakeHttpClientImpl fakeHttpClient) {
    this.userRepository = userRepository;
    this.jwtProvider = jwtProvider;
    this.fakeHttpClient = fakeHttpClient;
  }

  @Override
  public GoogleTokenResponse getAccessToken(String code) {
    try {
      if (code == null || code.isEmpty()) {
        throw new AuthHandler(AuthErrorStatus._TOKEN_FAIL);
      }
      String responseJson = fakeHttpClient.post("http://localhost:8888/oauth2/token");
      Map<String, Object> responseMap = objectMapper.readValue(responseJson, Map.class);

      return new GoogleTokenResponse(
          (String) responseMap.get("access_token"),
          (Integer) responseMap.get("expires_in"),
          (String) responseMap.get("refresh_token"),
          (String) responseMap.get("scope"),
          (String) responseMap.get("token_type"),
          (String) responseMap.get("id_token"));

    } catch (AuthHandler e) {
      throw e;
    } catch (Exception e) {
      throw new AuthHandler(AuthErrorStatus._TOKEN_FAIL);
    }
  }

  @Override
  public OAuthAttributes getUserInfo(String accessToken) {
    Map<String, Object> responseMap = new HashMap<>();
    try {
      if (accessToken == null || accessToken.isEmpty()) {
        throw new AuthHandler(AuthErrorStatus._USER_INFO_FAIL);
      }
      String responseJson = fakeHttpClient.get("http://localhost:8888/oauth2/userinfo");
      responseMap = objectMapper.readValue(responseJson, Map.class);

    } catch (AuthHandler e) {
      throw e;
    } catch (Exception e) {
      throw new AuthHandler(AuthErrorStatus._USER_INFO_FAIL);
    }

    return OAuthAttributes.builder()
        .attributes(responseMap)
        .sub((String) responseMap.get("sub"))
        .email((String) responseMap.get("email"))
        .build();
  }

  @Override
  public LoginResponse handleLogin(String code) {
    GoogleTokenResponse tokenResponse = getAccessToken(code);
    OAuthAttributes attributes = getUserInfo(tokenResponse.getAccessToken());

    User user =
        userRepository
            .findBySocialId(attributes.getSub())
            .orElseGet(
                () -> {
                  User newUser = attributes.toEntity();
                  newUser.setRole(Role.ROLE_USER);
                  return userRepository.save(newUser);
                });

    String jwtAccessToken = jwtProvider.createAccessToken(user.getSocialId());
    String jwtRefreshToken = jwtProvider.createRefreshToken();
    user.setRefreshToken(jwtRefreshToken);

    return new LoginResponse(user.getEmail(), jwtAccessToken, user.getSocialId(), jwtRefreshToken);
  }
}
