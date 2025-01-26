package com.umc7th.a1grade.domain.openAI.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
  public confirmQuestionResponse confirmQuestion(MultipartFile image) {
    try {
      log.info("[POST /api/open-ai/confirm] 수학 문제 판별 성공");
      return confirmQuestionManager.confirmQuestion(image);
    } catch (Exception e) {
      log.error("[POST /api/open-ai/confirm] 수학 문제 판별 실패 - {}", e.getMessage());
      return confirmQuestionResponse.builder().success(false).build();
    }
  }

  @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 1000))
  public generateQuestionResponse generateQuestion(MultipartFile image) {
    try {
      log.error("[POST /api/open-ai/generate] 유사 문제 생성 성공");
      return generateQuestionManager.generateQuestion(image);
    } catch (Exception e) {
      log.error("[POST /api/open-ai/generate] 유사 문제 생성 실패 - {}", e.getMessage());
      return generateQuestionResponse.builder().message("[서버 오류] 유사 문제 생성에 실패하였습니다.").build();
    }
  }
}
