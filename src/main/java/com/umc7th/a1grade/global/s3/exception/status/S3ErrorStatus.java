package com.umc7th.a1grade.global.s3.exception.status;

import org.springframework.http.HttpStatus;

import com.umc7th.a1grade.global.apiPayload.code.BaseErrorCode;
import com.umc7th.a1grade.global.apiPayload.code.ErrorReasonDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum S3ErrorStatus implements BaseErrorCode {
  _FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "IMG4001", "존재하지 않는 이미지입니다."),
  _FILE_SIZE_INVALID(HttpStatus.BAD_REQUEST, "IMG4002", "파일 크기는 5MB를 초과할 수 없습니다."),
  _FILE_TYPE_INVALID(HttpStatus.BAD_REQUEST, "IMG4003", "이미지 파일만 업로드 가능합니다."),
  _FILE_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "IMG5001", "이미지 처리 중 서버 에러, 관리자에게 문의 바랍니다."),
  _INVALID_BASE64(HttpStatus.BAD_REQUEST, "IMG4005", "잘못된 Base64값 입니다.");

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
