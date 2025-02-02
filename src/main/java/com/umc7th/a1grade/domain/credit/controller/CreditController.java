package com.umc7th.a1grade.domain.credit.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.umc7th.a1grade.domain.credit.dto.CreditResponse;
import com.umc7th.a1grade.domain.credit.dto.CreditResponse.createResponse;
import com.umc7th.a1grade.domain.credit.dto.CreditResponse.getResponse;
import com.umc7th.a1grade.domain.credit.exception.CreditErrorStatus;
import com.umc7th.a1grade.domain.credit.service.CreditService;
import com.umc7th.a1grade.global.annotation.ApiErrorCodeExample;
import com.umc7th.a1grade.global.apiPayload.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "credit", description = "회원의 크레딧 관리 API")
@RestController
@RequestMapping("/api/credits")
@RequiredArgsConstructor
public class CreditController {
  private final CreditService creditService;

  @Operation(summary = "사용자 크레딧 생성", description = "사용자 크레딧 초기화, 기본값: 10")
  @ApiErrorCodeExample(CreditErrorStatus.class)
  @PostMapping(produces = "application/json")
  public ApiResponse<createResponse> createCredit(
      @AuthenticationPrincipal UserDetails userDetails) {
    CreditResponse.createResponse response = creditService.createCredit(userDetails);
    return ApiResponse.onSuccess(response);
  }

  @Operation(summary = "사용자 크레딧 개수 조회", description = "사용자 현재 크레딧 개수 조회")
  @ApiErrorCodeExample(CreditErrorStatus.class)
  @GetMapping(produces = "application/json")
  public ApiResponse<getResponse> getCredit(@AuthenticationPrincipal UserDetails userDetails) {
    CreditResponse.getResponse response = creditService.getCredit(userDetails);
    return ApiResponse.onSuccess(response);
  }

  @Operation(summary = "사용자 크레딧 개수 수정", description = "사용자 현재 크레딧 1개 차감")
  @ApiErrorCodeExample(CreditErrorStatus.class)
  @PatchMapping(produces = "application/json")
  public ApiResponse<getResponse> updateCredit(@AuthenticationPrincipal UserDetails userDetails) {
    CreditResponse.getResponse response = creditService.updateCredit(userDetails);
    return ApiResponse.onSuccess(response);
  }
}
