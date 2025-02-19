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
  public static class UserQuestionDTO {
    @Schema(description = " 문제 id", example = "1")
    private Long userQuestionId;

    @Schema(description = " 문제 이미지", example = "https://questionImg.com")
    private String imageUrl;

    @Schema(
        description = " 문제 타입",
        example = "AI",
        allowableValues = {"AI", "USER"})
    private QuestionType type;
  }

  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
<<<<<<< HEAD
  @Schema(title = "Storage : 문제 리스트 Response DTO")
  public static class UserQuestionListDTO {
    @Schema(description = "문제 리스트")
    List<UserQuestionDTO> questions;

    @Schema(description = "조회한 리스트의 마지막 ID 값(커서)")
    Long cursor;
  }

  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @Schema(title = "Storage : 문제 아이디 리스트 DTO")
  public static class UserQuestionIdListDTO {
    @Schema(description = "문제 아이디 리스트")
    List<Long> userQuestionIds;
=======
  @Schema(title = "Storage : 문제 리스트 DTO")
  public static class QuestionListDTO {
    @Schema(description = " 문제 리스트")
    List<QuestionStorageResponseDTO.UserQuestionDTO> questions;
>>>>>>> d2bea99 (♻️ Refactor: 저장소 문제 조회 리턴값 수정)
  }
}
