package com.umc7th.a1grade.domain.question.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.umc7th.a1grade.domain.question.entity.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {
  Optional<Question> findById(Long id);

  @Query(
      value =
          """
        SELECT q.*
        FROM question q
        JOIN user_question uq ON q.id = uq.question_id
        LEFT JOIN question_log ql ON uq.id = ql.user_question_id
        WHERE uq.user_id = :userId
        GROUP BY q.id
        HAVING (
            MAX(ql.submission_time) IS NULL
            OR MAX(ql.submission_time) + INTERVAL 2 MONTH >= NOW()
        )
        AND (
            COUNT(ql.id) = 0
            OR (COUNT(ql.id) = 1 AND MAX(ql.submission_time) + INTERVAL 10 MINUTE <= NOW())
            OR (COUNT(ql.id) = 2 AND MAX(ql.submission_time) + INTERVAL 1 DAY <= NOW())
            OR (COUNT(ql.id) = 3 AND MAX(ql.submission_time) + INTERVAL 1 WEEK <= NOW())
            OR (COUNT(ql.id) = 4 AND MAX(ql.submission_time) + INTERVAL 1 MONTH <= NOW())
            OR (COUNT(ql.id) = 5 AND MAX(ql.submission_time) + INTERVAL 2 MONTH <= NOW())
        )
        AND q.id >= (SELECT FLOOR(RAND() * (SELECT MAX(id) FROM question)))
        LIMIT 3
        """,
      nativeQuery = true)

  // QuestionHistory를 사용하지 않고 questionLog submission_time과 count 함수를 사용하여 계산하는걸로 쿼리문 변경
  // CASE 문으로 2달이 넘은 문제는 count 6으로 변경하여 조회되지 않도록 변경
  // ID 값만 rand()함수를 적용하여 쿼리 최적화 진행
  // TIMESTAMPDIFF 대신에 INTERVAL을 사용하여 가독성 개선
  List<Question> findQuestionsByUserAndType(@Param("userId") Long userId);

  Optional<Question> findByImageUrlAndMemo(String imageUrl, String memo);

  @Query(
      value =
          """
        SELECT q.*
        FROM question q
        JOIN user_question uq ON q.id = uq.question_id
        LEFT JOIN question_log ql ON uq.id = ql.user_question_id
        WHERE uq.user_id = :userId
        GROUP BY q.id
        HAVING (
            MAX(ql.submission_time) IS NULL
            OR MAX(ql.submission_time) + INTERVAL 2 MONTH >= NOW()
        )
        AND (
            COUNT(ql.id) = 0
            OR (COUNT(ql.id) = 1 AND MAX(ql.submission_time) + INTERVAL 10 MINUTE <= NOW())
            OR (COUNT(ql.id) = 2 AND MAX(ql.submission_time) + INTERVAL 1 DAY <= NOW())
            OR (COUNT(ql.id) = 3 AND MAX(ql.submission_time) + INTERVAL 1 WEEK <= NOW())
            OR (COUNT(ql.id) = 4 AND MAX(ql.submission_time) + INTERVAL 1 MONTH <= NOW())
            OR (COUNT(ql.id) = 5 AND MAX(ql.submission_time) + INTERVAL 2 MONTH <= NOW())
        )
        ORDER BY RAND()
        LIMIT 3
        """,
      nativeQuery = true)
  List<Question> testFindQuestionsByUserAndType(@Param("userId") Long userId);
}
