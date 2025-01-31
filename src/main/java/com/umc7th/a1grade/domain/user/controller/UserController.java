package com.umc7th.a1grade.domain.user.controller;

import jakarta.validation.Valid;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.umc7th.a1grade.domain.user.dto.MypageResponseDto;
import com.umc7th.a1grade.domain.user.dto.UserInfoDto;
import com.umc7th.a1grade.domain.user.dto.UserInfoResponseDto;
import com.umc7th.a1grade.domain.user.exception.status.UserSuccessStatus;
import com.umc7th.a1grade.domain.user.service.UserService;
import com.umc7th.a1grade.global.apiPayload.ApiResponse;
import com.umc7th.a1grade.global.apiPayload.code.ErrorReasonDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
      description = "사용자가 입력한 닉네임이 이미 존재하는지 확인합니다.",
      parameters = {
        @Parameter(
            name = "nickname",
            description = "중복 확인을 요청할 닉네임",
            required = true,
            example = "testUser")
      })
  @ApiResponses({
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "200",
        description = "사용가능한 닉네임입니다.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(type = "boolean", example = "true"))),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "400",
        description = "이미 존재하는 닉네임입니다.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorReasonDTO.class)))
  })
  @GetMapping("")
  public ApiResponse<Boolean> confirmNickName(
      @AuthenticationPrincipal UserDetails userDetails, @RequestParam("nickname") String nickname) {
    userService.confirmNickName(userDetails, nickname);
    return ApiResponse.of(UserSuccessStatus._NICKNAME_OK, true);
  }

  @Operation(
      summary = "닉네임 및 선택한 캐릭터 정보 저장",
      description = "사용자에게 입력받은 닉네임과 캐릭터 Id 를 저장합니다.",
      requestBody =
          @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "사용자가 입력한 닉네임 및 캐릭터 아이디 정보",
              required = true,
              content =
                  @Content(
                      mediaType = "application/json",
                      schema = @Schema(implementation = UserInfoDto.class))))
  @ApiResponses({
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "200",
        description = "닉네임과 캐릭터 정보가 성공적으로 저장되었습니다.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UserInfoResponseDto.class))),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "400",
        description = "존재하지 않은 캐릭터 아이디 입니다.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorReasonDTO.class)))
  })
  @PatchMapping("")
  public ApiResponse<UserInfoResponseDto> saveUserInfo(
      @AuthenticationPrincipal UserDetails userDetails,
      @RequestBody @Valid UserInfoDto requestDto) {
    UserInfoResponseDto response = userService.saveUserInfo(userDetails, requestDto);
    return ApiResponse.of(UserSuccessStatus._USER_INFO_UPDATE, response);
  }

  @Operation(summary = "오늘의 정답률", description = "사용자의 닉네임과 오답 정답의 개수를 조회합니다.")
  @ApiResponses({
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "200",
        description = "사용자의 오답 정답 개수를 성공적으로 조회하였습니다.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = MypageResponseDto.class)))
  })
  @GetMapping("/grade")
  public ApiResponse<MypageResponseDto> findUserGrade(
      @AuthenticationPrincipal UserDetails userDetails) {
    MypageResponseDto response = userService.findUserGrade(userDetails);
    return ApiResponse.of(UserSuccessStatus._USER_GRADE_OK, response);
  }
}
