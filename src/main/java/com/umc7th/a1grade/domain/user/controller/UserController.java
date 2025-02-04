package com.umc7th.a1grade.domain.user.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.umc7th.a1grade.domain.user.dto.AllGradeResponseDto;
import com.umc7th.a1grade.domain.user.dto.UserGradeResponseDto;
import com.umc7th.a1grade.domain.user.dto.UserInfoRequestDto;
import com.umc7th.a1grade.domain.user.dto.UserInfoResponseDto;
import com.umc7th.a1grade.domain.user.exception.status.UserErrorStatus;
import com.umc7th.a1grade.domain.user.exception.status.UserSuccessStatus;
import com.umc7th.a1grade.domain.user.service.UserService;
import com.umc7th.a1grade.global.annotation.ApiErrorCodeExample;
import com.umc7th.a1grade.global.apiPayload.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "user", description = "회원 관리 및 마이페이지 관련 API")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @Operation(
      summary = "닉네임 중복 확인",
      description = """
        사용자가 입력한 닉네임이 이미 존재하는지 확인합니다.
        """)
  @Parameters({
    @Parameter(
        name = "nickname",
        description = "중복 확인을 요청할 닉네임",
        required = true,
        example = "testUser")
  })
  @ApiErrorCodeExample(UserErrorStatus.class)
  @GetMapping(value = "", produces = "application/json")
  public ApiResponse<Boolean> confirmNickName(
      @AuthenticationPrincipal UserDetails userDetails, @RequestParam("nickname") String nickname) {
    userService.confirmNickName(userDetails, nickname);
    return ApiResponse.of(UserSuccessStatus._NICKNAME_OK, true);
  }

  @Operation(
      summary = "닉네임 및 선택한 캐릭터 정보 저장",
      description = """
      사용자에게 입력받은 닉네임과 캐릭터 Id 를 저장합니다.
      """,
      requestBody =
          @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "사용자가 입력한 닉네임 및 캐릭터 아이디 정보",
              required = true,
              content =
                  @Content(
                      mediaType = "application/json",
                      schema = @Schema(implementation = UserInfoRequestDto.class))))
  @ApiErrorCodeExample(UserErrorStatus.class)
  @PatchMapping(value = "", consumes = "application/json", produces = "application/json")
  public ApiResponse<UserInfoResponseDto> saveUserInfo(
      @AuthenticationPrincipal UserDetails userDetails,
      @RequestBody @Valid UserInfoRequestDto requestDto) {
    UserInfoResponseDto response = userService.saveUserInfo(userDetails, requestDto);
    return ApiResponse.of(UserSuccessStatus._USER_INFO_UPDATE, response);
  }

  @Operation(summary = "오늘의 정답률", description = """
      사용자의 닉네임과 오답 정답의 개수를 조회합니다. \n
      """)
  @ApiErrorCodeExample(UserErrorStatus.class)
  @GetMapping(value = "/grade", produces = "application/json")
  public ApiResponse<UserGradeResponseDto> findUserGrade(
      @AuthenticationPrincipal UserDetails userDetails) {
    UserGradeResponseDto response = userService.findUserGrade(userDetails);
    return ApiResponse.of(UserSuccessStatus._USER_GRADE_OK, response);
  }

  @Operation(
      summary = "1등급 경쟁 랭킹 조회",
      description = """
      오답정답의 개수가 가장 많은 TOP 3 사용자의 정보를 조회합니다.  \n
      """)
  @ApiErrorCodeExample(UserErrorStatus.class)
  @GetMapping(value = "/allgrade", produces = "application/json")
  public ApiResponse<List<AllGradeResponseDto>> findTop3UserGrade(
      @AuthenticationPrincipal UserDetails userDetails) {

    List<AllGradeResponseDto> response = userService.findTop3UserGrade(userDetails);
    return ApiResponse.of(UserSuccessStatus._ALLUSER_GRADE_OK, response);
  }
}
