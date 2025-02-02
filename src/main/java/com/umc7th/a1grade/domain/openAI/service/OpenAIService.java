package com.umc7th.a1grade.domain.openAI.service;

import com.umc7th.a1grade.domain.openAI.exception.AIErrorStatus;
import com.umc7th.a1grade.global.exception.GeneralException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import com.umc7th.a1grade.domain.openAI.dto.OpenAIResponse.confirmQuestionResponse;
import com.umc7th.a1grade.domain.openAI.dto.OpenAIResponse.generateQuestionResponse;
import com.umc7th.a1grade.domain.openAI.infrastructure.ConfirmQuestionManager;
import com.umc7th.a1grade.domain.openAI.infrastructure.GenerateQuestionManager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
@Slf4j
public class OpenAIService {
  @Autowired private GenerateQuestionManager generateQuestionManager;
  @Autowired private ConfirmQuestionManager confirmQuestionManager;

  @Retryable(maxAttempts = 2, backoff = @Backoff(delay = 1000))
  public confirmQuestionResponse confirmQuestion(MultipartFile image) {

    try {
      validateFile(image);

      return confirmQuestionManager.confirmQuestion(image);
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
