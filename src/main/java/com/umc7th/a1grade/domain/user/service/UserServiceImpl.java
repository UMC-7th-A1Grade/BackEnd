package com.umc7th.a1grade.domain.user.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc7th.a1grade.domain.character.entity.Character;
import com.umc7th.a1grade.domain.character.repository.CharacterRepository;
import com.umc7th.a1grade.domain.user.dto.AllGradeResponseDto;
import com.umc7th.a1grade.domain.user.dto.UserGradeResponseDto;
import com.umc7th.a1grade.domain.user.dto.UserInfoRequestDto;
import com.umc7th.a1grade.domain.user.dto.UserInfoResponseDto;
import com.umc7th.a1grade.domain.user.entity.User;
import com.umc7th.a1grade.domain.user.entity.mapping.UserCharacter;
import com.umc7th.a1grade.domain.user.exception.UserHandler;
import com.umc7th.a1grade.domain.user.exception.status.UserErrorStatus;
import com.umc7th.a1grade.domain.user.repository.UserCharacterRepository;
import com.umc7th.a1grade.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final CharacterRepository characterRepository;
  private final UserCharacterRepository userCharacterRepository;
  private final RedisTemplate<String, String> redisTemplate;
  private final ObjectMapper objectMapper = new ObjectMapper();

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
  public UserInfoResponseDto saveUserInfo(UserDetails userDetails, UserInfoRequestDto requestDto) {
    User user = findUserBySocialId(userDetails.getUsername());
    Character character = findCharacterById(requestDto.getCharacterId());
    UserCharacter userCharacter = createUserCharacter(user, character);

    user.setNickName(requestDto.getNickname());
    User updatedUser = userRepository.save(user);
    return new UserInfoResponseDto(updatedUser, userCharacter.getCharacter().getId());
  }

  @Override
  public UserGradeResponseDto findUserGrade(UserDetails userDetails) {
    User user = findUserBySocialId(userDetails.getUsername());
    Long grade = userRepository.countCorrectAnswerByUserId(user.getId());
    return new UserGradeResponseDto(user.getNickName(), Math.toIntExact(grade));
  }

  @Override
  public List<AllGradeResponseDto> findTop3UserGrade(UserDetails userDetails) {
    findUserBySocialId(userDetails.getUsername());
    List<AllGradeResponseDto> top3Users = new ArrayList<>();

    for (int i = 1; i <= 3; i++) {
      String rankKey = "RANK:" + i;
      String rankingJson = redisTemplate.opsForValue().get(rankKey);

      if (rankingJson != null) {
        try {
          AllGradeResponseDto dto = objectMapper.readValue(rankingJson, AllGradeResponseDto.class);
          top3Users.add(dto);
        } catch (JsonProcessingException e) {
          log.info("JSON 변환 오류: " + e.getMessage());
        }
      }
    }
    return top3Users;
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
