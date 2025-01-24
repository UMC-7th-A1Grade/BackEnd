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
import com.umc7th.a1grade.global.apiPayload.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "user", description = "회원 관리 API")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
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
  @GetMapping("")
  public ApiResponse<Boolean> confirmNickName(
      @AuthenticationPrincipal UserDetails userDetails, @RequestParam("nickname") String nickname) {
    return ApiResponse.onSuccess(true);
  }

  @Operation(summary = "닉네임 저장", description = "사용자에게 입력받은 닉네임을 저장합니다.")
  @PostMapping("")
  public ApiResponse<Boolean> saveNickame(
      @AuthenticationPrincipal UserDetails userDetails, @RequestBody @Valid UserInfoDto request) {
    return ApiResponse.onSuccess(true);
  }
}
