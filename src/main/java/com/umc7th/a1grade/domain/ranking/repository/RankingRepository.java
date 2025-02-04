package com.umc7th.a1grade.domain.ranking.repository;

import java.time.LocalDate;
import java.util.List;

import com.umc7th.a1grade.domain.ranking.entity.Ranking;

public interface RankingRepository {

  Ranking save(Ranking ranking);

  void deleteByRankingDate(LocalDate rankingDate);

  void saveAll(List<Ranking> rankings);

  List<Ranking> findAll();
}
