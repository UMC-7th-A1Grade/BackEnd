package com.umc7th.a1grade.domain.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GoogleTokenResponse {
  @JsonProperty("access_token")
  private String accessToken;

  @JsonProperty("expires_in")
  private int expiresIn;

  @JsonProperty("refresh_token")
  private String refreshToken;

  @JsonProperty("scope")
  private String scope;

  @JsonProperty("token_type")
  private String tokenType;

  @JsonProperty("id_token")
  private String idToken;
}
