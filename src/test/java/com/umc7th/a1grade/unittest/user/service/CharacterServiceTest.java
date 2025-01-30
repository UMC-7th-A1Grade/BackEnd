package com.umc7th.a1grade.unittest.user.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.umc7th.a1grade.domain.character.dto.CharacterListResponseDto;
import com.umc7th.a1grade.domain.character.entity.Character;
import com.umc7th.a1grade.domain.character.repository.CharacterRepository;
import com.umc7th.a1grade.domain.character.service.CharacterService;
import com.umc7th.a1grade.domain.character.service.CharacterServiceImpl;
import com.umc7th.a1grade.unittest.user.fake.FakeCharacterRepository;

public class CharacterServiceTest {
  private CharacterService characterService;
  private CharacterRepository fakeCharacterRepository;

  @BeforeEach
  void init() {
    fakeCharacterRepository = new FakeCharacterRepository();
    characterService = new CharacterServiceImpl(fakeCharacterRepository);
  }

  @Test
  @DisplayName("[findAll] - 기본 저장된 캐릭터 리스트 조회 성공")
  void findAll_Success() {
    // given
    // when
    CharacterListResponseDto responseDto = characterService.findAll();

    // then
    assertAll(
        () -> assertNotNull(responseDto),
        () -> assertEquals(3, responseDto.getTotalCount()),
        () -> assertEquals("캐릭터1", responseDto.getCharacters().get(0).getName()),
        () ->
            assertEquals(
                "https://example.com/character1.png",
                responseDto.getCharacters().get(0).getImageUrl()));
  }

  @Test
  @DisplayName("[findById] - 특정 Id로 캐릭터 조회 성공")
  void findById_Success() {
    // given
    Optional<Character> characterOpt = fakeCharacterRepository.findById(1L);
    Character character = characterOpt.get();

    // when
    // then
    assertAll(
        () -> assertTrue(characterOpt.isPresent()),
        () -> assertEquals(1L, character.getId()),
        () -> assertEquals("캐릭터1", character.getName()),
        () -> assertEquals("https://example.com/character1.png", character.getImageUrl()));
  }

  @Test
  @DisplayName("[findById] - 존재하지 않는 Id로 조회 시 빈 값 반환")
  void findById_NotFound() {
    // given
    // when
    Optional<Character> characterOpt = fakeCharacterRepository.findById(999L);

    // then
    assertFalse(characterOpt.isPresent());
  }
}
