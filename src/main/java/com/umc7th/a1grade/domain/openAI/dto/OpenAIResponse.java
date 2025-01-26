package com.umc7th.a1grade.domain.openAI.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class OpenAIResponse {
  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class confirmQuestionResponse {
    @Schema(description = "판별 결과", example = "true")
    Boolean success;
  }

  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class generateQuestionResponse {
    @Schema(description = "생성 결과", example = "Successfully generated a similar problem.")
    String message;

    @Schema(description = "생성된 유사 문제")
    String question;

    @Schema(description = "생성된 유사 문제 풀이")
    String memo;

    @Schema(description = "생성된 유사 문제 정답", example = "2025")
    String answer;
  }
}
