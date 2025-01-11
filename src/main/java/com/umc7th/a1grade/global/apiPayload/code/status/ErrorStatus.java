package com.umc7th.a1grade.global.apiPayload.code.status;

import org.springframework.http.HttpStatus;

import com.umc7th.a1grade.global.apiPayload.code.BaseErrorCode;
import com.umc7th.a1grade.global.apiPayload.code.ErrorReasonDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

  // Common Error
  _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
  _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다."),
  _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),
  _NOT_FOUND(HttpStatus.NOT_FOUND, "COMMON404", "존재하지 않는 리소스입니다."),
  _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),

  // User Error
  _USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "USER4001", "존재하지 않는 사용자입니다."),
  _USER_NICKNAME_EXIST(HttpStatus.BAD_REQUEST, "USER4002", "이미 존재하는 닉네임입니다."),
  _USER_EMAIL_EXIST(HttpStatus.BAD_REQUEST, "USER4003", "이미 가입되어 있는 이메일입니다."),

  // Question Error
  _QUESTION_NOT_FOUND(HttpStatus.NOT_FOUND, "QUESTION4001", "존재하지 않는 문제입니다."),

  // Error Test
  TEMP_EXCEPTION(HttpStatus.BAD_REQUEST, "TEMP4001", "에러 테스트");

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
