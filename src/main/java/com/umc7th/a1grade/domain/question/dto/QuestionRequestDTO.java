package com.umc7th.a1grade.domain.question.dto;

import jakarta.validation.constraints.NotNull;

import com.umc7th.a1grade.domain.question.entity.QuestionType;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class QuestionRequestDTO {
  @Getter
  public static class submitAnswerDTO {
    @Schema(description = "사용자가 제출한 답안 메모", example = "https://sdjflksjefl")
    @NotNull
    String note;

    @Schema(description = "사용자가 제출한 답안", example = "42")
    @NotNull
    String answer;
  }

  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @Schema(title = "Question : 문제 저장을 위한 요청 DTO")
  public static class RequestSaveQuestionDTO {

    @Schema(description = "문제 풀이 이미지", example = "https://memoImg.com")
    private String memoImageUrl;

    @Schema(description = " 문제 이미지", example = "https://questionImg.com")
    private String imageUrl;

    @Schema(
        description = "문제 타입",
        example = "USER",
        allowableValues = {"AI", "USER"})
    private QuestionType type;

    @Schema(description = "사용자가 제출한 답안", example = "42")
    private String answer;
  }
}
