package com.umc7th.a1grade.domain.question.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.umc7th.a1grade.domain.question.entity.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {
  Optional<Question> findByNum(Integer num);

  // 1. 페이징 랜덤 선택
  @Query(
      value =
          "SELECT q.* FROM question q "
              + "JOIN user_question uq ON q.id = uq.question_id "
              + "WHERE uq.user_id = :userId "
              + "ORDER BY RAND()",
      countQuery =
          "SELECT COUNT(*) FROM question q "
              + "JOIN user_question uq ON q.id = uq.question_id "
              + "WHERE uq.user_id = :userId",
      nativeQuery = true)
  Page<Question> findRandomQuestionsByUserNative(@Param("userId") Long userId, Pageable pageable);

  // 2. LIMIT 사용 랜덤 선택
  @Query(
      value =
          "SELECT q.* FROM question q "
              + "JOIN user_question uq ON q.id = uq.question_id "
              + "WHERE uq.user_id = :userId "
              + "ORDER BY RAND() "
              + "LIMIT 3",
      nativeQuery = true)
  List<Question> findQuestionsByUserNative(@Param("userId") Long userId);

  // 3. Collection shuffle 사용
  @Query("SELECT q FROM Question q " + "JOIN q.userQuestions uq " + "WHERE uq.user.id = :userId")
  List<Question> findQuestionsByUser(@Param("userId") Long userId);

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
}
