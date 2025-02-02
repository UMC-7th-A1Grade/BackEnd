package com.umc7th.a1grade.domain.openAI.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

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
  @Schema(title = "OpenAI 문제 판별 결과 응답 DTO")
  public static class confirmQuestionResponse {
    @Schema(description = "판별 결과", example = "true")
    Boolean result;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "판별된 이미지 url")
    String imageUrl;
  }

  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @Schema(title = "OpenAI 문제 생성 결과 응답 DTO")
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
