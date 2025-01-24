package com.umc7th.a1grade.domain.auth.service;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.umc7th.a1grade.domain.auth.dto.GoogleTokenResponse;
import com.umc7th.a1grade.domain.auth.dto.LoginResponse;
import com.umc7th.a1grade.domain.auth.dto.OAuthAttributes;
import com.umc7th.a1grade.domain.auth.exception.AuthHandler;
import com.umc7th.a1grade.domain.auth.exception.status.AuthErrorStatus;
import com.umc7th.a1grade.domain.jwt.JwtProvider;
import com.umc7th.a1grade.domain.user.entity.Role;
import com.umc7th.a1grade.domain.user.entity.User;
import com.umc7th.a1grade.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleTokenService implements OAuth2TokenService {

  private final RestTemplate restTemplate;
  private final UserRepository userRepository;
  private final JwtProvider jwtProvider;

  @Value("${spring.security.oauth2.client.registration.google.client-id}")
  private String clientId;

  @Value("${spring.security.oauth2.client.registration.google.client-secret}")
  private String clientSecret;

  @Value("${spring.security.oauth2.client.provider.google.token-uri}")
  private String tokenUri;

  @Value("${spring.security.oauth2.client.provider.google.user-info-uri}")
  private String userInfoUri;

  @Value("${spring.security.oauth2.client.registration.google.authorization-grant-type}")
  private String grantType;

  @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
  private String redirectUri;

  @Override
  @Transactional
  public LoginResponse handleLogin(String code) {
    String decode = URLDecoder.decode(code, StandardCharsets.UTF_8);
    String accessToken = getAccessToken(decode).getAccessToken();
    OAuthAttributes attributes = getUserInfo(accessToken);
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

    log.info("JWT 액세스 토큰 생성: {}", jwtAccessToken);
    log.info("JWT 리프레시 토큰 생성: {}", jwtRefreshToken);

    return new LoginResponse(user.getEmail(), jwtAccessToken, user.getSocialId(), jwtRefreshToken);
  }

  @Transactional
  public GoogleTokenResponse getAccessToken(String code) {
    String tokenUrl =
        UriComponentsBuilder.fromHttpUrl(tokenUri)
            .queryParam("grant_type", grantType)
            .queryParam("client_id", clientId)
            .queryParam("client_secret", clientSecret)
            .queryParam("redirect_uri", redirectUri)
            .queryParam("code", code)
            .build()
            .toUriString();

    try {
      ResponseEntity<GoogleTokenResponse> response =
          restTemplate.exchange(tokenUrl, HttpMethod.POST, null, GoogleTokenResponse.class);
      log.info("구글 엑세스 토큰 응답: {}", response.getBody().getAccessToken());
      return response.getBody();
    } catch (Exception e) {
      log.error("구글 엑세스 토큰 요청 실패 : {}", e.getMessage(), e);
      throw new AuthHandler(AuthErrorStatus._TOKEN_FAIL);
    }
  }

  @Override
  @Transactional
  public OAuthAttributes getUserInfo(String accessToken) {
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(accessToken);

    HttpEntity<String> request = new HttpEntity<>(headers);
    ResponseEntity<Map<String, Object>> response;
    try {
      response =
          restTemplate.exchange(
              userInfoUri, HttpMethod.GET, request, new ParameterizedTypeReference<>() {});

    } catch (Exception e) {
      throw new AuthHandler(AuthErrorStatus._USER_INFO_FAIL);
    }
    Map<String, Object> attributes = response.getBody();

    return OAuthAttributes.builder()
        .attributes(attributes)
        .email((String) attributes.get("email"))
        .sub((String) attributes.get("sub"))
        .build();
  }
}
