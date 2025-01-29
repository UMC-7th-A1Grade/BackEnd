package com.umc7th.a1grade.domain.character.repository;

import java.util.Optional;

import com.umc7th.a1grade.domain.character.entity.Character;

public interface CharacterRepository {
  Optional<Character> findById(Long characterId);
}
