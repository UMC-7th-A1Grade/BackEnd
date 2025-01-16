package com.umc7th.a1grade.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "UserInfoDto - 닉네임 입력을 위한 데이터 전송 객체")
public class UserInfoDto {
  @NotBlank(message = "닉네임은 필수 입력 항목입니다. 최대 5자 까지 입력 가능")
  @Size(max = 5)
  @Schema(description = "닉네임", example = "홍길동", type = "string")
  private String nickname;
}
