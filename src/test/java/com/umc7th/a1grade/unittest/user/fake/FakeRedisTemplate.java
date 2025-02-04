package com.umc7th.a1grade.unittest.user.fake;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.redis.core.RedisTemplate;

public class FakeRedisTemplate extends RedisTemplate<String, String> {
  private final Map<String, String> fakeStorage = new HashMap<>();
}
