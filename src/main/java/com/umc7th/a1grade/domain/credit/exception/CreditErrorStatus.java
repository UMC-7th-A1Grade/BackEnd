package com.umc7th.a1grade.domain.credit.exception;

import org.springframework.http.HttpStatus;

import com.umc7th.a1grade.global.apiPayload.code.BaseErrorCode;
import com.umc7th.a1grade.global.apiPayload.code.ErrorReasonDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CreditErrorStatus implements BaseErrorCode {
  _USER_NOT_FOUND(HttpStatus.NOT_FOUND, "CREDIT4001", "존재하지 않는 사용자입니다."),
  _CREDIT_NOT_FOUND(HttpStatus.NOT_FOUND, "CREDIT4002", "크레딧이 존재하지 않습니다."),
  _CREDIT_EXIST(HttpStatus.BAD_REQUEST, "CREDIT4003", "이미 크레딧이 존재합니다."),
  _CREDIT_COUNT_INVALID(HttpStatus.BAD_REQUEST, "CREDIT4004", "크레딧 개수가 0개입니다."),
  _CREDIT_SERVER_ERROR(
      HttpStatus.INTERNAL_SERVER_ERROR, "CREDIT5001", "크레딧 처리 중 서버 에러, 관리자에게 문의 바랍니다.");

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
