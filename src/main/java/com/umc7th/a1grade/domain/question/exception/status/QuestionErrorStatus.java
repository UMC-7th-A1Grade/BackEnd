package com.umc7th.a1grade.domain.question.exception.status;

import org.springframework.http.HttpStatus;

import com.umc7th.a1grade.global.apiPayload.code.BaseErrorCode;
import com.umc7th.a1grade.global.apiPayload.code.ErrorReasonDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum QuestionErrorStatus implements BaseErrorCode {
  QUESTION_NOT_FOUND(HttpStatus.NOT_FOUND, "QUESTION4004", "존재하지 않는 문제입니다."),
  USER_QUESTION_NOT_FOUND(HttpStatus.NOT_FOUND, "QUESTION4003", "유저 문제가 존재하지 않습니다"),
  INVALID_QUESTION_ID(HttpStatus.BAD_REQUEST, "QUESTION4005", "유효하지 않은 문제 ID입니다."),
  INVALID_ANSWER(HttpStatus.BAD_REQUEST, "QUESTION4006", "답안이 유효하지 않습니다."),
  INVALID_MEMO(HttpStatus.BAD_REQUEST, "QUESTION4007", "메모가 유효하지 않습니다."),
  INSUFFICENT_QUESTIONS(HttpStatus.BAD_REQUEST, "QUESTION4008", "저장된 문제가 3개 미만입니다."),
  INVALID_QUESTION_TYPE(HttpStatus.BAD_REQUEST, "QUESTION4009", "유효하지 않은 문제 타입입니다."),
  NO_QUESTIONS_FOUND(HttpStatus.NOT_FOUND, "QUESTION4010", "조건에 맞는 문제가 없습니다."),
  QUESTIONLOG_NOT_FOUND(HttpStatus.NOT_FOUND, "QUESTION4009", "문제 로그가 존재하지 않습니다."),
  DUPLICATE_QUESTIONS(HttpStatus.BAD_REQUEST, "QUESTION4091", "이미 저장된 문제입니다."),

  QUESTION_DATABASE_ERROR(
      HttpStatus.INTERNAL_SERVER_ERROR, "QUESTION5000", "문제 조회 중 데이터베이스 오류가 발생하였습니다.");

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
