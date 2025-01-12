package com.umc7th.a1grade.domain.question.dto;

import java.util.List;

import com.umc7th.a1grade.domain.question.entity.QuestionType;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserQuestionResponseDTO {

  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class QuestionDTO {
    @Schema(description = "문제Id", example = "1")
    Long questionId;

    @Schema(description = "문제번호", example = "1")
    int num;

    @Schema(description = "풀이", example = "1.함수 및 도함수를 확인하여")
    String content;

    @Schema(description = "이미지", example = "url")
    String questionImgURL;

    @Schema(description = "타입", example = "틀린문제")
    QuestionType type;

    @Schema(description = "정답", example = "25")
    String answer;
  }

  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class GetUserQuestionDTO {
    @Schema(description = "문제 목록", example = "")
    List<QuestionDTO> questions;
  }

  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ResUserQuestionMemoDTO {
    @Schema(description = "메모", example = "도함수 기울기를 사용하는 문제")
    String memo;
  }

  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class CreditConfirmDTO {
    @Schema(description = "크래딧 소모 여부 응답", example = "네")
    boolean confirm;
  }

  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class SaveUserQuestionDTO {
    @Schema(description = "문제 이미지", example = "url")
    String questionImgURL;

    @Schema(description = "메모", example = "도함수 기울기를 사용하는 문제")
    String memo;
  }
}
