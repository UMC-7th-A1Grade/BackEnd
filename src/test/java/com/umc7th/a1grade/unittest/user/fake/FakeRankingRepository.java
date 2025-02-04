package com.umc7th.a1grade.unittest.user.fake;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import com.umc7th.a1grade.domain.ranking.entity.Ranking;
import com.umc7th.a1grade.domain.ranking.repository.RankingRepository;

public class FakeRankingRepository implements RankingRepository {

  private final AtomicLong autoGeneratedId = new AtomicLong(1);
  private final List<Ranking> data = new ArrayList<>();

  @Override
  public Ranking save(Ranking ranking) {
    return null;
  }

  @Override
  public void deleteByRankingDate(LocalDate rankingDate) {}

  @Override
  public void saveAll(List<Ranking> rankings) {}

  @Override
  public List<Ranking> findAll() {
    return new ArrayList<>(data);
  }
}
