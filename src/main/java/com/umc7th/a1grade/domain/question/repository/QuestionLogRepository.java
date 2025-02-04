package com.umc7th.a1grade.domain.question.repository;

import java.time.LocalDateTime;
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
      "SELECT DISTINCT ql.userQuestion.id "
          + "FROM QuestionLog ql "
          + "WHERE ql.isCorrect = true AND ql.submissionTime < :start "
          + "AND ql.user.id = :userId")
  List<Long> findPreviouslySolvedQuestionIds(
      @Param("userId") Long userId, @Param("start") LocalDateTime start);

  @Query(
      "SELECT DISTINCT ql.userQuestion.id "
          + "FROM QuestionLog ql "
          + "JOIN ql.userQuestion uq "
          + "JOIN uq.user u "
          + "WHERE ql.isCorrect = true AND ql.submissionTime BETWEEN :start AND :end "
          + "AND u.id = :userId")
  List<Long> findYesterdaySolvedQuestionIds(
      @Param("userId") Long userId,
      @Param("start") LocalDateTime start,
      @Param("end") LocalDateTime end);

  @Query(
      "SELECT COUNT(ql.id) "
          + "FROM QuestionLog ql "
          + "WHERE ql.user.id = :userId "
          + "AND ql.submissionTime BETWEEN :start AND :end")
  long countTotalAttempts(Long userId, LocalDateTime start, LocalDateTime end);
}
