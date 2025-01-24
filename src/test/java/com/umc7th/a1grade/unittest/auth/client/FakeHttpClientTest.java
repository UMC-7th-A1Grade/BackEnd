package com.umc7th.a1grade.unittest.auth.client;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.umc7th.a1grade.unittest.auth.fake.FakeHttpClientImpl;

public class FakeHttpClientTest {
  private FakeHttpClientImpl fakeHttpClient;

  @BeforeEach
  void init() {
    fakeHttpClient = new FakeHttpClientImpl();
  }

  @Test
  @DisplayName("[post] 구글 엑세스 토큰 요청 - 예상 응답 확인")
  void postReturnMockAccessToken_WhenValidTokenUrlProvided() {
    // given
    String tokenUrl = "http://localhost:8888/oauth2/token";

    // when
    String response = fakeHttpClient.post(tokenUrl);

    // then
    assertNotNull(response);
    assertTrue(response.contains("\"access_token\": \"mock-access-token\""));
  }

  @Test
  @DisplayName("[post] 잘못된 URL 요청 시 예외 발생")
  void postWithThrowException_WhenInvalidUrlProvided() {
    // given
    String invalidUrl = "http://localhost:8888/invalid-url";

    // when
    IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, () -> fakeHttpClient.post(invalidUrl));

    // then
    assertEquals("Invalid URL for POST request: " + invalidUrl, exception.getMessage());
  }

  @Test
  @DisplayName("[get] 구글 유저 정보 요청 - 예상된 응답 확인")
  void getReturnMockUserInfo_WhenValidUserInfoUrlProvided() {
    // given
    String userInfoUrl = "http://localhost:8888/oauth2/userinfo";

    // when
    String response = fakeHttpClient.get(userInfoUrl);

    // then
    assertNotNull(response);
    assertTrue(response.contains("\"sub\": \"test-social-id\""));
    assertTrue(response.contains("\"email\": \"test@example.com\""));
  }

  @Test
  @DisplayName("[get] 잘못된 URL 요청 시 예외 발생")
  void getWithThrowException_WhenInvalidUrlProvided() {
    // given
    String invalidUrl = "http://localhost:8888/invalid-userinfo";

    // when
    IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, () -> fakeHttpClient.get(invalidUrl));

    // then
    assertEquals("Invalid URL for GET request: " + invalidUrl, exception.getMessage());
  }
}
