package com.umc7th.a1grade.domain.user.exception;

import com.umc7th.a1grade.global.apiPayload.code.BaseErrorCode;
import com.umc7th.a1grade.global.exception.GeneralException;

public class UserHandler extends GeneralException {

  public UserHandler(BaseErrorCode code) {
    super(code);
  }
}
