package com.umc7th.a1grade.domain.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.umc7th.a1grade.domain.user.entity.User;

public interface UserJpaRepository extends JpaRepository<User, Long> {
  Optional<User> findByNickName(String nickname);

  Optional<User> findBySocialId(String socialId);
}
