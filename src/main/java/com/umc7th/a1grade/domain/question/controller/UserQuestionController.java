package com.umc7th.a1grade.domain.question.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.umc7th.a1grade.domain.question.dto.UserQuestionRequestDTO;
import com.umc7th.a1grade.domain.question.dto.UserQuestionResponseDTO;
import com.umc7th.a1grade.domain.question.service.UserQuestionService;
import com.umc7th.a1grade.global.apiPayload.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user-questions")
public class UserQuestionController {

  private final UserQuestionService userQuestionService;

  @GetMapping("/")
  @Operation(summary = "저장소", description = " 사용자 저장소의 문제를 모두 조회하는 api")
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
  public ApiResponse<UserQuestionResponseDTO.GetUserQuestionDTO> getAllUserQuestions() {
    userQuestionService.getAllUserQuestions();
    return null;
  }

  @GetMapping("/{type}")
  @Operation(summary = "저장소", description = " 사용자 저장소의 문제를 타입별로 조회하는 api")
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
  public ApiResponse<UserQuestionResponseDTO.GetUserQuestionDTO> getUserQuestion(
      @PathVariable String type) {
    userQuestionService.getUserQuestion();
    return null;
  }

  @PostMapping("/memo")
  @Operation(summary = "메모 등록", description = " 사용자 문제의 메모를 등록하는 api")
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
  public ApiResponse<UserQuestionResponseDTO.ResUserQuestionMemoDTO> registerUserQuestionMemo(
      @RequestBody UserQuestionRequestDTO.ReqUserQuestionMemoDTO memo) {
    userQuestionService.registerUserQuestionMemo();
    return null;
  }

  @PatchMapping("memo")
  @Operation(summary = "메모 수정", description = " 사용자 문제의 메모를 수정하는 api")
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
  public ApiResponse<UserQuestionResponseDTO.ResUserQuestionMemoDTO> patchUserQuestionMemo(
      @RequestBody UserQuestionRequestDTO.ReqUserQuestionMemoDTO memo) {
    userQuestionService.patchUserQuestionMemo();
    return null;
  }

  @PostMapping("save")
  @Operation(summary = "문제 저장", description = " 사용자 문제를 저장하는 api")
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
  public ApiResponse<UserQuestionResponseDTO.SaveUserQuestionDTO> saveUserQuestion() {
    userQuestionService.saveUserQuestion();
    return null;
  }

  @PostMapping("/credit/confirm")
  @Operation(summary = "크래딧 소모 확인", description = " 사용자 크래딧 소모 여부를 확인하는 api")
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
  public ApiResponse<UserQuestionResponseDTO.CreditConfirmDTO> handleCreditConfirm(
      @RequestParam boolean confirm) {
    if (confirm) {
      return null;
    } else {
      return null;
    }
  }

  @PostMapping("/similar-question")
  @Operation(summary = "유사문제 저장", description = " 사용자 문제와 유사한 문제를 조회하는 api")
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
  public ApiResponse<UserQuestionResponseDTO> saveSimilarQuestion() {
    userQuestionService.saveSimilarQuestion();
    return null;
  }
}
