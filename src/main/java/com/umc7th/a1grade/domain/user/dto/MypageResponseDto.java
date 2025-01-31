package com.umc7th.a1grade.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@Getter
@Builder
@AllArgsConstructor
@Schema(title = "마이페이지 - 사용자의 정답률 조회 응답 객체")
public class MypageResponseDto {
  @Schema(description = "사용자의 닉네임", example = "일급등")
  private String nickName;

  @Schema(description = "사용자의 정답률", example = "30")
  private int grade;
}
