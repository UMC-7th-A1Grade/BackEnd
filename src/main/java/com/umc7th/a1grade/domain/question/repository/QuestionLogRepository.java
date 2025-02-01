package com.umc7th.a1grade.domain.question.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.umc7th.a1grade.domain.question.entity.mapping.QuestionLog;

public interface QuestionLogRepository extends JpaRepository<QuestionLog, Long> {

  @Query(
      "SELECT DISTINCT uq.question.id FROM QuestionLog ql "
          + "JOIN ql.userQuestion uq "
          + "WHERE uq.question.id IN :questionIds")
  Set<Long> findQuestionIdsWithLogs(@Param("questionIds") List<Long> questionIds);

  @Query(
      "SELECT ql FROM QuestionLog ql "
          + "JOIN ql.userQuestion uq "
          + "WHERE uq.user.id = :userId AND uq.question.id = :questionId"
          + " ORDER BY ql.submissionTime DESC")
  List<QuestionLog> findByUserIdAndQuestionId(
      @Param("userId") Long userId, @Param("questionId") Long questionId);
}
