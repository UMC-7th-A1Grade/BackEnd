package com.umc7th.a1grade.domain.user.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {

  void confirmNickName(UserDetails userDetails, String nickname);
}
