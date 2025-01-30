package com.umc7th.a1grade.domain.character.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.umc7th.a1grade.domain.character.dto.CharacterListResponseDto;
import com.umc7th.a1grade.domain.character.exception.status.CharacterSuccessStatus;
import com.umc7th.a1grade.domain.character.service.CharacterService;
import com.umc7th.a1grade.global.apiPayload.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "characters", description = "캐릭터 관련 API")
@RestController
@RequestMapping("/api/characters")
@RequiredArgsConstructor
public class CharacterController {
  private final CharacterService characterService;

  @Operation(summary = "캐릭터 정보 조회", description = "캐릭터의 정보를 전부 조회합니다.")
  @ApiResponses({
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "200",
        description = "캐릭터 정보조회에 성공했습니다.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CharacterListResponseDto.class)))
  })
  @GetMapping("")
  public ApiResponse<CharacterListResponseDto> findAllCharacters(
      @AuthenticationPrincipal UserDetails userDetails) {
    CharacterListResponseDto response = characterService.findAll();
    return ApiResponse.of(CharacterSuccessStatus._INFO_OK, response);
  }
}
