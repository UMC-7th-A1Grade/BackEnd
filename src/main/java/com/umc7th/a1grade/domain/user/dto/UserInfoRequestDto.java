package com.umc7th.a1grade.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
@Schema(title = "UserInfoRequestDto : 사용자가 입력한 닉네임, 캐릭터 정보 저장을 위한 데이터 전송 DTO")
public class UserInfoRequestDto {
  @NotBlank(message = "닉네임은 필수 입력 항목입니다. 최대 5자 까지 입력 가능")
  @Size(max = 5)
  @Schema(description = "닉네임", example = "일급등", type = "string")
  private String nickname;

  @NotNull(message = "캐릭터 Id는 필수 입력 값입니다.")
  @Schema(description = "캐릭터 Id", example = "2", type = "Long")
  private Long characterId;
}
