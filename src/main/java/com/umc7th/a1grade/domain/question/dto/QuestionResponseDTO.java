package com.umc7th.a1grade.domain.question.dto;

import java.util.List;

import com.umc7th.a1grade.domain.question.entity.Question;

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
  public static class RandomQuestionDTO {
    @Schema(description = "랜덤 3문제 질문 리스트", example = "")
    List<Question> questions;
  }

  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class SubmitAnswerDTO {
    @Schema(description=" 문제 정답", example= "3")
    String answer;

    @Schema(description=" 문제 정답 여부", example= "true")
    boolean isCorrect;
  }

  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class getAnswerDTO {
    @Schema(description=" 문제 정답", example= "3")
    String answer;
  }
}
