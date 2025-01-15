package com.umc7th.a1grade.domain.auth.exception.status;

import org.springframework.http.HttpStatus;

import com.umc7th.a1grade.global.apiPayload.code.BaseCode;
import com.umc7th.a1grade.global.apiPayload.code.ReasonDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuthSuccessStatus implements BaseCode {
  _LOGIN_SUCCESS(HttpStatus.OK, "AUTH2002", "로그인에 성공했습니다. JWT 액세스 토큰과 사용자 정보 반환 완료."),
  _LOGOUT_SUCCESS(HttpStatus.OK, "AUTH2003", "로그아웃에 성공했습니다."),
  _TOKEN_REFRESH_SUCCESS(HttpStatus.OK, "AUTH2004", "토큰 재발급에 성공했습니다.");

  private final HttpStatus httpStatus;
  private final String code;
  private final String message;

  @Override
  public ReasonDTO getReason() {
    return ReasonDTO.builder().message(message).code(code).isSuccess(false).build();
  }

  @Override
  public ReasonDTO getReasonHttpStatus() {
    return ReasonDTO.builder()
        .message(message)
        .code(code)
        .isSuccess(false)
        .httpStatus(httpStatus)
        .build();
  }
}
