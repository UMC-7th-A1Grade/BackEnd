package com.umc7th.a1grade.domain.temp.service;

import org.springframework.stereotype.Service;

import com.umc7th.a1grade.domain.temp.handler.TempHandler;
import com.umc7th.a1grade.global.apiPayload.code.status.ErrorStatus;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TempQueryServiceImpl implements TempQueryService {

  @Override
  public void CheckFlag(Integer flag) {
    if (flag == 1) throw new TempHandler(ErrorStatus.TEMP_EXCEPTION);
  }
}
