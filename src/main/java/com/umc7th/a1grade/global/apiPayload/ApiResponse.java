package com.umc7th.a1grade.global.apiPayload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.umc7th.a1grade.global.apiPayload.code.BaseCode;
import com.umc7th.a1grade.global.apiPayload.code.status.CommonSuccessStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"isSuccess", "code", "message", "result"})
@Schema(title = "ApiResponse : 응답 코드 확인 DTO")
public class ApiResponse<T> {

  @JsonProperty("isSuccess")
  @Schema(description = "응답 성공 여부", example = "true")
  private final Boolean isSuccess;

  @Schema(description = "응답 코드", example = "COMMON200")
  private final String code;

  @Schema(description = "응답 메세지", example = "응답에 성공했습니다.")
  private final String message;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  @Schema(description = "응답 결과", example = "Result")
  private T result;

  public static <T> ApiResponse<T> onSuccess(T result) {
    return new ApiResponse<>(
        true, CommonSuccessStatus._OK.getCode(), CommonSuccessStatus._OK.getMessage(), result);
  }

  public static <T> ApiResponse<T> of(BaseCode code, T result) {
    return new ApiResponse<>(
        true,
        code.getReasonHttpStatus().getCode(),
        code.getReasonHttpStatus().getMessage(),
        result);
  }

  public static <T> ApiResponse<T> onFailure(String code, String message, T data) {
    return new ApiResponse<>(false, code, message, data);
  }
}
