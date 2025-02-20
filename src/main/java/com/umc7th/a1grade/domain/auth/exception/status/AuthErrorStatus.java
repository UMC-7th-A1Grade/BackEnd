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
  _USER_INFO_FAIL(HttpStatus.UNAUTHORIZED, "AUTH4003", "구글 사용자 정보 요청 실패"),

  _INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH4004", "유효하지 않은 토큰입니다."),
  _TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "AUTH4005", "토큰이 만료되었습니다."),
  _REFRESH_TOKEN_REQUIRED(HttpStatus.FORBIDDEN, "AUTH4006", "리프레시 토큰이 필요합니다."),
  _RTR_FAIL(HttpStatus.BAD_REQUEST, "AUTH4007", "리프레시 토큰 재발급 실패"),
  _RTR_FAIL_DELETE(HttpStatus.BAD_REQUEST, "AUTH4007", "리프레시 토큰 삭제 실패"),

  _LOGOUT_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "AUTH5000", "로그아웃 처리 중 오류가 발생했습니다."),
  _TEST_LOGIN_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "AUTH5001", "테스트 유저 로그인 처리 중 오류가 발생했습니다."),

  EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "JWT_EXPIRED4001", "JWT 토큰이 만료되었습니다."),
  UNSUPPORTED_TOKEN(HttpStatus.UNAUTHORIZED, "JWT_UNSUPPORTED4002", "지원되지 않는 JWT 형식입니다."),
  MALFORMED_TOKEN(HttpStatus.UNAUTHORIZED, "JWT_MALFORMED4003", "JWT 형식이 올바르지 않습니다."),
  INVALID_SIGNATURE(HttpStatus.UNAUTHORIZED, "JWT_INVALID_SIGNATURE4004", "JWT 서명이 유효하지 않습니다."),
  ILLEGAL_ARGUMENT(HttpStatus.UNAUTHORIZED, "JWT_ILLEGAL_ARGUMENT4005", "JWT 토큰 값이 잘못되었습니다.");

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
