package com.umc7th.a1grade.domain.auth.exception.status;

import org.springframework.http.HttpStatus;

import com.umc7th.a1grade.global.apiPayload.code.BaseErrorCode;
import com.umc7th.a1grade.global.apiPayload.code.ErrorReasonDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuthErrorStatus implements BaseErrorCode {
  _LOGIN_FAIL(HttpStatus.BAD_REQUEST, "AUTH4001", "구글 로그인 처리 중 오류 발생"),
  _TOKEN_FAIL(HttpStatus.UNAUTHORIZED, "AUTH4002", "구글 액세스 토큰 요청 실패"),
  _USER_INFO_FAIL(HttpStatus.BAD_REQUEST, "AUTH4003", "구글 사용자 정보 요청 실패"),

  _INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH4004", "유효하지 않은 토큰입니다."),
  _TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "AUTH4005", "토큰이 만료되었습니다."),
  _REFRESH_TOKEN_REQUIRED(HttpStatus.FORBIDDEN, "AUTH4006", "리프레시 토큰이 필요합니다."),

  _LOGOUT_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "AUTH5000", "로그아웃 처리 중 오류가 발생했습니다.");

  private final HttpStatus httpStatus;
  private final String code;
  private final String message;

  @Override
  public ErrorReasonDTO getReason() {
    return ErrorReasonDTO.builder().message(message).code(code).isSuccess(false).build();
  }

  @Override
  public ErrorReasonDTO getReasonHttpStatus() {
    return ErrorReasonDTO.builder()
        .message(message)
        .code(code)
        .isSuccess(false)
        .httpStatus(httpStatus)
        .build();
  }
}
