package com.umc7th.a1grade.domain.character.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.umc7th.a1grade.domain.character.dto.CharacterListResponseDto;
import com.umc7th.a1grade.domain.character.dto.CharacterResponseDto;
import com.umc7th.a1grade.domain.character.entity.Character;
import com.umc7th.a1grade.domain.character.repository.CharacterRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CharacterServiceImpl implements CharacterService {
  private final CharacterRepository characterRepository;

  @Override
  public CharacterListResponseDto findAll() {
    List<Character> characters = characterRepository.findAll();

    List<CharacterResponseDto> characterDtos =
        characters.stream().map(CharacterResponseDto::new).collect(Collectors.toList());

    return new CharacterListResponseDto(characterDtos);
  }
}
