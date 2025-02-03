package com.umc7th.a1grade.domain.user.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.umc7th.a1grade.domain.character.entity.Character;
import com.umc7th.a1grade.domain.character.repository.CharacterRepository;
import com.umc7th.a1grade.domain.ranking.entity.Ranking;
import com.umc7th.a1grade.domain.ranking.repository.RankingRepository;
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

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final CharacterRepository characterRepository;
  private final UserCharacterRepository userCharacterRepository;
  private final RankingRepository rankingRepository;

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
    List<Ranking> top3Users = rankingRepository.findAll();

    return top3Users.stream()
        .map(
            ranking ->
                AllGradeResponseDto.builder()
                    .userId(ranking.getUser().getId())
                    .nickName(ranking.getUser().getNickName())
                    .correctCount(ranking.getSolvedCount())
                    .answerRate(ranking.getAnswerRate())
                    .build())
        .collect(Collectors.toList());
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
