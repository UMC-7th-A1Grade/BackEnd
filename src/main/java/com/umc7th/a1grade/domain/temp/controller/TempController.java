package com.umc7th.a1grade.domain.temp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.umc7th.a1grade.domain.temp.converter.TempConverter;
import com.umc7th.a1grade.domain.temp.dto.TempResponse;
import com.umc7th.a1grade.domain.temp.service.TempQueryService;
import com.umc7th.a1grade.global.apiPayload.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/temp")
@RequiredArgsConstructor
public class TempController {

  private final TempQueryService tempQueryService;

  @GetMapping("/test")
  public ApiResponse<TempResponse.TempTestDTO> testAPI() {

    return ApiResponse.onSuccess(TempConverter.toTempTestDTO());
  }

  @GetMapping("/exception")
  public ApiResponse<TempResponse.TempExceptionDTO> exceptionAPI(@RequestParam Integer flag) {
    tempQueryService.CheckFlag(flag);
    return ApiResponse.onSuccess(TempConverter.toTempExceptionDTO(flag));
  }
}
