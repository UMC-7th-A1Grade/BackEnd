package com.umc7th.a1grade.domain.question.dto;

import java.util.List;

import com.umc7th.a1grade.domain.question.entity.QuestionType;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class QuestionStorageResponseDTO {

  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @Schema(title = "Storage : 문제 DTO")
  public static class QuestionDTO {
    @Schema(description = " 문제 id", example = "1")
    private Long id;

    @Schema(description = " 문제 풀이", example = "함수 및 도함수 어쩌구 저쩌구")
    private String memo;

    @Schema(description = " 문제 이미지", example = "https://questionImg.com")
    private String imageUrl;

    @Schema(
        description = " 문제 타입",
        example = "AI",
        allowableValues = {"AI", "USER"})
    private QuestionType type;

    @Schema(description = " 문제 정답", example = "3")
    private String answer;
  }

  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @Schema(title = "Storage : 문제 리스트 DTO")
  public static class QuestionListDTO {
    @Schema(description = " 문제 리스트")
    List<QuestionStorageResponseDTO.QuestionDTO> questions;
  }
}
