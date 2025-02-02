package com.umc7th.a1grade.domain.openAI.service;

import static com.umc7th.a1grade.global.s3.entity.PathName.AI_QUESTION;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.umc7th.a1grade.domain.openAI.dto.OpenAIResponse.confirmQuestionResponse;
import com.umc7th.a1grade.domain.openAI.dto.OpenAIResponse.generateQuestionResponse;
import com.umc7th.a1grade.domain.openAI.exception.AIErrorStatus;
import com.umc7th.a1grade.domain.openAI.infrastructure.ConfirmQuestionManager;
import com.umc7th.a1grade.domain.openAI.infrastructure.GenerateQuestionManager;
import com.umc7th.a1grade.global.exception.GeneralException;
import com.umc7th.a1grade.global.s3.dto.S3ResponseDTO;
import com.umc7th.a1grade.global.s3.service.S3Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class OpenAIService {
  @Autowired private GenerateQuestionManager generateQuestionManager;
  @Autowired private ConfirmQuestionManager confirmQuestionManager;
  private final S3Service s3Service;

  @Retryable(maxAttempts = 2, backoff = @Backoff(delay = 1000))
  public confirmQuestionResponse confirmQuestion(MultipartFile image) {

    try {
      validateFile(image);

      if (confirmQuestionManager.confirmQuestion(image).getResult()) {
        S3ResponseDTO.ImgUrlDTO response = s3Service.ImgUpload(AI_QUESTION, image);

        return confirmQuestionResponse
            .builder()
            .result(true)
            .imageUrl(response.getImageUrl())
            .build();
      }
      return confirmQuestionResponse.builder().result(false).build();
    } catch (Exception e) {
      throw new GeneralException(AIErrorStatus._FILE_SERVER_ERROR);
    }
  }

  @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 1000))
  public generateQuestionResponse generateQuestion(String imageUrl) {
    return generateQuestionManager.generateQuestion(imageUrl);
  }

  private void validateFile(MultipartFile file) {
    if (file.isEmpty()) {
      throw new GeneralException(AIErrorStatus._FILE_NOT_FOUND);
    }

    if (file.getSize() > 5 * 1024 * 1024) {
      throw new GeneralException(AIErrorStatus._FILE_SIZE_INVALID);
    }

    String contentType = file.getContentType();
    if (contentType == null || !contentType.startsWith("image/")) {
      throw new GeneralException(AIErrorStatus._FILE_TYPE_INVALID);
    }
  }
}
