package com.umc7th.a1grade.unittest.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.umc7th.a1grade.domain.auth.dto.GoogleTokenResponse;
import com.umc7th.a1grade.domain.auth.dto.LoginResponse;
import com.umc7th.a1grade.domain.auth.dto.OAuthAttributes;
import com.umc7th.a1grade.domain.auth.exception.AuthHandler;
import com.umc7th.a1grade.domain.user.entity.Role;
import com.umc7th.a1grade.domain.user.entity.User;
import com.umc7th.a1grade.unittest.auth.fake.FakeHttpClientImpl;
import com.umc7th.a1grade.unittest.auth.fake.FakeJwtProvider;
import com.umc7th.a1grade.unittest.auth.fake.FakeOAuth2TokenService;
import com.umc7th.a1grade.unittest.user.fake.FakeUserRepository;

@SpringBootTest
public class GoogleTokenServiceTest {
  private FakeOAuth2TokenService fakeOAuth2TokenService;
  private FakeUserRepository fakeUserRepository;
  private FakeJwtProvider fakeJwtProvider;
  private FakeHttpClientImpl fakeHttpClient;
  private final Long testUserId = 1L;
  private User existingUser;

  @BeforeEach
  void init() {
    fakeUserRepository = new FakeUserRepository();
    fakeJwtProvider = new FakeJwtProvider();
    fakeHttpClient = new FakeHttpClientImpl();
    fakeOAuth2TokenService =
        new FakeOAuth2TokenService(fakeUserRepository, fakeJwtProvider, fakeHttpClient);

    this.existingUser =
        User.builder()
            .nickName("existing-test-nickname")
            .socialId("existing-test-social-id")
            .email("existing-test-email")
            .role(Role.ROLE_USER)
            .build();

    fakeUserRepository.save(existingUser);
  }

  @Test
  @DisplayName("[handleLogin] 구글 엑세스 토큰 발급 - 정상 요청 ( 새로운 유저일 경우 새로운 유저 정보 저장 )")
  void getGoogleAccessTokenTest_WithNewUser() {
    // given
    String authorizationCode = "mock_auth_code";

    // when
    LoginResponse response = fakeOAuth2TokenService.handleLogin(authorizationCode);

    // then
    User newUser = fakeUserRepository.findBySocialId(response.getSocialId()).get();
    assertAll(
        () -> assertNotNull(response),
        () -> assertEquals("test@example.com", response.getEmail()),
        () -> assertEquals("mock-access-token:test-social-id", response.getAccessToken()),
        () -> assertThat(response.getSocialId()).isEqualTo("test-social-id"),
        () -> assertThat(newUser.getSocialId()).isEqualTo("test-social-id"),
        () -> assertThat(newUser.getId()).isEqualTo(2L),
        () -> assertTrue(fakeUserRepository.findBySocialId("test-social-id").isPresent()));
  }

  @Test
  @DisplayName("[handleLogin] 구글 엑세스 토큰 발급 - 정상 요청 ( 유저가 이미 존재할 경우 기존 유저 데이터 반환 )")
  void getGoogleAccessTokenTest_WithExistingUser() {
    // given
    User existingUser =
        User.builder()
            .socialId("test-social-id")
            .email("test@example.com")
            .role(Role.ROLE_USER)
            .build();
    fakeUserRepository.save(existingUser);
    String authorizationCode = "mock_auth_code";

    // when
    LoginResponse response = fakeOAuth2TokenService.handleLogin(authorizationCode);

    // then
    User user = fakeUserRepository.findBySocialId(response.getSocialId()).get();
    assertAll(
        () -> assertNotNull(response),
        () -> assertEquals("test@example.com", response.getEmail()),
        () -> assertEquals("mock-access-token:test-social-id", response.getAccessToken()),
        () -> assertThat(response.getSocialId()).isEqualTo(user.getSocialId()),
        () -> assertThat(user.getId()).isEqualTo(2L),
        () -> assertEquals(2, fakeUserRepository.findAll().size()));
  }

  @Test
  @DisplayName("[handleLogin] 유효하지 않은 Authorization Code 사용 시 예외 발생")
  void handleLogin_WithThrowException_WhenAuthCodeIsInvalid() {
    // given
    String invalidAuthCode = "";

    // when & then
    assertThrows(AuthHandler.class, () -> fakeOAuth2TokenService.handleLogin(invalidAuthCode));
  }

  @Test
  @DisplayName("[handleLogin] JWT가 정상적으로 생성되는지 확인")
  void handleLogin_WithGenerateJWTTokens() {
    // given
    String authorizationCode = "mock_auth_code";

    // when
    LoginResponse response = fakeOAuth2TokenService.handleLogin(authorizationCode);

    // then
    assertAll(
        () -> assertNotNull(response),
        () -> assertTrue(fakeJwtProvider.validateToken(response.getAccessToken())),
        () -> assertTrue(fakeJwtProvider.validateToken(response.getRefreshToken())));
  }

  @Test
  @DisplayName("[getUserInfo] JWT에서 올바른 Social ID를 추출하는지 확인")
  void getGoogleUserInfo_WithSocialIdFromJwt() {
    // given
    String authorizationCode = "mock_auth_code";

    // when
    LoginResponse response = fakeOAuth2TokenService.handleLogin(authorizationCode);

    // then
    String socialId = response.getSocialId();
    String newAccessToken = response.getAccessToken();

    OAuthAttributes attributes = fakeOAuth2TokenService.getUserInfo(newAccessToken);

    assertAll(() -> assertNotNull(attributes), () -> assertEquals(socialId, attributes.getSub()));
  }

  @Test
  @DisplayName("[getAccessToken] 유효하지 않은 Authorization Code 사용 시 예외 발생")
  void getAccessToken_WithThrowException_WhenAuthCodeIsInvalid() {
    // given
    String invalidAuthCode = "";

    // when
    // then
    assertThrows(AuthHandler.class, () -> fakeOAuth2TokenService.getAccessToken(invalidAuthCode));
  }

  @Test
  @DisplayName("[getAccessToken] 정상적인 Authorization Code 사용 시 GoogleTokenResponse 반환")
  void getAccessToken_ReturnValidGoogleTokenResponse() {
    // given
    String validAuthCode = "mock_auth_code";

    // when
    GoogleTokenResponse response = fakeOAuth2TokenService.getAccessToken(validAuthCode);

    // then
    assertNotNull(response);
    assertEquals("mock-access-token", response.getAccessToken());
    assertEquals(3600, response.getExpiresIn());
    assertEquals("Bearer", response.getTokenType());
  }

  @Test
  @DisplayName("[getUserInfo] 유효하지 않은 AccessToken 사용 시 예외 발생")
  void getUserInfo_WithThrowException_WhenAccessTokenIsInvalid() {
    // given
    String invalidAccessToken = "";

    // when
    // then
    assertThrows(AuthHandler.class, () -> fakeOAuth2TokenService.getUserInfo(invalidAccessToken));
  }

  @Test
  @DisplayName("[getUserInfo] 정상적인 AccessToken 사용 시 올바른 유저 정보 반환")
  void getUserInfo_WithReturnValidUserInfo_WhenAccessTokenIsValid() {
    // given
    String validAccessToken = "mock-access-token";

    // when
    OAuthAttributes attributes = fakeOAuth2TokenService.getUserInfo(validAccessToken);

    // then
    assertNotNull(attributes);
    assertEquals("test-social-id", attributes.getSub());
    assertEquals("test@example.com", attributes.getEmail());
  }
}
