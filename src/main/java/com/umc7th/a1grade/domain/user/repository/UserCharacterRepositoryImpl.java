package com.umc7th.a1grade.domain.user.repository;

import org.springframework.stereotype.Repository;

import com.umc7th.a1grade.domain.user.entity.mapping.UserCharacter;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserCharacterRepositoryImpl implements UserCharacterRepository {

  private final UserCharacterJpaRepository userCharacterJpaRepository;

  @Override
  public UserCharacter save(UserCharacter userCharacter) {
    userCharacterJpaRepository.save(userCharacter);
    return userCharacter;
  }
}
