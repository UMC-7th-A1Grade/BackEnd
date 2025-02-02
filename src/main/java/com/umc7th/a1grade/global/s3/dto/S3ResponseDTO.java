package com.umc7th.a1grade.global.s3.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class S3ResponseDTO {

  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @Schema(title = "Question : 문제 이미지 URL DTO")
  public static class ImgUrlDTO {
    @Schema(description = "문제 이미지 URL", example = "https://questionImg.com")
    String imageUrl;
  }
}
