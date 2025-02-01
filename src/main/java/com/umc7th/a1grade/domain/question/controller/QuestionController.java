package com.umc7th.a1grade.domain.question.controller;

import jakarta.validation.Valid;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.umc7th.a1grade.domain.question.dto.QuestionRequestDTO;
import com.umc7th.a1grade.domain.question.dto.QuestionResponseDTO;
import com.umc7th.a1grade.domain.question.exception.status.QuestionErrorStatus;
import com.umc7th.a1grade.domain.question.service.QuestionService;
import com.umc7th.a1grade.global.annotation.ApiErrorCodeExample;
import com.umc7th.a1grade.global.apiPayload.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "question-controller", description = "문제 관리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/question")
public class QuestionController {
  private final QuestionService questionService;

  @GetMapping("/recent")
  @Operation(summary = "최근에 저장된 5문제 이미지 조회하기", description = "사용자가 최근에 저장한 5문제 이미지를 조회하는 API")
  @ApiResponses({
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "200",
        description = "OK, 성공",
        content = @Content(schema = @Schema(implementation = ApiResponse.class)))
  })
  @ApiErrorCodeExample(QuestionErrorStatus.class)
  public ApiResponse<QuestionResponseDTO.RandomQuestionDTO> getRandomQuestions(
      @Parameter(name = "userDetails", description = "인증된 사용자 정보", hidden = true)
          @AuthenticationPrincipal
          UserDetails userDetails) {
    QuestionResponseDTO.RandomQuestionDTO result = questionService.getRecentQuestions(userDetails);
    return ApiResponse.onSuccess(result);
  }

  @GetMapping("/random")
  @Operation(summary = "틀린 문제 중 랜덤 3문제 조회하기", description = "사용자가 틀린 3문제를 랜덤으로 조회하는 API")
  @ApiResponses({
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "200",
        description = "OK, 성공",
        content = @Content(schema = @Schema(implementation = ApiResponse.class)))
  })
  @ApiErrorCodeExample(QuestionErrorStatus.class)
  public ApiResponse<QuestionResponseDTO.RandomFalseQuestionDTO> getFalseRandomQuestions(
      @Parameter(name = "userDetails", description = "인증된 사용자 정보", hidden = true)
          @AuthenticationPrincipal
          UserDetails userDetails) {
    QuestionResponseDTO.RandomFalseQuestionDTO result =
        questionService.getRandomFalseQuestions(userDetails);
    return ApiResponse.onSuccess(result);
  }

  @PostMapping("{questionId}/submit/")
  @Operation(summary = "답안 입력하기 컨트롤러 구현", description = "사용자가 푼 문제를 제출하는 API")
  @ApiResponses({
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "200",
        description = "OK, 성공",
        content = @Content(schema = @Schema(implementation = ApiResponse.class)))
  })
  @ApiErrorCodeExample(QuestionErrorStatus.class)
  public ApiResponse<QuestionResponseDTO.SubmitAnswerDTO> submitAnswer(
      @Parameter(name = "questionId", description = "제출할 문제의 Id", example = "1", required = true)
          @PathVariable
          Long questionId,
      @Parameter(description = "사용자가 제출한 답안 정보", required = true) @Valid @RequestBody
          QuestionRequestDTO.submitAnswerDTO answer,
      @Parameter(name = "userDetails", description = "인증된 사용자 정보", hidden = true)
          @AuthenticationPrincipal
          UserDetails userDetails) {
    QuestionResponseDTO.SubmitAnswerDTO result =
        questionService.submitAnswer(questionId, answer, userDetails);
    return ApiResponse.onSuccess(result);
  }

  @GetMapping("/answer/{questionId}")
  @Operation(summary = "풀이 및 정답 확인하기 API", description = "풀이 및 정답 확인하기 API")
  @ApiResponses({
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "200",
        description = "OK, 성공",
        content = @Content(schema = @Schema(implementation = ApiResponse.class)))
  })
  @ApiErrorCodeExample(QuestionErrorStatus.class)
  public ApiResponse<QuestionResponseDTO.GetAnswerDTO> getAnswer(
      @Parameter(description = "질문 번호", example = "1") @PathVariable Long questionId) {
    QuestionResponseDTO.GetAnswerDTO answer = questionService.getAnswer(questionId);
    return ApiResponse.onSuccess(answer);
  }

    @GetMapping("/{userQuestionId}")
    @Operation(summary = "개별 문제 조회 API", description = "개별 문제 조회 API (사진, 풀이, 필기)")
    @ApiErrorCodeExample(QuestionErrorStatus.class)
    public ApiResponse<QuestionResponseDTO.GetQuestionDTO> getQuestion(
            @Parameter(description = "userQuestion Id 값", example = "1") @PathVariable
            Long userQuestionId,
            @Parameter(name = "userDetails", description = "인증된 사용자 정보", hidden = true)
            @AuthenticationPrincipal
            UserDetails userDetails) {
        QuestionResponseDTO.GetQuestionDTO answer =
                questionService.getQuestion(userQuestionId, userDetails);
        return ApiResponse.onSuccess(answer);
    }

    @PostMapping("/save")
    @Operation(summary = "문제 저장하기", description = "문제 이미지, 풀이, 정답 저장 API")
    @ApiErrorCodeExample(QuestionErrorStatus.class)
    public ApiResponse<QuestionResponseDTO.SaveUserQuestionDTO> saveUserQuestion(
            @RequestBody QuestionRequestDTO.RequestSaveQuestionDTO requestSaveQuestionDTO,
            @Parameter(name = "userDetails", description = "인증된 사용자 정보", hidden = true)
            @AuthenticationPrincipal UserDetails userDetails) {

        QuestionResponseDTO.SaveUserQuestionDTO response =
                questionService.saveQuestion(requestSaveQuestionDTO, userDetails);
        return ApiResponse.onSuccess(response);
    }
}
