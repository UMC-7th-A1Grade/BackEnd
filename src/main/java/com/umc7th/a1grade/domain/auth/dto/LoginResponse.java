package com.umc7th.a1grade.domain.auth.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Schema(title = "LoginResponse : 구글 소셜 로그인 응답 객체")
public class LoginResponse {
  @Schema(description = "사용자 이메일", example = "user@example.com")
  private String email;

  @Schema(description = "JWT 액세스 토큰", example = "eyJhbGciOiJIUzI1...")
  private String accessToken;

  @Schema(description = "구글 소셜 로그인 ID", example = "1234567890")
  private String socialId;

  @JsonIgnore private String refreshToken;

  public LoginResponse(String email, String accessToken, String socialId) {
    this.email = email;
    this.accessToken = accessToken;
    this.socialId = socialId;
  }
}
