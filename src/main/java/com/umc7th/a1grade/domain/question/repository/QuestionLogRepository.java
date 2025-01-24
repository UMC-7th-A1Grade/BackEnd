package com.umc7th.a1grade.domain.question.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.umc7th.a1grade.domain.question.entity.mapping.QuestionLog;

public interface QuestionLogRepository extends JpaRepository<QuestionLog, Long> {
  @Query(
      "SELECT ql.memo FROM QuestionLog ql "
          + "WHERE ql.user.id = :userId "
          + "AND ql.userQuestion.question.id = :questionId "
          + "ORDER BY ql.submissionTime DESC")
  Optional<String> findLatestMemoByUserAndQuestion(
      @Param("userId") Long userId, @Param("questionId") Long questionId);

  @Query(
      "SELECT DISTINCT uq.question.id FROM QuestionLog ql "
          + "JOIN ql.userQuestion uq "
          + "WHERE uq.question.id IN :questionIds")
  Set<Long> findQuestionIdsWithLogs(@Param("questionIds") List<Long> questionIds);
}
