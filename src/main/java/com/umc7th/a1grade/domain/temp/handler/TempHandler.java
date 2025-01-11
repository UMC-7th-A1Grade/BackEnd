package com.umc7th.a1grade.domain.temp.handler;

import com.umc7th.a1grade.domain.temp.exception.GeneralException;
import com.umc7th.a1grade.global.apiPayload.code.BaseErrorCode;

public class TempHandler extends GeneralException {

  public TempHandler(BaseErrorCode errorCode) {
    super(errorCode);
  }
}
