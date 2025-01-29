package com.umc7th.a1grade.unittest.user.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

import com.umc7th.a1grade.domain.character.repository.CharacterRepository;
import com.umc7th.a1grade.domain.user.entity.Role;
import com.umc7th.a1grade.domain.user.entity.User;
import com.umc7th.a1grade.domain.user.exception.UserHandler;
import com.umc7th.a1grade.domain.user.repository.UserCharacterRepository;
import com.umc7th.a1grade.domain.user.repository.UserRepository;
import com.umc7th.a1grade.domain.user.service.UserService;
import com.umc7th.a1grade.domain.user.service.UserServiceImpl;
import com.umc7th.a1grade.unittest.user.fake.FakeCharacterRepository;
import com.umc7th.a1grade.unittest.user.fake.FakeUserCharacterRepository;
import com.umc7th.a1grade.unittest.user.fake.FakeUserDetails;
import com.umc7th.a1grade.unittest.user.fake.FakeUserRepository;

public class UserServiceTest {
  private UserService userService;
  private UserRepository fakeUserRepository;
  private CharacterRepository fakeCharacterRepository;
  private UserCharacterRepository fakeUserCharacterRepository;
  private final String socialId = "user1234";
  private User existingUser;

  @BeforeEach
  void init() {
    fakeUserRepository = new FakeUserRepository();
    fakeCharacterRepository = new FakeCharacterRepository();
    fakeUserCharacterRepository = new FakeUserCharacterRepository();
    userService =
        new UserServiceImpl(
            fakeUserRepository, fakeCharacterRepository, fakeUserCharacterRepository);

    this.existingUser =
        User.builder()
            .socialId(socialId)
            .email("existing-test-email")
            .role(Role.ROLE_USER)
            .nickName("existing-nickname")
            .build();

    fakeUserRepository.save(existingUser);
  }

  @Test
  @DisplayName("[confirmNickName] - 정상 요청")
  void confirmNickName_Success() {
    /// given
    String nickName = "new-nickname";
    String socialId = "new-socialID";
    UserDetails userDetails = new FakeUserDetails(socialId);

    // when
    // then
    assertDoesNotThrow(() -> userService.confirmNickName(userDetails, nickName));
  }

  @Test
  @DisplayName("[confirmNickName] - 닉네임을 입력하지 않거나, 공백 입력시 예외 발생")
  void confirmNickName_invalidInput() {
    // given
    String nickName = "   ";
    String socialId = "new-socialId";
    UserDetails userDetails = new FakeUserDetails(socialId);

    // when
    // then
    assertThrows(UserHandler.class, () -> userService.confirmNickName(userDetails, nickName));
  }

  @Test
  @DisplayName("[confirmNickName] - 이미 사용중인 닉네임이라면 예외 발생")
  void confirmNickName_usedInput() {
    // given
    String nickName = "existing-nickname";
    String socialId = "new-socialId";
    UserDetails userDetails = new FakeUserDetails(socialId);

    // when
    // then
    assertThrows(UserHandler.class, () -> userService.confirmNickName(userDetails, nickName));
  }
}
