package com.umc7th.a1grade.domain.question.dto;

import jakarta.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

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
}
