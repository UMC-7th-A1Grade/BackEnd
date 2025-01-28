package com.umc7th.a1grade.domain.user.repository;

import java.util.Optional;

import com.umc7th.a1grade.domain.user.entity.User;

public interface UserRepository {
  Optional<User> findByNickname(String nickname);

  Optional<User> findBySocialId(String socialId);

  User save(User user);
}
