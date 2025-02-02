package com.umc7th.a1grade.domain.question.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.umc7th.a1grade.domain.question.entity.QuestionType;
import com.umc7th.a1grade.domain.question.entity.mapping.UserQuestion;

public interface UserQuestionRepository extends JpaRepository<UserQuestion, Long> {
  @Query(
      "SELECT uq FROM UserQuestion uq WHERE uq.user.id = :userId AND uq.question.id = :questionId")
  Optional<UserQuestion> findByUserIdAndQuestionId(
      @Param("userId") Long userId, @Param("questionId") Long questionId);

  @Query(
      "SELECT uq FROM UserQuestion uq "
          + "JOIN FETCH uq.question q "
          + "WHERE uq.user.id = :userId "
          + "ORDER BY q.createdDate DESC")
  List<UserQuestion> findRecentQuestions(@Param("userId") Long userId, Pageable pageable);

  @Query(
      "SELECT uq FROM UserQuestion uq "
          + "JOIN FETCH uq.question q "
          + "LEFT JOIN FETCH uq.questionLogs ql "
          + "WHERE uq.id = :userQuestionId "
          + "ORDER BY ql.submissionTime DESC")
  Optional<UserQuestion> findUserQuestion(@Param("userQuestionId") Long userQuestionId);
}