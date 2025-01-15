package com.umc7th.a1grade.domain.auth.controller.response;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
