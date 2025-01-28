package com.umc7th.a1grade.domain.user.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.umc7th.a1grade.domain.user.exception.UserHandler;
import com.umc7th.a1grade.domain.user.exception.status.UserErrorStatus;
import com.umc7th.a1grade.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  public void confirmNickName(UserDetails userDetails, String nickname) {
    if (nickname == null || nickname.isBlank()) {
      throw new UserHandler(UserErrorStatus._USER_NICKNAME_NULL);
    }
    boolean isDuplicate = userRepository.existsByNickName(nickname);
    if (isDuplicate) {
      throw new UserHandler(UserErrorStatus._USER_NICKNAME_EXIST);
    }
  }
}
