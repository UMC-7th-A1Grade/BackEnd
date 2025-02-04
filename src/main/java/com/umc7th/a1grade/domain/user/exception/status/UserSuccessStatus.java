package com.umc7th.a1grade.domain.user.exception.status;

import org.springframework.http.HttpStatus;

import com.umc7th.a1grade.global.apiPayload.code.BaseCode;
import com.umc7th.a1grade.global.apiPayload.code.ReasonDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserSuccessStatus implements BaseCode {
  _NICKNAME_OK(HttpStatus.OK, "NINKNAME2000", "사용가능한 닉네임입니다."),
  _USER_GRADE_OK(HttpStatus.OK, "GRADE20000", "사용자의 오답 정답 개수를 성공적으로 조회하였습니다."),
  _ALLUSER_GRADE_OK(HttpStatus.OK, "GRADE2001", "TOP 3명의 정답 오답 개수를 성공적으로 조회하였습니다."),
  _USER_INFO_UPDATE(HttpStatus.OK, "USERINFO2001", "닉네임과 캐릭터 정보가 성공적으로 저장되었습니다.");

  private final HttpStatus httpStatus;
  private final String code;
  private final String message;

  @Override
  public ReasonDTO getReason() {
    return ReasonDTO.builder().message(message).code(code).isSuccess(true).build();
  }

  @Override
  public ReasonDTO getReasonHttpStatus() {
    return ReasonDTO.builder()
        .message(message)
        .code(code)
        .isSuccess(true)
        .httpStatus(httpStatus)
        .build();
  }
}
