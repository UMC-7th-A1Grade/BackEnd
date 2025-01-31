package com.umc7th.a1grade.domain.temp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.umc7th.a1grade.domain.temp.converter.TempConverter;
import com.umc7th.a1grade.domain.temp.dto.TempResponse;
import com.umc7th.a1grade.domain.temp.exception.status.TempErrorStatus;
import com.umc7th.a1grade.domain.temp.service.TempQueryService;
import com.umc7th.a1grade.global.annotation.ApiErrorCodeExample;
import com.umc7th.a1grade.global.apiPayload.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/temp")
@RequiredArgsConstructor
@Tag(name = "Temp", description = "응답 통일 테스트용 API")
public class TempController {

  private final TempQueryService tempQueryService;

  @Operation(summary = "응답 통일 테스트", description = """
           테스트 성공 시 응답 예시  \n
           """)
  @GetMapping(value = "/test", produces = "application/json")
  public ApiResponse<TempResponse.TempTestDTO> testAPI() {

    return ApiResponse.onSuccess(TempConverter.toTempTestDTO());
  }

  @Operation(
      summary = "응답 통일 테스트(flag 사용)",
      description = """
           테스트 실패시 flag에 따라 다른 응답을 보내는 API  \n
           """)
  @ApiErrorCodeExample(TempErrorStatus.class)
  @GetMapping(value = "/exception", produces = "application/json")
  public ApiResponse<TempResponse.TempExceptionDTO> exceptionAPI(@RequestParam Integer flag) {
    tempQueryService.CheckFlag(flag);
    return ApiResponse.onSuccess(TempConverter.toTempExceptionDTO(flag));
  }
}
