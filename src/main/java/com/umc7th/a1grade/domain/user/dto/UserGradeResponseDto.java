package com.umc7th.a1grade.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@Getter
@Builder
@AllArgsConstructor
@Schema(title = "UserGradeResponseDto - 사용자의 등급 조회 응답 DTO")
public class UserGradeResponseDto {
  @Schema(description = "사용자의 닉네임", example = "일급등")
  private String nickName;

  @Schema(description = "사용자의 오답 정답 개수", example = "30")
  private int grade;
}
