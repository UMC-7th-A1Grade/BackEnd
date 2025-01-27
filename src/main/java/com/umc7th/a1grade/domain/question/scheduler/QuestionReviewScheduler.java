package com.umc7th.a1grade.domain.question.scheduler;

import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.umc7th.a1grade.domain.question.repository.QuestionReviewHistoryRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class QuestionReviewScheduler {

  private final QuestionReviewHistoryRepository questionReviewHistoryRepository;

  @Scheduled(cron = "0 0 0 * * *") // 매일 자정에 체크하도록 설정
  public void updateForgottenQuestions() {
    LocalDateTime twoMonthsAgo = LocalDateTime.now().minusMonths(2); // 2달전인지 계산

    questionReviewHistoryRepository.updateForgottenStatus(
        twoMonthsAgo); // isForgotten을 true로 설정하여 다시 조회 안되도록 함
  }
}
