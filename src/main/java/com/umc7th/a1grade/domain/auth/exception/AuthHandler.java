package com.umc7th.a1grade.domain.auth.exception;

import com.umc7th.a1grade.global.apiPayload.code.BaseErrorCode;
import com.umc7th.a1grade.global.exception.GeneralException;

public class AuthHandler extends GeneralException {
  public AuthHandler(BaseErrorCode code) {
    super(code);
  }
}
