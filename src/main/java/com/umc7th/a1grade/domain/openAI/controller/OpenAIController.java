package com.umc7th.a1grade.domain.openAI.controller;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.umc7th.a1grade.domain.openAI.dto.OpenAIResponse;
import com.umc7th.a1grade.domain.openAI.dto.OpenAIResponse.confirmQuestionResponse;
import com.umc7th.a1grade.domain.openAI.dto.OpenAIResponse.generateQuestionResponse;
import com.umc7th.a1grade.domain.openAI.service.OpenAIService;
import com.umc7th.a1grade.global.apiPayload.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api/open-ai")
@RestController
public class OpenAIController {
  private final OpenAIService openAIService;

  @Operation(summary = "수학 문제 판별", description = "문제 업로드 페이지에서 사진을 찍은 후 찍은 사진이 수학 문제 이미지인지 판별")
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "COMMON200",
          description = "OK, 응답에 성공했습니다."),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "AI4001",
          description = "NOT FOUND, 존재하지 않는 이미지입니다."),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "AI4002",
          description = "BAD REQUEST, 파일 크기는 5MB를 초과할 수 없습니다."),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "AI4003",
          description = "BAD REQUEST, 이미지 파일만 업로드 가능합니다."),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "AI5001",
          description = "INTERNAL SERVER ERROR, 이미지 처리 중 서버 에러, 관리자에게 문의 바랍니다.")
  })
  @PostMapping(value = "/confirm", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ApiResponse<confirmQuestionResponse> confirmQuestion(
      @Parameter(description = "확인할 문제 이미지") @RequestPart(value = "image", required = false)
          MultipartFile image) {

    OpenAIResponse.confirmQuestionResponse response = openAIService.confirmQuestion(image);
    return ApiResponse.onSuccess(response);
  }

  @Operation(summary = "유사 문제 생성", description = "유사 문제 생성하기에서 문제 이미지와 유사한 문제를 AI가 생성")
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "COMMON200",
          description = "OK, 응답에 성공했습니다."),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "AI4001",
          description = "NOT FOUND, 존재하지 않는 이미지입니다."),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "AI4002",
          description = "BAD REQUEST, 파일 크기는 5MB를 초과할 수 없습니다."),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "AI4003",
          description = "BAD REQUEST, 이미지 파일만 업로드 가능합니다."),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "AI5001",
          description = "INTERNAL SERVER ERROR, 이미지 처리 중 서버 에러, 관리자에게 문의 바랍니다.")
  })
  @PostMapping(value = "/generate", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ApiResponse<generateQuestionResponse> generateQuestion(
      @Parameter(description = "유사 문제를 생성할 이미지") @RequestPart(value = "image", required = false)
          MultipartFile image) {

    generateQuestionResponse response = openAIService.generateQuestion(image);
    return ApiResponse.onSuccess(response);
  }
}
