package com.umc7th.a1grade.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.umc7th.a1grade.domain.user.entity.mapping.UserCharacter;

public interface UserCharacterJpaRepository extends JpaRepository<UserCharacter, Long> {}
