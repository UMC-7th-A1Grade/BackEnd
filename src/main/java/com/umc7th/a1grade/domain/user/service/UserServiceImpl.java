package com.umc7th.a1grade.domain.user.service;

import org.springframework.stereotype.Service;

import com.umc7th.a1grade.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
}
