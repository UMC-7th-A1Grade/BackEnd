package com.umc7th.a1grade.domain.user.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@Builder
@AllArgsConstructor
@Schema(title = "AllGradeResponseDto : 3순위 사용자 조회 응답 DTO")
public class AllGradeResponseDto {
  @Schema(description = "사용자 ID", example = "1")
  private Long userId;

  @Schema(description = "사용자의 닉네임", example = "일급등")
  private String nickName;

  @Schema(description = "사용자의 오답 정답 개수", example = "120")
  private Long correctCount;

  @Schema(description = "정답률 (소수점 포함)", example = "0.75")
  private BigDecimal accuracy;
}
