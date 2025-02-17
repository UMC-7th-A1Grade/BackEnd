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
  _USER_INFO_INVALID(HttpStatus.BAD_REQUEST, "CHAR4005", "존재하지 않은 캐릭터 아이디 입니다."),
  _USER_CREDIT_ZERO(HttpStatus.BAD_REQUEST, "CREDIT4003", "사용자의 크레딧이 부족합니다."),

  _USER_REDIS_CONNECTION_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "REDIS5001", "Redis 서버에 연결할 수 없습니다."),
  _USER_REDIS_ACCESS_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "REDIS5002", "Redis 데이터 접근 중 오류가 발생했습니다."),
  _USER_JSON_PARSE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "JSON5003", "JSON 데이터 변환에 실패했습니다."),
  _USER_TOP3_NOT_FOUND(HttpStatus.NOT_FOUND, "TOP3_4006", "상위 3명에 대한 데이터가 없습니다.");

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
