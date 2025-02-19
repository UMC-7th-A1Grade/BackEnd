package com.umc7th.a1grade.domain.user.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.umc7th.a1grade.domain.user.entity.User;

public interface UserRepository {
  Optional<User> findByNickname(String nickname);

  Optional<User> findBySocialId(String socialId);

  User save(User user);

  boolean existsByNickName(String nickname);

  Long countCorrectAnswerByUserId(Long userId);

  List<User> findUserWithCorrectAnswers();

  List<User> findAll();

  List<User> findUsersWhoSolvedQuestions(LocalDateTime start, LocalDateTime end);
}
