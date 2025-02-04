package com.umc7th.a1grade.unittest.user.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.umc7th.a1grade.unittest.user.fake.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;

import com.umc7th.a1grade.domain.character.repository.CharacterRepository;
import com.umc7th.a1grade.domain.user.dto.UserInfoRequestDto;
import com.umc7th.a1grade.domain.user.dto.UserInfoResponseDto;
import com.umc7th.a1grade.domain.user.entity.Role;
import com.umc7th.a1grade.domain.user.entity.User;
import com.umc7th.a1grade.domain.user.exception.UserHandler;
import com.umc7th.a1grade.domain.user.repository.UserCharacterRepository;
import com.umc7th.a1grade.domain.user.repository.UserRepository;
import com.umc7th.a1grade.domain.user.service.UserService;
import com.umc7th.a1grade.domain.user.service.UserServiceImpl;

public class UserServiceTest {
  private UserService userService;
  private UserRepository fakeUserRepository;
  private CharacterRepository fakeCharacterRepository;
  private UserCharacterRepository fakeUserCharacterRepository;
  private RedisTemplate fakeRedisTemplate;
  private final String socialId1 = "user1234";
  private final String socialId2 = "user2222";
  private User existingUser;
  private User existingUser2;

  @BeforeEach
  void init() {
    fakeUserRepository = new FakeUserRepository();
    fakeCharacterRepository = new FakeCharacterRepository();
    fakeUserCharacterRepository = new FakeUserCharacterRepository();
    fakeRedisTemplate = new FakeRedisTemplate();
    userService =
        new UserServiceImpl(
            fakeUserRepository,
            fakeCharacterRepository,
            fakeUserCharacterRepository,
                fakeRedisTemplate
        );

    this.existingUser =
        User.builder()
            .socialId(socialId1)
            .email("existing-test-email")
            .role(Role.ROLE_USER)
            .nickName("existing-nickname")
            .build();

    this.existingUser2 =
        User.builder()
            .socialId(socialId2)
            .email("existing-test-email2")
            .role(Role.ROLE_USER)
            .build();

    fakeUserRepository.save(existingUser);
    fakeUserRepository.save(existingUser2);
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

  @Test
  @DisplayName("[confirmNickName] - 닉네임 입력 시 앞뒤 공백이 제거된 후 중복 여부를 검사해야 함")
  void confirmNickName_WithTrim_Success() {
    /// given
    String nickName = "existing-nickname                     ";
    String socialId = "new-socialID";
    UserDetails userDetails = new FakeUserDetails(socialId);

    // when
    // then
    assertThrows(UserHandler.class, () -> userService.confirmNickName(userDetails, nickName));
  }

  @Test
  @DisplayName("[saveUserInfo] - 닉네임 및 선택한 캐릭터 정보 저장 성공")
  void saveUserInfo_Success() {
    // given
    UserDetails userDetails = new FakeUserDetails(existingUser2.getSocialId());
    UserInfoRequestDto requestDto =
        UserInfoRequestDto.builder().nickname("test-nickname").characterId(1L).build();
    // when
    UserInfoResponseDto response = userService.saveUserInfo(userDetails, requestDto);

    // then
    assertAll(
        () -> assertNotNull(response),
        () -> assertEquals(response.getId(), 2L),
        () -> assertEquals(response.getSocialId(), existingUser2.getSocialId()),
        () -> assertEquals(response.getEmail(), existingUser2.getEmail()),
        () -> assertEquals(response.getNickName(), requestDto.getNickname()),
        () -> assertEquals(response.getSelectedCharacter(), requestDto.getCharacterId()));
  }

  @Test
  @DisplayName("[saveUserInfo] - 닉네임 및 선택한 캐릭터 정보 저장 실패 ( 존재하지 않은 사용자일 경우 예외 발생 ) ")
  void findUserBySocialId_Success() {
    // given
    String invalidSocialId = "invalid-social-id";
    UserDetails userDetails = new FakeUserDetails(invalidSocialId);
    UserInfoRequestDto requestDto =
        UserInfoRequestDto.builder().nickname("test-nickname").characterId(1L).build();
    // when
    // then
    assertThrows(UserHandler.class, () -> userService.saveUserInfo(userDetails, requestDto));
  }

  @Test
  @DisplayName("[saveUserInfo] - 닉네임 및 선택한 캐릭터 정보 저장 실패 ( 존재하지 않는 캐릭터 조회 시 예외 발생 )")
  void findCharacterById_NotFound() {
    // given
    Long invalidCharacterId = 222L;
    UserDetails userDetails = new FakeUserDetails(socialId2);
    UserInfoRequestDto requestDto =
        UserInfoRequestDto.builder()
            .nickname("test-nickname")
            .characterId(invalidCharacterId)
            .build();
    // when
    // then
    assertThrows(UserHandler.class, () -> userService.saveUserInfo(userDetails, requestDto));
  }
}
