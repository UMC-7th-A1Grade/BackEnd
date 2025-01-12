package com.umc7th.a1grade.domain.question.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class QuestionResponseDTO {

  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class GetSimilarQuestionDTO {
    @Schema(description = "유사문제 text", example = "유사문제 내용 도함수를 활용하여~")
    String question;

    @Schema(description = "유사문제 풀이", example = "1.함수 및 도함수 확인")
    String content;

    @Schema(description = "유사문제 정답", example = "25")
    String answer;
  }
}
