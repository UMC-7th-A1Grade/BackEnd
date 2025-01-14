package com.umc7th.a1grade.domain.auth.dto;

import java.util.Map;

import com.umc7th.a1grade.domain.user.entity.User;

import lombok.Builder;
import lombok.Getter;

@Getter
public class OAuthAttributes {
  private Map<String, Object> attributes;
  private String sub;
  private String email;

  @Builder
  public OAuthAttributes(Map<String, Object> attributes, String sub, String email) {
    this.attributes = attributes;
    this.sub = sub;
    this.email = email;
  }

  public User toEntity() {
    return User.builder().socialId(sub).email(email).build();
  }
}
