package com.umc7th.a1grade.domain.character.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@Schema(description = "캐릭터 목록 응답 DTO")
public class CharacterListResponseDto {
  @Schema(description = "전체 캐릭터 개수", example = "10")
  private int totalCount;

  @Schema(description = "캐릭터 목록")
  private List<CharacterResponseDto> characters;

  public CharacterListResponseDto(List<CharacterResponseDto> list) {
    this.totalCount = list.size();
    this.characters = list;
  }
}
