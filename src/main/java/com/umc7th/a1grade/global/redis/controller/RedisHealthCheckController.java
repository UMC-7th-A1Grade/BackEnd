package com.umc7th.a1grade.global.redis.controller;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/redis")
@RequiredArgsConstructor
@Tag(name = "Redis", description = "Redis 테스트용 API")
public class RedisHealthCheckController {
  private final RedisTemplate<String, String> redisTemplate;

  @Operation(summary = "Redis 응답 테스트", description = """
           Redis 응답 테스트  \n
           """)
  @GetMapping(value = "/check", produces = "application/json")
  public String checkRedisConnection() {
    try {
      String pingResponse = redisTemplate.getConnectionFactory().getConnection().ping();
      return "Redis 연결 성공: " + pingResponse;
    } catch (Exception e) {
      return "Redis 연결 실패: " + e.getMessage();
    }
  }
}
