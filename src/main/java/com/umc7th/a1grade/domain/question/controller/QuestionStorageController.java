package com.umc7th.a1grade.domain.question.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.umc7th.a1grade.domain.question.dto.QuestionStorageResponseDTO;
import com.umc7th.a1grade.domain.question.entity.QuestionType;
import com.umc7th.a1grade.domain.question.exception.status.QuestionStorageErrorStatus;
import com.umc7th.a1grade.domain.question.service.QuestionStorageService;
import com.umc7th.a1grade.global.annotation.ApiErrorCodeExample;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/storage")
public class QuestionStorageController {

  private final QuestionStorageService questionStorageService;

  @Operation(summary = "저장소 문제 조회하기", description = "사용자가 저장한 전체 문제, 틀린 문제, 유사 문제를 조회하는 API")
  @ApiErrorCodeExample(QuestionStorageErrorStatus.class)
  @GetMapping("/questions")
  public QuestionStorageResponseDTO.QuestionListDTO getStorageQuestionsByQuestionType(
      @Parameter(name = "userDetails", description = "인증된 사용자 정보", hidden = true)
          @AuthenticationPrincipal
          UserDetails userDetails,
      @Parameter(name = "type", description = "문제 유형")
          @RequestParam(value = "type", required = false)
          QuestionType questionType) {
    QuestionStorageResponseDTO.QuestionListDTO response;
    if (questionType == null) {
      response = questionStorageService.getStorageQuestions(userDetails);
    } else {
      response =
          questionStorageService.getStorageQuestionsByQuestionType(userDetails, questionType);
    }

    return response;
  }
}
