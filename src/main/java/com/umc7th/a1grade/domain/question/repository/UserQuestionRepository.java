package com.umc7th.a1grade.domain.question.repository;

import java.util.List;
import java.util.Optional;

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
      "SELECT uq FROM UserQuestion uq WHERE uq.user.id = :user_id AND uq.question.type = :question_type")
  List<UserQuestion> findByUserIdAndQuestionType(
      @Param("userId") Long userId, @Param("questionType") QuestionType questionType);

  List<UserQuestion> findByUserId(Long id);
}
