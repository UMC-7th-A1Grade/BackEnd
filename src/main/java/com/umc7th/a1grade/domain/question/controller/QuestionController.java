package com.umc7th.a1grade.domain.question.controller;

import jakarta.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.umc7th.a1grade.domain.question.dto.QuestionRequestDTO;
import com.umc7th.a1grade.domain.question.dto.QuestionResponseDTO;
import com.umc7th.a1grade.domain.question.exception.status.QuestionErrorStatus;
import com.umc7th.a1grade.domain.question.service.QuestionService;
import com.umc7th.a1grade.global.annotation.ApiErrorCodeExample;
import com.umc7th.a1grade.global.apiPayload.ApiResponse;
import com.umc7th.a1grade.global.s3.PathName;
import com.umc7th.a1grade.global.s3.exception.status.S3ErrorStatus;

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

  @Operation(summary = "이미지 업로드 API", description = "이미지를 업로드하고 URL을 리턴받는 API")
  @ApiErrorCodeExample(S3ErrorStatus.class)
  @PostMapping(value = "/img-upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ApiResponse<QuestionResponseDTO.ImgUrlDTO> uploadImage(
      @RequestParam PathName pathName, MultipartFile file) {

    QuestionResponseDTO.ImgUrlDTO response = questionService.ImgUpload(pathName, file);
    return ApiResponse.onSuccess(response);
  }
}
