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
@Schema(title = "LoginResponse - 구글 소셜 로그인 응답 객체")
public class LoginResponse {
  private String email;
  private String accessToken;
  private String socialId;
  @JsonIgnore private String refreshToken;

  public LoginResponse(String email, String accessToken, String socialId) {
    this.email = email;
    this.accessToken = accessToken;
    this.socialId = socialId;
  }
}
