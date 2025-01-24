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
import com.umc7th.a1grade.domain.question.service.QuestionService;
import com.umc7th.a1grade.global.apiPayload.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/questions")
public class QuestionController {
  private final QuestionService questionService;

  @GetMapping("/random")
  @Operation(summary = "랜덤 3문제 조회하기", description = "사용자가 저장한 3문제를 랜덤으로 조회하는 API")
  @ApiResponses({
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "200",
        description = "OK, 성공",
        content =
            @Content(
                schema = @Schema(implementation = ApiResponse.class),
                examples = {
                  @ExampleObject(
                      name = "COMMON200",
                      value =
                          "{\"isSuccess\":true,\"code\":\"COMMON200\",\"message\":\"성공\",\"data\":{\"questions\":[{\"id\":1,\"title\":\"문제1\",\"content\":\"내용1\"},{\"id\":2,\"title\":\"문제2\",\"content\":\"내용2\"},{\"id\":3,\"title\":\"문제3\",\"content\":\"내용3\"}]}}")
                }))
  })
  public ApiResponse<QuestionResponseDTO.RandomQuestionDTO> getRandomQuestions(
      @Parameter(name = "userDetails", description = "인증된 사용자 정보", hidden = true)
          @AuthenticationPrincipal
          UserDetails userDetails) {
    QuestionResponseDTO.RandomQuestionDTO result = questionService.getRandomQuestions(userDetails);
    return ApiResponse.onSuccess(result);
  }

  @GetMapping("/random/false")
  @Operation(summary = "틀린 문제 중 랜덤 3문제 조회하기", description = "사용자가 틀린 3문제를 랜덤으로 조회하는 API")
  @ApiResponses({
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "200",
        description = "OK, 성공",
        content =
            @Content(
                schema = @Schema(implementation = ApiResponse.class),
                examples = {
                  @ExampleObject(
                      name = "COMMON200",
                      value =
                          "{\"isSuccess\":true,\"code\":\"COMMON200\",\"message\":\"성공\",\"data\":{\"questions\":[{\"id\":1,\"title\":\"문제1\",\"content\":\"내용1\"},{\"id\":2,\"title\":\"문제2\",\"content\":\"내용2\"},{\"id\":3,\"title\":\"문제3\",\"content\":\"내용3\"}]}}")
                }))
  })
  public ApiResponse<QuestionResponseDTO.RandomFalseQuestionDTO> getFalseRandomQuestions(
      @Parameter(name = "userDetails", description = "인증된 사용자 정보", hidden = true)
          @AuthenticationPrincipal
          UserDetails userDetails) {
    QuestionResponseDTO.RandomFalseQuestionDTO result =
        questionService.getRandomFalseQuestions(userDetails);
    return ApiResponse.onSuccess(result);
  }

  @PostMapping("{questionNum}/submit/")
  @Operation(summary = "답안 입력하기 컨트롤러 구현", description = "사용자가 푼 문제를 제출하는 API")
  @ApiResponses({
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "200",
        description = "OK, 성공",
        content =
            @Content(
                schema = @Schema(implementation = ApiResponse.class),
                examples = {
                  @ExampleObject(
                      name = "COMMON200",
                      value =
                          "{\"isSuccess\":true,\"code\":\"COMMON200\",\"message\":\"정답 제출 완료\",\"data\":{\"isCorrect\":false}}")
                }))
  })
  public ApiResponse<QuestionResponseDTO.SubmitAnswerDTO> submitAnswer(
      @Parameter(
              name = "questionNum",
              description = "제출할 문제의 Num",
              example = "123",
              required = true)
          @PathVariable
          int questionNum,
      @Parameter(description = "사용자가 제출한 답안 정보", required = true) @Valid @RequestBody
          QuestionRequestDTO.submitAnswerDTO answer,
      @Parameter(name = "userDetails", description = "인증된 사용자 정보", hidden = true)
          @AuthenticationPrincipal
          UserDetails userDetails) {
    QuestionResponseDTO.SubmitAnswerDTO result =
        questionService.submitAnswer(questionNum, answer, userDetails);
    return ApiResponse.onSuccess(result);
  }

  @GetMapping("/answer/{questionNum}")
  @Operation(summary = "풀이 및 정답 확인하기 API", description = "풀이 및 정답 확인하기 API")
  @ApiResponses({
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "200",
        description = "OK, 성공",
        content =
            @Content(
                schema = @Schema(implementation = ApiResponse.class),
                examples = {
                  @ExampleObject(
                      name = "COMMON200",
                      value =
                          "{\"isSuccess\":true,\"code\":\"COMMON200\",\"message\":\"성공\",\"data\":{\"questionId\":1,\"answer\":\"정답\",\"explanation\":\"풀이 내용\"}}")
                }))
  })
  public ApiResponse<QuestionResponseDTO.GetAnswerDTO> getAnswer(
      @Parameter(description = "질문 번호", example = "1") @PathVariable Integer questionNum) {
    QuestionResponseDTO.GetAnswerDTO answer = questionService.getAnswer(questionNum);
    return ApiResponse.onSuccess(answer);
  }
}