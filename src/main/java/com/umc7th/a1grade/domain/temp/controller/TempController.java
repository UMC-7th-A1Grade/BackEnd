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
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/temp")
@RequiredArgsConstructor
@Tag(name = "Temp", description = "응답 통일 테스트용 API")
public class TempController {

  private final TempQueryService tempQueryService;

  @Operation(
      summary = "응답 통일 테스트",
      description =
          """
           **Parameter**  \n
           -
           **Returns**  \n
            "isSuccess": true \n
            "code": "COMMON200" \n
            "message": "응답에 성공했습니다." \n
            "result": { \n
              "testString": "This is Test!" \n
            }
           """)
  @GetMapping("/test")
  public ApiResponse<TempResponse.TempTestDTO> testAPI() {

    return ApiResponse.onSuccess(TempConverter.toTempTestDTO());
  }

  @Operation(
      summary = "응답 통일 테스트(flag 사용)",
      description =
          """
           **Parameter**  \n
           flag: 1~4까지의 정수 \n
           **Returns**  \n
            "isSuccess": true \n
            "code": "COMMON200" \n
            "message": "응답에 성공했습니다." \n
            "result": { \n
              "testString": "This is Test!" \n
            }
           """)
  @GetMapping("/exception")
  public ApiResponse<TempResponse.TempExceptionDTO> exceptionAPI(@RequestParam Integer flag) {
    tempQueryService.CheckFlag(flag);
    return ApiResponse.onSuccess(TempConverter.toTempExceptionDTO(flag));
  }
}
