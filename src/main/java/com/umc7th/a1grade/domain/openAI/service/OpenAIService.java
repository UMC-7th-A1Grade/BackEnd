package com.umc7th.a1grade.domain.openAI.service;

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

@RequiredArgsConstructor
@Service
@Slf4j
public class OpenAIService {
  @Autowired private GenerateQuestionManager generateQuestionManager;
  @Autowired private ConfirmQuestionManager confirmQuestionManager;

  @Retryable(maxAttempts = 2, backoff = @Backoff(delay = 1000))
  public confirmQuestionResponse confirmQuestion(String imageUrl) {
    return confirmQuestionManager.confirmQuestion(imageUrl);
  }

  @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 1000))
  public generateQuestionResponse generateQuestion(String imageUrl) {
    return generateQuestionManager.generateQuestion(imageUrl);
  }
}
