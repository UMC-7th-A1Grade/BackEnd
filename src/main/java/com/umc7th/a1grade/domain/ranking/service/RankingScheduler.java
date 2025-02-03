package com.umc7th.a1grade.domain.ranking.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RankingScheduler {
  private final RankingService rankingService;

  @Scheduled(cron = "0 0 0 * * ?")
  @Transactional
  public void runDailyRankingUpdate() {
    rankingService.updateDailyRanking();
  }
}
