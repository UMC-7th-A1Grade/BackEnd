package com.umc7th.a1grade.domain.question.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.umc7th.a1grade.domain.question.entity.mapping.UserQuestion;

public interface UserQuestionRepository extends JpaRepository<UserQuestion, Long> {
  @Query(
      "SELECT uq FROM UserQuestion uq WHERE uq.user.id = :userId AND uq.question.id = :questionId")
  Optional<UserQuestion> findByUserIdAndQuestionId(
      @Param("userId") Long userId, @Param("questionId") Long questionId);
}
