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
          """
    SELECT q.*
    FROM question q
    JOIN user_question uq ON q.id = uq.question_id
    LEFT JOIN question_review_history qrh ON uq.id = qrh.user_question_id
    WHERE uq.user_id = :userId
      AND (
        qrh.id IS NULL OR (
          qrh.is_forgotten = false
          AND (
            (qrh.review_count = 1 AND TIMESTAMPDIFF(MINUTE, qrh.last_reviewed_at, NOW()) >= 10)
            OR (qrh.review_count = 2 AND TIMESTAMPDIFF(DAY, qrh.last_reviewed_at, NOW()) >= 1)
            OR (qrh.review_count = 3 AND TIMESTAMPDIFF(WEEK, qrh.last_reviewed_at, NOW()) >= 1)
            OR (qrh.review_count = 4 AND TIMESTAMPDIFF(MONTH, qrh.last_reviewed_at, NOW()) >= 1)
            OR (qrh.review_count = 5 AND TIMESTAMPDIFF(MONTH, qrh.last_reviewed_at, NOW()) >= 2)
          )
        )
      )
    ORDER BY RAND()
    LIMIT 3
""",
      nativeQuery = true)
  // questionReviewHistory가 null일 경우 (한번도 풀지 않았을 경우에 조회될 수 있도록)
  // reviewCount 가 1이면 마지막 풀이 시간이 10분 미만인 문제가 나오지 않도록 (1일, 1주, 1개월, 2개월로 체크)
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
