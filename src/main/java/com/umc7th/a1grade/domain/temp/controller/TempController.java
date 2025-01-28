package com.umc7th.a1grade.domain.temp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.umc7th.a1grade.domain.temp.converter.TempConverter;
import com.umc7th.a1grade.domain.temp.dto.TempResponse;
import com.umc7th.a1grade.domain.temp.service.TempQueryService;
import com.umc7th.a1grade.global.apiPayload.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
  @GetMapping("/test")
  public ApiResponse<TempResponse.TempTestDTO> testAPI() {

    return ApiResponse.onSuccess(TempConverter.toTempTestDTO());
  }

  @Operation(
      summary = "응답 통일 테스트(flag 사용)",
      description = """
           테스트 실패시 flag에 따라 다른 응답을 보내는 API  \n
           """)
  @ApiResponses(
      value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            content = {
              @Content(
                  mediaType = "application/json",
                  examples = {
                    @ExampleObject(name = "TEMP4001", description = "flag 1일 때"),
                    @ExampleObject(name = "TEMP4002", description = "flag 2일 때")
                  })
            }),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            content = {
              @Content(
                  mediaType = "application/json",
                  examples = {
                    @ExampleObject(name = "TEMP4041", description = "flag 3일 때"),
                    @ExampleObject(name = "TEMP4042", description = "flag 4일 때")
                  })
            })
      })
  @GetMapping("/exception")
  public ApiResponse<TempResponse.TempExceptionDTO> exceptionAPI(@RequestParam Integer flag) {
    tempQueryService.CheckFlag(flag);
    return ApiResponse.onSuccess(TempConverter.toTempExceptionDTO(flag));
  }
}
