package com.umc7th.a1grade.domain.user.exception.status;

import org.springframework.http.HttpStatus;

import com.umc7th.a1grade.global.apiPayload.code.BaseErrorCode;
import com.umc7th.a1grade.global.apiPayload.code.ErrorReasonDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserErrorStatus implements BaseErrorCode {
  _USER_NOT_FOUND(HttpStatus.UNAUTHORIZED, "USER4001", "존재하지 않는 사용자입니다."),
  _USER_INVALID(HttpStatus.UNAUTHORIZED, "USER4002", "유효하지 않은 사용자입니다."),

  _USER_NICKNAME_EXIST(HttpStatus.BAD_REQUEST, "NICKNAME4002", "이미 존재하는 닉네임입니다."),
  _USER_NICKNAME_NULL(HttpStatus.BAD_REQUEST, "NICKNAME4003", "닉네임은 필수로 입력해야 합니다."),

  _USER_EMAIL_EXIST(HttpStatus.BAD_REQUEST, "EMAIL4004", "이미 가입되어 있는 이메일입니다."),
  _USER_INFO_INVALID(HttpStatus.BAD_REQUEST, "CHAR4005", "존재하지 않은 캐릭터 아이디 입니다.");

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
