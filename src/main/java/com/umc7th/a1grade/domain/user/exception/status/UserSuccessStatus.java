package com.umc7th.a1grade.domain.user.exception.status;

import org.springframework.http.HttpStatus;

import com.umc7th.a1grade.global.apiPayload.code.BaseCode;
import com.umc7th.a1grade.global.apiPayload.code.ReasonDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserSuccessStatus implements BaseCode {
  _NICKNAME_OK(HttpStatus.OK, "NINKNAME2000", "사용가능한 닉네임입니다."),
  ;

  private final HttpStatus httpStatus;
  private final String code;
  private final String message;

  @Override
  public ReasonDTO getReason() {
    return ReasonDTO.builder().message(message).code(code).isSuccess(true).build();
  }

  @Override
  public ReasonDTO getReasonHttpStatus() {
    return ReasonDTO.builder()
        .message(message)
        .code(code)
        .isSuccess(true)
        .httpStatus(httpStatus)
        .build();
  }
}
