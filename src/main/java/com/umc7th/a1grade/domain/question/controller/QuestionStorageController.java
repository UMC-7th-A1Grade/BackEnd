package com.umc7th.a1grade.domain.question.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.umc7th.a1grade.domain.question.dto.QuestionStorageResponseDTO.UserQuestionIdListDTO;
import com.umc7th.a1grade.domain.question.dto.QuestionStorageResponseDTO.UserQuestionListDTO;
import com.umc7th.a1grade.domain.question.entity.QuestionType;
import com.umc7th.a1grade.domain.question.exception.status.QuestionStorageErrorStatus;
import com.umc7th.a1grade.domain.question.exception.status.QuestionStorageSuccessStatus;
import com.umc7th.a1grade.domain.question.service.QuestionStorageService;
import com.umc7th.a1grade.global.annotation.ApiErrorCodeExample;
import com.umc7th.a1grade.global.apiPayload.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "question-storage-controller", description = "저장소 문제 관리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/storage")
public class QuestionStorageController {

  private final QuestionStorageService questionStorageService;

  @Operation(
      summary = "저장소 문제 조회하기",
      description = "사용자가 저장한 전체 문제, 틀린 문제, 유사 문제를 조회하는 API. 최신 저장된 순서로 정렬되므로 작은 숫자의 ID 값을 커서로 사용.")
  @ApiErrorCodeExample(QuestionStorageErrorStatus.class)
  @GetMapping("/questions")
  public ApiResponse<UserQuestionListDTO> getStorageQuestionsByQuestionType(
      @Parameter(name = "userDetails", description = "인증된 사용자 정보", hidden = true)
          @AuthenticationPrincipal
          UserDetails userDetails,
      @Parameter(name = "type", description = "문제 유형")
          @RequestParam(value = "type", required = false)
          QuestionType questionType,
      @Parameter(name = "cursor", description = "페이징 커서")
          @RequestParam(value = "cursor", defaultValue = Long.MAX_VALUE + "")
          Long cursor,
      @Parameter(name = "isOverLimit", description = "장기기억저장소 조회 = true")
          @RequestParam(value = "isOverLimit", defaultValue = "false")
          boolean isOverLimit) {

    UserQuestionListDTO response =
        questionStorageService.getStorageQuestionsByQuestionType(
            userDetails, questionType, cursor, isOverLimit);

    QuestionStorageSuccessStatus status =
        response.getQuestions().isEmpty()
            ? QuestionStorageSuccessStatus._NULL_QUESTION_SUCCESS
            : QuestionStorageSuccessStatus._QUESTION_STORAGE_SUCCESS;

    return ApiResponse.of(status, response);
  }

  @Operation(summary = "저장소 문제 삭제하기", description = "선택한 문제의 아이디를 받아 저장소 문제를 삭제하는 API")
  @ApiErrorCodeExample(QuestionStorageErrorStatus.class)
  @PostMapping("/questions/delete")
  public ApiResponse<Boolean> deleteStorageQuestions(
      @Parameter(name = "userDetails", description = "인증된 사용자 정보", hidden = true)
          @AuthenticationPrincipal
          UserDetails userDetails,
      @Parameter(name = "userQuestionIds", description = "삭제할 문제 아이디") @RequestBody
          UserQuestionIdListDTO userQuestionIdList) {
    Boolean response =
        questionStorageService.deleteStorageQuestions(userDetails, userQuestionIdList);

    return ApiResponse.onSuccess(response);
  }
}
