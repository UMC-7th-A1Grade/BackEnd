package com.umc7th.a1grade.domain.user.service;

import java.util.List;

import com.umc7th.a1grade.domain.user.dto.*;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {

  void confirmNickName(UserDetails userDetails, String nickname);

  UserInfoResponseDto saveUserInfo(UserDetails userDetails, UserInfoRequestDto requestDto);

  UserGradeResponseDto findUserGrade(UserDetails userDetails);

  List<AllGradeResponseDto> findTop3UserGrade(UserDetails userDetails);

  UserCreditResponseDto findUserCredit(UserDetails userDetails);

  UserCreditResponseDto updateUserCredit(UserDetails userDetails);

  void logout(UserDetails userDetails);

  UserNicknameResponseDto getUserNickName(UserDetails userDetails);
}
