package com.umc7th.a1grade.domain.credit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(title = "Credit 정보 응답 DTO")
public class CreditResponse {

  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class createResponse {
    @Schema(description = "크레딧 생성 결과", example = "true")
    Boolean result;
  }

  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class getResponse {
    @Schema(description = "크레딧 조회 결과", example = "true")
    Boolean result;

    @Schema(description = "크레딧 개수", example = "10")
    Integer totalCredit;
  }
}
