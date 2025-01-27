package com.umc7th.a1grade.domain.question.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.umc7th.a1grade.domain.question.entity.mapping.QuestionReviewHistory;

public interface QuestionReviewHistoryRepository
    extends JpaRepository<QuestionReviewHistory, Long> {
  @Query(
      value =
          """
    SELECT *
    FROM question_review_history qrh
    WHERE qrh.user_question_id = :userQuestionId
""",
      nativeQuery = true)
  Optional<QuestionReviewHistory> findByUserQuestionId(
      @Param("userQuestionId") Long userQuestionId);

  @Modifying
  @Query(
      "UPDATE QuestionReviewHistory qrh "
          + "SET qrh.isForgotten = true "
          + "WHERE qrh.lastReviewedAt < :twoMonthsAgo "
          + "AND qrh.isForgotten = false")
  void updateForgottenStatus(@Param("twoMonthsAgo") LocalDateTime twoMonthsAgo);
}
