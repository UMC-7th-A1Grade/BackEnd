package com.umc7th.a1grade.domain.openAI.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.umc7th.a1grade.domain.openAI.dto.OpenAIResponse;
import com.umc7th.a1grade.domain.openAI.dto.OpenAIResponse.confirmQuestionResponse;
import com.umc7th.a1grade.domain.openAI.dto.OpenAIResponse.generateQuestionResponse;
import com.umc7th.a1grade.domain.openAI.exception.AIErrorStatus;
import com.umc7th.a1grade.domain.openAI.service.OpenAIService;
import com.umc7th.a1grade.global.annotation.ApiErrorCodeExample;
import com.umc7th.a1grade.global.apiPayload.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RequestMapping("/api/open-ai")
@RestController
public class OpenAIController {

  private final OpenAIService openAIService;

  @Operation(summary = "수학 문제 판별", description = "문제 업로드 페이지에서 사진을 찍은 후 찍은 사진이 수학 문제 이미지인지 판별")
  @ApiErrorCodeExample(AIErrorStatus.class)
  @PostMapping(value = "/confirm", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ApiResponse<confirmQuestionResponse> confirmQuestion(
      @Parameter(description = "확인할 문제 이미지") @RequestPart(value = "image", required = false)
      MultipartFile image) {

    OpenAIResponse.confirmQuestionResponse response = openAIService.confirmQuestion(image);
    return ApiResponse.onSuccess(response);
  }

  @Operation(summary = "유사 문제 생성", description = "유사 문제 생성하기에서 문제 이미지와 유사한 문제를 AI가 생성")
  @ApiErrorCodeExample(AIErrorStatus.class)
  @PostMapping(value = "/generate", produces = "application/json")
  public ApiResponse<generateQuestionResponse> generateQuestion(
      @Parameter(description = "유사 문제를 생성할 이미지 URL") @RequestParam String imageUrl) {

    generateQuestionResponse response = openAIService.generateQuestion(imageUrl);
    return ApiResponse.onSuccess(response);
  }
}
