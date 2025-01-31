package com.umc7th.a1grade.domain.question.dto;

import java.util.List;

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
    @Schema(description = "랜덤 5문제 문제 이미지 리스트")
    List<QuestionResponseDTO.QuestionDTO> questions;
  }

  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class RandomFalseQuestionDTO {
    @Schema(description = "틀린 문제 중 랜덤 3문제 질문 리스트")
    List<QuestionResponseDTO.FalseQuestionDTO> questions;
  }

  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class SubmitAnswerDTO {
    @Schema(description = " 문제 정답", example = "3")
    String answer;

    @Schema(description = " 문제 정답 여부", example = "true")
    boolean isCorrect;
  }

  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class GetAnswerDTO {
    @Schema(description = " 문제 정답", example = "3")
    String answer;

    @Schema(description = "문제 풀이", example = " 함수 및 도함수 어쩌구 저쩌구")
    String memo;
  }

  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class GetQuestionDTO {
    @Schema(description = " 문제 이미지", example = "https://questionImg.com")
    String questionImg;

    @Schema(description = " 문제 정답", example = "3")
    String answer;

    @Schema(description = "문제 풀이", example = " 함수 및 도함수 어쩌구 저쩌구")
    String memo;

    @Schema(description = "사용자가 작성한 필기", example = "https://note.com")
    List<String> note;
  }

  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class QuestionDTO {
    @Schema(description = " 문제 id", example = "1")
    Long id;

    @Schema(description = " 문제 이미지", example = "https://questionImg.com")
    String questionImg;
  }

  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class FalseQuestionDTO {
    @Schema(description = " 문제 id", example = "1")
    Long id;

    @Schema(description = " 문제 이미지", example = "https://questionImg.com")
    String questionImg;

    @Schema(description = "제출 여부", example = "false")
    boolean isSubmitted;
  }
}
