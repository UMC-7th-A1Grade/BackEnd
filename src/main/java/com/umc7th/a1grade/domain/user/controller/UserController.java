package com.umc7th.a1grade.domain.user.controller;

import jakarta.validation.Valid;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.umc7th.a1grade.domain.user.dto.UserInfoDto;
import com.umc7th.a1grade.domain.user.exception.status.UserSuccessStatus;
import com.umc7th.a1grade.domain.user.service.UserService;
import com.umc7th.a1grade.global.apiPayload.ApiResponse;
import com.umc7th.a1grade.global.apiPayload.code.ErrorReasonDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "user", description = "회원 관리 API")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @Operation(
      summary = "닉네임 중복 확인",
      description = "사용자가 입력한 닉네임이 이미 존재하는지 확인합니다.",
      parameters = {
        @Parameter(
            name = "nickname",
            description = "중복 확인을 요청할 닉네임",
            required = true,
            example = "testUser")
      })
  @ApiResponses({
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "200",
        description = "사용가능한 닉네임입니다.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(type = "boolean", example = "true"))),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "400",
        description = "이미 존재하는 닉네임입니다.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorReasonDTO.class)))
  })
  @GetMapping("")
  public ApiResponse<Boolean> confirmNickName(
      @AuthenticationPrincipal UserDetails userDetails, @RequestParam("nickname") String nickname) {
    userService.confirmNickName(userDetails, nickname);
    return ApiResponse.of(UserSuccessStatus._NICKNAME_OK, true);
  }

  @Operation(summary = "닉네임 저장", description = "사용자에게 입력받은 닉네임을 저장합니다.")
  @PostMapping("")
  public ApiResponse<Boolean> saveNickame(
      @AuthenticationPrincipal UserDetails userDetails, @RequestBody @Valid UserInfoDto request) {
    return ApiResponse.onSuccess(true);
  }
}
