package com.umc7th.a1grade.domain.temp.service;

import org.springframework.stereotype.Service;

import com.umc7th.a1grade.domain.temp.handler.TempHandler;
import com.umc7th.a1grade.domain.temp.handler.status.TempErrorStatus;
import com.umc7th.a1grade.global.apiPayload.code.status.CommonErrorStatus;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TempQueryServiceImpl implements TempQueryService {

  @Override
  public void CheckFlag(Integer flag) {
    if (flag == 1) throw new TempHandler(TempErrorStatus._TEMP_EXCEPTION);
    else if (flag == 2) throw new TempHandler(CommonErrorStatus._FORBIDDEN);
    else if (flag == 3) throw new TempHandler(CommonErrorStatus._NOT_FOUND);
    else if (flag == 4) throw new TempHandler(CommonErrorStatus._INTERNAL_SERVER_ERROR);
  }
}
