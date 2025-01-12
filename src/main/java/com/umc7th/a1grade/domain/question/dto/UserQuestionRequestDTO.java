package com.umc7th.a1grade.domain.question.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserQuestionRequestDTO {

  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ReqUserQuestionMemoDTO {
    @Schema(description = "메모", example = "도함수 기울기를 사용하는 문제")
    String memo;
  }
}
