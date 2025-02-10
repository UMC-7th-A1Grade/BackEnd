package com.umc7th.a1grade.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@Schema(title = "UserNicknameResponseDto : 사용자 닉네임 응답 DTO")
public class UserNicknameResponseDto {
  @Schema(description = "사용자의 닉네임", example = "일급등")
  private String nickName;
}
