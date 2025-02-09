package com.umc7th.a1grade.domain.user.dto;

import com.umc7th.a1grade.domain.user.entity.User;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@Schema(title = "UserCreditResponseDto : 사용자 크레딧 응답 DTO")
public class UserCreditResponseDto {

  @Schema(description = "사용자의 크레딧", example = "100")
  private Integer credit;

  public UserCreditResponseDto(User user) {
    this.credit = user.getCredit();
  }
}
