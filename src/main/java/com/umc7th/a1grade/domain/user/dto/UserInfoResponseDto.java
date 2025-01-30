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
@Schema(description = "사용자 정보 응답 DTO")
public class UserInfoResponseDto {
  @Schema(description = "사용자 ID", example = "1")
  private Long id;

  @Schema(description = "사용자의 소셜 로그인 ID", example = "sdfadgdaf123456789")
  private String socialId;

  @Schema(description = "사용자의 이메일", example = "test@example.com")
  private String email;

  @Schema(description = "사용자의 닉네임", example = "일급등")
  private String nickName;

  @Schema(description = "현재 선택된 캐릭터 ID", example = "2")
  private Long selectedCharacter;

  public UserInfoResponseDto(User user, Long selectedCharacterId) {
    this.id = user.getId();
    this.socialId = user.getSocialId();
    this.email = user.getEmail();
    this.nickName = user.getNickName();
    this.selectedCharacter = selectedCharacterId;
  }
}
