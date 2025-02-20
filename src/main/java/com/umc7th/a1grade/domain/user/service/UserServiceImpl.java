package com.umc7th.a1grade.domain.user.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc7th.a1grade.domain.character.entity.Character;
import com.umc7th.a1grade.domain.character.repository.CharacterRepository;
import com.umc7th.a1grade.domain.user.dto.*;
import com.umc7th.a1grade.domain.user.entity.User;
import com.umc7th.a1grade.domain.user.entity.mapping.UserCharacter;
import com.umc7th.a1grade.domain.user.exception.UserHandler;
import com.umc7th.a1grade.domain.user.exception.status.UserErrorStatus;
import com.umc7th.a1grade.domain.user.repository.UserCharacterRepository;
import com.umc7th.a1grade.domain.user.repository.UserRepository;
import com.umc7th.a1grade.global.exception.GeneralException;

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

    // 평가용 테스트 계정 용도(실제 서비스하는 경우 삭제)
    if (user.getSocialId().equals("test")) {
      return new UserInfoResponseDto(user, 7L); // 테스트 계정은 7번 캐릭터로 고정
    }

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
      try {
        String rankingJson = redisTemplate.opsForValue().get(rankKey);
        if (!StringUtils.hasText(rankingJson)) {
          log.info("순위{}에 해당하는 데이터 없음", i);
          continue;
        }
        top3Users.add(objectMapper.readValue(rankingJson, AllGradeResponseDto.class));
      } catch (JsonProcessingException e) {
        log.error("순위 {} 데이터 JSON 변환 실패: {}", i, e.getMessage());
        throw new UserHandler(UserErrorStatus._USER_JSON_PARSE_FAIL);
      } catch (RedisConnectionFailureException e) {
        log.error("Redis 서버 연결 실패: {}", e.getMessage());
        throw new UserHandler(UserErrorStatus._USER_REDIS_CONNECTION_FAIL);
      } catch (DataAccessException e) {
        log.error("Redis 데이터 접근 오류 발생: {}", e.getMessage());
        throw new UserHandler(UserErrorStatus._USER_REDIS_ACCESS_ERROR);
      }
    }
    if (top3Users.isEmpty()) {
      log.warn("상위 3명에 대한 데이터가 없습니다.");
      throw new UserHandler(UserErrorStatus._USER_TOP3_NOT_FOUND);
    }
    return top3Users;
  }

  @Override
  public UserCreditResponseDto findUserCredit(UserDetails userDetails) {
    User user = findUserBySocialId(userDetails.getUsername());
    return new UserCreditResponseDto(user.getCredit());
  }

  @Override
  @Transactional
  public UserCreditResponseDto updateUserCredit(UserDetails userDetails) {
    User user = findUserBySocialId(userDetails.getUsername());
    if (user.getCredit() > 0) {
      user.setCredit(user.getCredit() - 1);
    } else {
      throw new GeneralException(UserErrorStatus._USER_CREDIT_ZERO);
    }
    userRepository.save(user);
    return new UserCreditResponseDto(user.getCredit());
  }

  @Override
  public void logout(UserDetails userDetails) {
    User user = findUserBySocialId(userDetails.getUsername());
  }

  @Override
  public UserNicknameResponseDto getUserNickName(UserDetails userDetails) {
    User user = findUserBySocialId(userDetails.getUsername());

    // 평가용 테스트 계정 용도(실제 서비스하는 경우 삭제)
    if (user.getSocialId().equals("test")) {
      return new UserNicknameResponseDto(null);
    }

    return new UserNicknameResponseDto(user.getNickName());
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
