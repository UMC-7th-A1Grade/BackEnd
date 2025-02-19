package com.umc7th.a1grade.domain.question.exception.status;

import org.springframework.http.HttpStatus;

import com.umc7th.a1grade.global.apiPayload.code.BaseErrorCode;
import com.umc7th.a1grade.global.apiPayload.code.ErrorReasonDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum QuestionStorageErrorStatus implements BaseErrorCode {
<<<<<<< HEAD
  EMPTY_LIST_REQUEST(HttpStatus.BAD_REQUEST, "STORAGE4000", "요청한 리스트가 비어있습니다."),
  INVALID_QUESTION_TYPE(HttpStatus.BAD_REQUEST, "STORAGE4001", "유효하지 않은 문제 TYPE입니다."),
  NO_QUESTION_TYPE(HttpStatus.BAD_REQUEST, "STORAGE4002", "문제 TYPE이 null 입니다."),

  FORBIDDEN_DELETE_REQUEST(HttpStatus.FORBIDDEN, "STORAGE4030", "해당 리소스를 삭제할 권한이 없습니다."),

  RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "STORAGE4040", "해당 리소스를 찾을 수 없습니다."),
=======
  INVALID_QUESTION_TYPE(HttpStatus.BAD_REQUEST, "STORAGE4005", "유효하지 않은 문제 TYPE입니다."),
  NO_QUESTION_TYPE(HttpStatus.BAD_REQUEST, "STORAGE4006", "문제 TYPE이 null 입니다."),
  NO_QUESTIONS_FOUND(HttpStatus.NOT_FOUND, "STORAGE4010", "조건에 맞는 문제가 저장소에 존재하지 않습니다."),
>>>>>>> d2bea99 (♻️ Refactor: 저장소 문제 조회 리턴값 수정)

  QUESTION_DATABASE_ERROR(
      HttpStatus.INTERNAL_SERVER_ERROR, "STORAGE5000", "문제 조회 중 데이터베이스 오류가 발생하였습니다.");

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
