package com.umc7th.a1grade.domain.ranking.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.umc7th.a1grade.domain.ranking.entity.Ranking;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RankingRepositoryImpl implements RankingRepository {
  private final RankingJpaRepository rankingJpaRepository;

  @Override
  public Ranking save(Ranking ranking) {
    return rankingJpaRepository.save(ranking);
  }

  @Override
  public void deleteByRankingDate(LocalDate date) {
    rankingJpaRepository.deleteByRankingDate(date);
  }

  @Override
  public void saveAll(List<Ranking> rankings) {
    rankingJpaRepository.saveAll(rankings);
  }

  @Override
  public List<Ranking> findAll() {
    return rankingJpaRepository.findAll();
  }
}
