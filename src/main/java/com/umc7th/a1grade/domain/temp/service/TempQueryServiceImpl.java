package com.umc7th.a1grade.domain.temp.service;

import org.springframework.stereotype.Service;

import com.umc7th.a1grade.domain.temp.exception.status.TempErrorStatus;
import com.umc7th.a1grade.domain.temp.handler.TempHandler;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TempQueryServiceImpl implements TempQueryService {

  @Override
  public void CheckFlag(Integer flag) {
    if (flag == 1) throw new TempHandler(TempErrorStatus._TEMP_EXCEPTION_FLAG_1);
    else if (flag == 2) throw new TempHandler(TempErrorStatus._TEMP_EXCEPTION_FLAG_2);
    else if (flag == 3) throw new TempHandler(TempErrorStatus._TEMP_EXCEPTION_FLAG_3);
    else if (flag == 4) throw new TempHandler(TempErrorStatus._TEMP_EXCEPTION_FLAG_4);
  }
}
