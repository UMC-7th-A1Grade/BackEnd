package com.umc7th.a1grade.domain.character.dto;

import com.umc7th.a1grade.domain.character.entity.Character;

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
@Schema(description = "캐릭터 정보 조회 응답 DTO")
public class CharacterResponseDto {
  @Schema(description = "캐릭터 ID", example = "1")
  private Long id;

  @Schema(description = "캐릭터 이름", example = "일급등")
  private String name;

  @Schema(description = "캐릭터 이미지 URL", example = "https://example.com/character1.png")
  private String imageUrl;

  public CharacterResponseDto(Character character) {
    this.id = character.getId();
    this.name = character.getName();
    this.imageUrl = character.getImageUrl();
  }
}
