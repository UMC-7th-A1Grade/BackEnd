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
        WITH ReviewableQuestions AS (
            SELECT
                q.id,
                MAX(ql.submission_time) AS last_submission,
                CASE
                    WHEN MAX(ql.submission_time) IS NULL THEN 0
                    WHEN TIMESTAMPDIFF(MONTH, MAX(ql.submission_time), NOW()) >= 2 THEN 6
                    ELSE COUNT(ql.id)
                END AS attempt_count
            FROM question q
            JOIN user_question uq ON q.id = uq.question_id
            LEFT JOIN question_log ql ON uq.id = ql.user_question_id
            WHERE uq.user_id = :userId
            GROUP BY q.id
            HAVING (
                attempt_count = 0
                OR (attempt_count = 1 AND last_submission + INTERVAL 10 MINUTE <= NOW())
                OR (attempt_count = 2 AND last_submission + INTERVAL 1 DAY <= NOW())
                OR (attempt_count = 3 AND last_submission + INTERVAL 1 WEEK <= NOW())
                OR (attempt_count = 4 AND last_submission + INTERVAL 1 MONTH <= NOW())
                OR (attempt_count = 5 AND last_submission + INTERVAL 2 MONTH <= NOW())
            )
        ),
        RandomIds AS (
            SELECT id
            FROM ReviewableQuestions
            WHERE attempt_count < 6
            ORDER BY RAND()
            LIMIT 3
        )
        SELECT q.*
        FROM question q
        JOIN RandomIds r ON q.id = r.id
        """,
      nativeQuery = true)
  // QuestionHistory를 사용하지 않고 questionLog submission_time과 count 함수를 사용하여 계산하는걸로 쿼리문 변경
  // CASE 문으로 2달이 넘은 문제는 count 6으로 변경하여 조회되지 않도록 변경
  // ID 값만 rand()함수를 적용하여 쿼리 최적화 진행
  // TIMESTAMPDIFF 대신에 INTERVAL을 사용하여 가독성 개선
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
