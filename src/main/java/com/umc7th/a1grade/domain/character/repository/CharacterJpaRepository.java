package com.umc7th.a1grade.domain.character.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.umc7th.a1grade.domain.character.entity.Character;

public interface CharacterJpaRepository extends JpaRepository<Character, Long> {
  Optional<Character> findById(Long characterId);
}
