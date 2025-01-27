package com.umc7th.a1grade.global.s3;

import java.io.IOException;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.umc7th.a1grade.global.config.S3Config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3Service {

  private final AmazonS3 amazonS3;

  private final S3Config s3Config;

  public String uploadFile(String keyName, MultipartFile file) {
    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentLength(file.getSize());
    try {
      amazonS3.putObject(
          new PutObjectRequest(s3Config.getBucket(), keyName, file.getInputStream(), metadata));
    } catch (IOException e) {
      log.error("error at AmazonS3Manager uploadFile : {}", (Object) e.getStackTrace());
    }

    return amazonS3.getUrl(s3Config.getBucket(), keyName).toString();
  }

  public String generateKeyNameWithPathName(PathName pathName) {

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

  public boolean deleteFile(String keyName) {
    try {
      amazonS3.deleteObject(new DeleteObjectRequest(s3Config.getBucket(), keyName));
      log.info("File deleted successfully from S3: {}", keyName);
      return true;
    } catch (AmazonServiceException e) {
      log.error("Error deleting file from S3: {}", e.getErrorMessage());
    } catch (SdkClientException e) {
      log.error("Error deleting file from S3: {}", e.getMessage());
    }
    return false;
  }
}
