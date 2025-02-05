package com.umc7th.a1grade.unittest.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.umc7th.a1grade.domain.auth.exception.AuthHandler;
import com.umc7th.a1grade.domain.auth.exception.status.AuthErrorStatus;
import com.umc7th.a1grade.domain.auth.service.TokenService;
import com.umc7th.a1grade.domain.auth.service.TokenServiceImpl;
import com.umc7th.a1grade.domain.jwt.JwtProvider;
import com.umc7th.a1grade.domain.user.entity.Role;
import com.umc7th.a1grade.domain.user.entity.User;
import com.umc7th.a1grade.domain.user.repository.UserRepository;
import com.umc7th.a1grade.unittest.auth.fake.FakeJwtProvider;
import com.umc7th.a1grade.unittest.user.fake.FakeUserRepository;

public class TokenServiceTest {
  private TokenService tokenService;
  private UserRepository fakeUserRepository;
  private JwtProvider jwtProvider;
  private String mockRefreshToken;
  private final String socialId = "user123";
  private User existingUser;

  @BeforeEach
  void init() {
    fakeUserRepository = new FakeUserRepository();
    jwtProvider = new FakeJwtProvider();
    this.tokenService =
        TokenServiceImpl.builder()
            .userRepository(fakeUserRepository)
            .jwtProvider(jwtProvider)
            .build();

    this.existingUser =
        User.builder().socialId(socialId).email("existing-test-email").role(Role.ROLE_USER).build();

    mockRefreshToken = jwtProvider.createRefreshToken(socialId);
    //    existingUser.setRefreshToken(mockRefreshToken);
    fakeUserRepository.save(existingUser);
  }

  //  @Test
  //  @DisplayName("[getSocialIdFronRefreshToken] - 정상 요청")
  //  void getSocialIdFromRefreshToken_Success() {
  //    // given
  //    // When
  //    Map<String, String> tokens = tokenService.getSocialIdFronRefreshToken(mockRefreshToken);
  //
  //    // Then
  //    assertThat(tokens).containsKeys("accessToken", "refreshToken");
  //    assertThat(tokens.get("accessToken")).isNotEmpty();
  //    assertThat(tokens.get("refreshToken")).isNotEmpty();
  //  }

  @Test
  @DisplayName("[getSocialIdFronRefreshToken] - socialId 가 null일 경우 예외 발생")
  void getSocialIdFromRefreshToken_NullToken() {
    // given
    // When
    // Then
    AuthHandler exception =
        assertThrows(AuthHandler.class, () -> tokenService.getSocialIdFronRefreshToken(null));

    assertThat(exception.getErrorReason().getMessage())
        .isEqualTo(AuthErrorStatus._REFRESH_TOKEN_REQUIRED.getMessage());
  }

  //  @Test
  //  @DisplayName("[getSocialIdFronRefreshToken] - 존재하지 않는 사용자의 리프레시 토큰을 사용하면 예외 발생 ")
  //  void getSocialIdFromRefreshToken_UserNotFound() {
  //    // Given
  //    String newUserRefreshToken = jwtProvider.createRefreshToken("nonexistentUser");
  //
  //    // When
  //    // Then
  //    AuthHandler exception =
  //        assertThrows(
  //            AuthHandler.class, () ->
  // tokenService.getSocialIdFronRefreshToken(newUserRefreshToken));
  //
  //    assertThat(exception.getErrorReason().getMessage())
  //        .isEqualTo(UserErrorStatus._USER_NOT_FOUND.getMessage());
  //  }
  //
  //  @Test
  //  @DisplayName("[validateRefreshToken] - 정상 요청")
  //  void validateRefreshToken_Success() {
  //    // Given
  //    // When
  //    // Then
  //    tokenService.validateRefreshToken(mockRefreshToken);
  //  }

  @Test
  @DisplayName("[findUserBySocialId] - 정상 요청")
  void findUserBySocialId_Success() {
    // When
    User user = tokenService.findUserBySocialId(socialId);

    // Then
    assertThat(user).isNotNull();
    assertThat(user.getSocialId()).isEqualTo(socialId);
  }

  //  @Test
  //  @DisplayName("[createNewTokens] - 정상 요청")
  //  void createNewTokens_Success() {
  //    // When
  //    Map<String, String> newTokens = tokenService.createNewTokens(socialId);
  //
  //    // Then
  //    assertThat(newTokens).containsKeys("accessToken", "refreshToken");
  //    assertThat(newTokens.get("accessToken")).isNotEmpty();
  //    assertThat(newTokens.get("refreshToken")).isNotEmpty();
  //  }

  //  @Test
  //  @DisplayName("[updateUserRefreshToken] - 정상 요청")
  //  void updateUserRefreshToken_Success() {
  //    // Given
  //    String newRefreshToken = jwtProvider.createRefreshToken(socialId);
  //
  //    // When
  //    tokenService.updateUserRefreshToken(existingUser, newRefreshToken);
  //
  //    // Then
  //    User updatedUser = fakeUserRepository.findBySocialId(socialId).orElseThrow();
  //    assertThat(updatedUser.getRefreshToken()).isEqualTo(newRefreshToken);
  //  }
}
