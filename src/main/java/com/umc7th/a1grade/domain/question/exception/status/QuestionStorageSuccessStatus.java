package com.umc7th.a1grade.domain.question.exception.status;

import org.springframework.http.HttpStatus;

import com.umc7th.a1grade.global.apiPayload.code.BaseCode;
import com.umc7th.a1grade.global.apiPayload.code.ReasonDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum QuestionStorageSuccessStatus implements BaseCode {
  _QUESTION_STORAGE_SUCCESS(HttpStatus.OK, "STORAGE2002", "응답에 성공했습니다."),
  _NULL_QUESTION_SUCCESS(HttpStatus.OK, "STORAGE2003", "응답에 성공했습니다. 저장소에 저장된 문제가 존재하지 않습니다.");

  private final HttpStatus httpStatus;
  private final String code;
  private final String message;

  @Override
  public ReasonDTO getReason() {
    return ReasonDTO.builder().message(message).code(code).isSuccess(false).build();
  }

  @Override
  public ReasonDTO getReasonHttpStatus() {
    return ReasonDTO.builder()
        .message(message)
        .code(code)
        .isSuccess(false)
        .httpStatus(httpStatus)
        .build();
  }
}
