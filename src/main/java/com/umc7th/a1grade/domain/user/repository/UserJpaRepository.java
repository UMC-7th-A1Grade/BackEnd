package com.umc7th.a1grade.domain.user.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.umc7th.a1grade.domain.user.entity.User;

public interface UserJpaRepository extends JpaRepository<User, Long> {
  Optional<User> findByNickName(String nickname);

  Optional<User> findBySocialId(String socialId);

  boolean existsByNickName(String nickname);

  @Query(
      value =
          "SELECT COUNT(DISTINCT uq.question_id) "
              + "FROM question_log ql "
              + "JOIN user_question uq ON ql.user_question_Id = uq.id "
              + "WHERE ql.is_correct = TRUE AND uq.user_id = :userId",
      nativeQuery = true)
  Long countCorrectAnswerByUserId(@Param("userId") Long userId);

  @Query(
      "SELECT u FROM User u "
          + "WHERE u.id IN ("
          + "    SELECT ql.user.id "
          + "    FROM QuestionLog ql "
          + "    WHERE ql.isCorrect = true "
          + "    GROUP BY ql.user.id "
          + "    HAVING COUNT(DISTINCT ql.userQuestion.id) >= 10)")
  List<User> findUserWithCorrectAnswers();

  @Query(
      "SELECT DISTINCT ql.user FROM QuestionLog ql "
          + "WHERE ql.submissionTime "
          + "BETWEEN :start AND :end")
  List<User> findUsersWhoSolvedQuestions(
      @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
