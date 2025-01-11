package com.umc7th.a1grade.domain.temp.handler;

import com.umc7th.a1grade.global.apiPayload.code.BaseErrorCode;
import com.umc7th.a1grade.global.exception.GeneralException;

public class TempHandler extends GeneralException {

  public TempHandler(BaseErrorCode errorCode) {
    super(errorCode);
  }
}
