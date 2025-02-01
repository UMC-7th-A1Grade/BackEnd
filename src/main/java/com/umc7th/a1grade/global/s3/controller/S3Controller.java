package com.umc7th.a1grade.global.s3.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.umc7th.a1grade.global.annotation.ApiErrorCodeExample;
import com.umc7th.a1grade.global.apiPayload.ApiResponse;
import com.umc7th.a1grade.global.s3.dto.S3ResponseDTO;
import com.umc7th.a1grade.global.s3.entity.PathName;
import com.umc7th.a1grade.global.s3.exception.status.S3ErrorStatus;
import com.umc7th.a1grade.global.s3.service.S3Service;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "S3-controller", description = "이미지 관리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/s3")
public class S3Controller {

  private final S3Service s3Service;

  @Operation(summary = "이미지 업로드 API", description = "이미지를 업로드하고 URL을 리턴받는 API")
  @ApiErrorCodeExample(S3ErrorStatus.class)
  @PostMapping(value = "/image-upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ApiResponse<S3ResponseDTO.ImgUrlDTO> uploadImage(
      @RequestParam PathName pathName, MultipartFile file) {

    S3ResponseDTO.ImgUrlDTO response = s3Service.ImgUpload(pathName, file);
    return ApiResponse.onSuccess(response);
  }
}
