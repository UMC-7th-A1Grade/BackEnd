package com.umc7th.a1grade.domain.character.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.umc7th.a1grade.domain.character.entity.Character;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CharacterRepositoryImpl implements CharacterRepository {
  private final CharacterJpaRepository characterJpaRepository;

  @Override
  public Optional<Character> findById(Long characterId) {
    return characterJpaRepository.findById(characterId);
  }

  @Override
  public List<Character> findAll() {
    return characterJpaRepository.findAll();
  }
}
