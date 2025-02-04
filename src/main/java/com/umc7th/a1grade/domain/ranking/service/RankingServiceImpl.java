package com.umc7th.a1grade.domain.ranking.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.umc7th.a1grade.domain.question.repository.QuestionLogRepository;
import com.umc7th.a1grade.domain.ranking.dto.UserRankingDto;
import com.umc7th.a1grade.domain.ranking.entity.Ranking;
import com.umc7th.a1grade.domain.ranking.repository.RankingRepository;
import com.umc7th.a1grade.domain.user.entity.User;
import com.umc7th.a1grade.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RankingServiceImpl implements RankingService {
  private final RankingRepository rankingRepository;
  private final QuestionLogRepository logRepository;
  private final UserRepository userRepository;

  @Override
  @Transactional
  public void updateDailyRanking() {
    LocalDateTime start = LocalDate.now().minusDays(1).atStartOfDay();
    LocalDateTime end = LocalDate.now().atStartOfDay();
    LocalDate rankingDate = LocalDate.now().minusDays(1);

    deletePreviousRanking(rankingDate);
    List<User> users = getUsersWithEnoughCorrectAnswers();

    List<UserRankingDto> top3Users =
        users.stream()
            .map(user -> calculateUserRanking(user, start, end))
            .sorted(
                Comparator.comparing(UserRankingDto::getCorrectCount)
                    .reversed()
                    .thenComparing(UserRankingDto::getAccuracy, Comparator.reverseOrder()))
            .limit(3)
            .collect(Collectors.toList());

    updateRanking(top3Users, rankingDate);
  }

  private void deletePreviousRanking(LocalDate rankingDate) {
    rankingRepository.deleteByRankingDate(rankingDate);
  }

  private List<User> getUsersWithEnoughCorrectAnswers() {
    return userRepository.findUserWithCorrectAnswers();
  }

  private UserRankingDto calculateUserRanking(User user, LocalDateTime start, LocalDateTime end) {
    Long userId = user.getId();

    List<Long> yesterdaySolvedQuestions =
        logRepository.findYesterdaySolvedQuestionIds(userId, start, end);
    List<Long> previouslySolvedQuestions =
        logRepository.findPreviouslySolvedQuestionIds(userId, start);

    long finalCorrectCount =
        yesterdaySolvedQuestions.stream()
            .filter(q -> !previouslySolvedQuestions.contains(q))
            .count();

    long totalCount = logRepository.countTotalAttempts(userId, start, end);
    double accuracy = (totalCount == 0) ? 0 : (double) finalCorrectCount / totalCount;

    return new UserRankingDto(user, finalCorrectCount, totalCount, accuracy);
  }

  private void updateRanking(List<UserRankingDto> top3Users, LocalDate rankingDate) {
    List<Ranking> rankings =
        top3Users.stream()
            .map(
                ranking ->
                    Ranking.builder()
                        .user(ranking.getUser())
                        .solvedCount(ranking.getCorrectCount())
                        .rankingDate(rankingDate)
                        .answerRate(BigDecimal.valueOf(ranking.getAccuracy()))
                        .build())
            .collect(Collectors.toList());
    rankingRepository.saveAll(rankings);
  }
}
