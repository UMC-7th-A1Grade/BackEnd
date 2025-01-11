package com.umc7th.a1grade.domain.temp.exception;

import com.umc7th.a1grade.global.apiPayload.code.BaseErrorCode;
import com.umc7th.a1grade.global.apiPayload.code.ErrorReasonDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GeneralException extends RuntimeException {

  private BaseErrorCode code;

  public ErrorReasonDTO getErrorReason() {
    return this.code.getReason();
  }

  public ErrorReasonDTO getErrorReasonHttpStatus() {
    return this.code.getReasonHttpStatus();
  }
}
