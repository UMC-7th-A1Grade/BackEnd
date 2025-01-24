package com.umc7th.a1grade.domain.user.repository;

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
  public Optional<User> findBySocailId(String socialId) {
    return userJpaRepository.findBySocialId(socialId);
  }

  @Override
  public User save(User user) {
    return userJpaRepository.save(user);
  }
}
