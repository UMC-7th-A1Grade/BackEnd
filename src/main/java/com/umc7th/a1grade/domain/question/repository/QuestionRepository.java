package com.umc7th.a1grade.domain.question.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.umc7th.a1grade.domain.question.entity.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {
  Optional<Question> findByNum(Integer num);

  @Query(
      value =
          "SELECT q.* FROM question q "
              + "JOIN user_question uq ON q.id = uq.question_id "
              + "WHERE uq.user_id = :userId "
              + "AND q.type = 'USER'"
              + "ORDER BY RAND() "
              + "LIMIT 3",
      nativeQuery = true)
  List<Question> findQuestionsByUserAndType(@Param("userId") Long userId);

  @Query(
      value =
          "SELECT q.* FROM question q "
              + "JOIN user_question uq ON q.id = uq.question_id "
              + "WHERE uq.user_id = :userId "
              + "ORDER BY q.created_date DESC "
              + "LIMIT 5",
      nativeQuery = true)
  List<Question> findRecentQuestion(@Param("userId") Long userId);
}
