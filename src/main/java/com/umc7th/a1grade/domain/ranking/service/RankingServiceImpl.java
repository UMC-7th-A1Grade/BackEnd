package com.umc7th.a1grade.domain.ranking.service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc7th.a1grade.domain.user.dto.AllGradeResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.umc7th.a1grade.domain.question.repository.QuestionLogRepository;
import com.umc7th.a1grade.domain.ranking.dto.UserRankingDto;
import com.umc7th.a1grade.domain.user.entity.User;
import com.umc7th.a1grade.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Slf4j
@Service
@RequiredArgsConstructor
public class RankingServiceImpl implements RankingService {
  private final QuestionLogRepository logRepository;
  private final UserRepository userRepository;
  private final RedisTemplate<String, String> redisTemplate;
  private final ObjectMapper objectMapper;
  @Scheduled(cron = "*/5 * * * * *")
  public void scheduleDailyRankingUpdate() {
    log.info("갱신 실행");
    updateDailyRanking();
  }

  @Override
  @Transactional
  public void updateDailyRanking() {
    LocalDateTime start = LocalDate.now().minusDays(1).atStartOfDay();
    LocalDateTime end = LocalDate.now().atStartOfDay();

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

    saveRankingToRedis(top3Users);

  }

  private void saveRankingToRedis(List<UserRankingDto> top3Users) {
    Duration ttl = getRemainingTime();

    for (int i = 0; i < top3Users.size(); i++) {
      UserRankingDto ranking = top3Users.get(i);
      String rankKey = "RANK:" + (i + 1);
      AllGradeResponseDto dto = AllGradeResponseDto.builder()
              .userId(ranking.getUser().getId())
              .nickName(ranking.getUser().getNickName())
              .correctCount(ranking.getCorrectCount())
              .accuracy(BigDecimal.valueOf(ranking.getAccuracy()))
              .build();

      try {
        String rankingJson = objectMapper.writeValueAsString(dto);

        redisTemplate.opsForValue().set(rankKey, rankingJson, ttl);

        log.info("저장 성공 : {} == {}, ( 삭제 시간 = {} )", rankKey, rankingJson, ttl.getSeconds());
      } catch (Exception e) {
        log.error("JSON 변환 오류: {}", e.getMessage());
      }
    }
  }

  private Duration getRemainingTime() {
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime night = LocalDateTime.of(now.toLocalDate().plusDays(1), LocalDateTime.MIN.toLocalTime());
    return Duration.between(now, night);
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

}
