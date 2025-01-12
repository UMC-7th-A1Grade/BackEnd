package com.umc7th.a1grade.domain.question.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.umc7th.a1grade.domain.question.dto.QuestionResponseDTO;
import com.umc7th.a1grade.global.apiPayload.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/questions")
public class QuestionController {

  @GetMapping("/similar-question")
  @Operation(summary = "유사문제 조회", description = "유사문제를 조회하는 api")
  @ApiResponses({
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "COMMON200",
        description = "OK, 성공"),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "AUTH003",
        description = "access 토큰을 주세요!",
        content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "AUTH004",
        description = "acess 토큰 만료",
        content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "AUTH006",
        description = "acess 토큰 모양이 이상함",
        content = @Content(schema = @Schema(implementation = ApiResponse.class))),
  })
  ApiResponse<QuestionResponseDTO.GetSimilarQuestionDTO> getSimilarQuestion() {

    return null;
  }
}
