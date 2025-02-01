package com.umc7th.a1grade.domain.ranking.dto;

import com.umc7th.a1grade.domain.user.entity.User;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "UserRankingDto", description = "사용자 랭킹 정보 DTO")
public class UserRankingDto {

  @Schema(description = "사용자 정보 객체", implementation = User.class)
  private User user;

  @Schema(description = "정답 개수", example = "120")
  private Long correctCount;

  @Schema(description = "총 제출 횟수", example = "150")
  private Long totalAttempts;

  @Schema(description = "정답률 (소수점 포함)", example = "0.80")
  private double accuracy;
}
