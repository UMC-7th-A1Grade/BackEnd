package com.umc7th.a1grade.domain.user.service;

import org.springframework.security.core.userdetails.UserDetails;

import com.umc7th.a1grade.domain.user.dto.UserInfoDto;
import com.umc7th.a1grade.domain.user.dto.UserInfoResponseDto;

public interface UserService {

  void confirmNickName(UserDetails userDetails, String nickname);

  UserInfoResponseDto saveUserInfo(UserDetails userDetails, UserInfoDto requestDto);
}
