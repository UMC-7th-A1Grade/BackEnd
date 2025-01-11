package com.umc7th.a1grade.domain.temp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class TempResponse {

  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @Schema(title = "Temp : 응답 코드 테스트용 DTO")
  public static class TempTestDTO {
    String testString;
  }

  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @Schema(title = "Temp : 응답 코드 테스트용 DTO")
  public static class TempExceptionDTO {
    @Schema(description = "응답 신호", example = "1 || 2 || 3 || 4")
    Integer flag;
  }
}
