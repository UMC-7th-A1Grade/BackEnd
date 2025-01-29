package com.umc7th.a1grade.unittest.user.fake;

import java.util.Optional;

import com.umc7th.a1grade.domain.character.entity.Character;
import com.umc7th.a1grade.domain.character.repository.CharacterRepository;

public class FakeCharacterRepository implements CharacterRepository {

  @Override
  public Optional<Character> findById(Long characterId) {
    return Optional.empty();
  }
}
