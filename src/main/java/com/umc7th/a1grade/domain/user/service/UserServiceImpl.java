package com.umc7th.a1grade.domain.user.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.umc7th.a1grade.domain.character.entity.Character;
import com.umc7th.a1grade.domain.character.repository.CharacterRepository;
import com.umc7th.a1grade.domain.user.dto.MypageResponseDto;
import com.umc7th.a1grade.domain.user.dto.UserInfoDto;
import com.umc7th.a1grade.domain.user.dto.UserInfoResponseDto;
import com.umc7th.a1grade.domain.user.entity.User;
import com.umc7th.a1grade.domain.user.entity.mapping.UserCharacter;
import com.umc7th.a1grade.domain.user.exception.UserHandler;
import com.umc7th.a1grade.domain.user.exception.status.UserErrorStatus;
import com.umc7th.a1grade.domain.user.repository.UserCharacterRepository;
import com.umc7th.a1grade.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final CharacterRepository characterRepository;
  private final UserCharacterRepository userCharacterRepository;

  @Override
  public void confirmNickName(UserDetails userDetails, String nickname) {
    if (nickname == null || nickname.isBlank()) {
      throw new UserHandler(UserErrorStatus._USER_NICKNAME_NULL);
    }
    boolean isDuplicate = userRepository.existsByNickName(nickname.trim());
    if (isDuplicate) {
      throw new UserHandler(UserErrorStatus._USER_NICKNAME_EXIST);
    }
  }

  @Override
  @Transactional
  public UserInfoResponseDto saveUserInfo(UserDetails userDetails, UserInfoDto requestDto) {
    User user = findUserBySocialId(userDetails.getUsername());
    Character character = findCharacterById(requestDto.getCharacterId());
    UserCharacter userCharacter = createUserCharacter(user, character);

    user.setNickName(requestDto.getNickname());
    User updatedUser = userRepository.save(user);
    return new UserInfoResponseDto(updatedUser, userCharacter.getCharacter().getId());
  }

  @Override
  public MypageResponseDto findUserGrade(UserDetails userDetails) {
    User user = findUserBySocialId(userDetails.getUsername());
    Long grade = userRepository.countCorrectAnswerByUserId(user.getId());
    return new MypageResponseDto(user.getNickName(), Math.toIntExact(grade));
  }

  private UserCharacter createUserCharacter(User user, Character character) {
    UserCharacter userCharacter =
        UserCharacter.builder().user(user).character(character).isActive(true).build();

    return userCharacterRepository.save(userCharacter);
  }

  private User findUserBySocialId(String socialId) {
    return userRepository
        .findBySocialId(socialId)
        .orElseThrow(() -> new UserHandler(UserErrorStatus._USER_NOT_FOUND));
  }

  private Character findCharacterById(Long characterId) {
    return characterRepository
        .findById(characterId)
        .orElseThrow(() -> new UserHandler(UserErrorStatus._USER_INFO_INVALID));
  }
}
