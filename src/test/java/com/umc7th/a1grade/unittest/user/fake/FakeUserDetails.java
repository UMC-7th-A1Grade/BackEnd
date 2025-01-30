package com.umc7th.a1grade.unittest.user.fake;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class FakeUserDetails implements UserDetails {
  private final String socialId;

  public FakeUserDetails(String socialId) {
    this.socialId = socialId;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of();
  }

  @Override
  public String getPassword() {
    return null;
  }

  @Override
  public String getUsername() {
    return socialId;
  }
}
