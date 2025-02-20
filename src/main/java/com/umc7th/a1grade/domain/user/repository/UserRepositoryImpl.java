package com.umc7th.a1grade.domain.user.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.umc7th.a1grade.domain.user.entity.User;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
  private final UserJpaRepository userJpaRepository;

  @Override
  public Optional<User> findByNickname(String nickname) {
    return userJpaRepository.findByNickName(nickname);
  }

  @Override
  public Optional<User> findBySocialId(String socialId) {
    return userJpaRepository.findBySocialId(socialId);
  }

  @Override
  public User save(User user) {
    return userJpaRepository.save(user);
  }

  @Override
  public boolean existsByNickName(String nickname) {
    return userJpaRepository.existsByNickName(nickname);
  }

  @Override
  public Long countCorrectAnswerByUserId(Long userId) {
    return userJpaRepository.countCorrectAnswerByUserId(userId);
  }

  @Override
  public List<User> findUserWithCorrectAnswers() {
    return userJpaRepository.findUserWithCorrectAnswers();
  }

  @Override
  public List<User> findAll() {
    return userJpaRepository.findAll();
  }

  @Override
  public List<User> findUsersWhoSolvedQuestions(LocalDateTime start, LocalDateTime end) {
    return userJpaRepository.findUsersWhoSolvedQuestions(start, end);
  }
}
