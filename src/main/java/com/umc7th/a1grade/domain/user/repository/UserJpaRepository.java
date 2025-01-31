package com.umc7th.a1grade.domain.user.repository;

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
}
