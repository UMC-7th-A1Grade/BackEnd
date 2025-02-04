package com.umc7th.a1grade.unittest.user.fake;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.HashMap;
import java.util.Map;

public class FakeRedisTemplate extends RedisTemplate<String,String> {
    private final Map<String, String> fakeStorage = new HashMap<>();


}
