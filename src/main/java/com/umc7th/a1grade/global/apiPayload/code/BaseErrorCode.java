package com.umc7th.a1grade.global.apiPayload.code;

public interface BaseErrorCode {

  ErrorReasonDTO getReason();

  ErrorReasonDTO getReasonHttpStatus();
}
