package com.umc7th.a1grade.domain.temp.handler.status;

import org.springframework.http.HttpStatus;

import com.umc7th.a1grade.global.apiPayload.code.BaseErrorCode;
import com.umc7th.a1grade.global.apiPayload.code.ErrorReasonDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TempErrorStatus implements BaseErrorCode {
  _TEMP_EXCEPTION_FLAG_1(HttpStatus.BAD_REQUEST, "TEMP4001", "에러 테스트_FLAG_1"),
  _TEMP_EXCEPTION_FLAG_2(HttpStatus.BAD_REQUEST, "TEMP4002", "에러 테스트_FLAG_2"),
  _TEMP_EXCEPTION_FLAG_3(HttpStatus.NOT_FOUND, "TEMP4041", "에러 테스트_FLAG_3"),
  _TEMP_EXCEPTION_FLAG_4(HttpStatus.NOT_FOUND, "TEMP4042", "에러 테스트_FLAG_4");

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
