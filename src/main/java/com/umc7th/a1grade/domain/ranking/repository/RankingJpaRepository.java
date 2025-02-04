package com.umc7th.a1grade.domain.ranking.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.umc7th.a1grade.domain.ranking.entity.Ranking;

public interface RankingJpaRepository extends JpaRepository<Ranking, Long> {
  @Modifying
  @Query("DELETE FROM Ranking r WHERE r.rankingDate = :rankingDate")
  void deleteByRankingDate(@Param("rankingDate") LocalDate rankingDate);
}
