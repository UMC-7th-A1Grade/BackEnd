package com.umc7th.a1grade.global.s3.service;

import java.io.ByteArrayInputStream;
import java.util.Base64;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.umc7th.a1grade.global.config.S3Config;
import com.umc7th.a1grade.global.exception.GeneralException;
import com.umc7th.a1grade.global.s3.converter.S3Converter;
import com.umc7th.a1grade.global.s3.dto.S3ResponseDTO;
import com.umc7th.a1grade.global.s3.entity.PathName;
import com.umc7th.a1grade.global.s3.exception.status.S3ErrorStatus;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class S3Service {

  private final AmazonS3 amazonS3;
  private final S3Config s3Config;

  public String uploadFile(PathName pathName, MultipartFile file) {

    validateFile(file);

    String keyName = createKeyName(pathName);

    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentLength(file.getSize());
    metadata.setContentType(file.getContentType());

    try {
      amazonS3.putObject(
          new PutObjectRequest(s3Config.getBucket(), keyName, file.getInputStream(), metadata));
      return amazonS3.getUrl(s3Config.getBucket(), keyName).toString();
    } catch (Exception e) {
      throw new GeneralException(S3ErrorStatus._FILE_SERVER_ERROR);
    }
  }

  public String base64UploadFile(PathName pathName, String base64Url) {
    if (!validateBase64(base64Url)) {
      throw new GeneralException(S3ErrorStatus._INVALID_BASE64);
    }

    String base64Data = base64Url;
    String contentType = "image/png";

    if (base64Url.contains(",")) {
      String[] parts = base64Url.split(",");
      if (parts[0].contains("data:") && parts[0].contains(";base64")) {
        contentType = parts[0].substring(5, parts[0].indexOf(";"));
      }
      base64Data = parts[1];
    }

    byte[] decodedBytes = Base64.getDecoder().decode(base64Data);
    String keyName = createKeyName(pathName);

    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentLength(decodedBytes.length);
    metadata.setContentType(contentType);

    try (ByteArrayInputStream inputStream = new ByteArrayInputStream(decodedBytes)) {
      amazonS3.putObject(
          new PutObjectRequest(s3Config.getBucket(), keyName, inputStream, metadata));
      return amazonS3.getUrl(s3Config.getBucket(), keyName).toString();
    } catch (Exception e) {
      throw new GeneralException(S3ErrorStatus._FILE_SERVER_ERROR);
    }
  }

  public String createKeyName(PathName pathName) {

    return switch (pathName) {
          case AI_QUESTION -> s3Config.getAiQuestionPath();
          case USER_QUESTION -> s3Config.getUserQuestionPath();
          case NOTE -> s3Config.getNotePath();
          case MEMO -> s3Config.getMemoPath();
          case CHARACTER -> s3Config.getCharacterPath();
        }
        + '/'
        + UUID.randomUUID().toString();
  }

  public String getFileUrl(String keyName) {
    existFile(keyName);

    try {
      String fileUrl = amazonS3.getUrl(s3Config.getBucket(), keyName).toString();
      return fileUrl;
    } catch (Exception e) {
      throw new GeneralException(S3ErrorStatus._FILE_SERVER_ERROR);
    }
  }

  public void deleteFile(String keyName) {
    existFile(keyName);

    try {
      amazonS3.deleteObject(new DeleteObjectRequest(s3Config.getBucket(), keyName));
    } catch (Exception e) {
      throw new GeneralException(S3ErrorStatus._FILE_SERVER_ERROR);
    }
  }

  private void existFile(String keyName) {
    if (!amazonS3.doesObjectExist(s3Config.getBucket(), keyName)) {
      throw new GeneralException(S3ErrorStatus._FILE_NOT_FOUND);
    }
  }

  private void validateFile(MultipartFile file) {
    if (file.getSize() > 5 * 1024 * 1024) {
      throw new GeneralException(S3ErrorStatus._FILE_SIZE_INVALID);
    }

    String contentType = file.getContentType();
    if (contentType == null || !contentType.startsWith("image/")) {
      throw new GeneralException(S3ErrorStatus._FILE_TYPE_INVALID);
    }
  }

  private boolean validateBase64(String base64Data) {
    return base64Data != null && !base64Data.trim().isEmpty();
  }

  public S3ResponseDTO.ImgUrlDTO ImgUpload(PathName pathName, MultipartFile file) {
    String imgUrl = uploadFile(pathName, file);

    return S3Converter.toImgUrlDTO(imgUrl);
  }
}
