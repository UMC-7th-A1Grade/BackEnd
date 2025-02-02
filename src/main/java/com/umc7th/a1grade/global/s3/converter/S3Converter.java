package com.umc7th.a1grade.global.s3.converter;

import com.umc7th.a1grade.global.s3.dto.S3ResponseDTO;

public class S3Converter {

  public static S3ResponseDTO.ImgUrlDTO toImgUrlDTO(String imgUrl) {
    return S3ResponseDTO.ImgUrlDTO.builder().imageUrl(imgUrl).build();
  }
}
