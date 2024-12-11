package com.pet_care.customer_service.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RedisNativeService {
    RedisTemplate<String, Object> redisTemplate;

    public <T> void saveToRedisList(String key, List<T> list, long timeoutInSeconds) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("List cannot be null or empty.");
        }
        redisTemplate.opsForList().rightPushAll(key, list.toArray());
        redisTemplate.expire(key, Duration.ofSeconds(timeoutInSeconds)); // Thêm TTL
    }

    public <T> List<T> getRedisList(String key, Class<T> clazz) {
        List<Object> rawList = redisTemplate.opsForList().range(key, 0, -1);
        if (rawList == null || rawList.isEmpty()) {
            return List.of(); // Trả về danh sách rỗng thay vì null
        }
        return rawList.stream().map(clazz::cast).toList();
    }

    public void deleteRedisList(String key) {
        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            redisTemplate.delete(key);
        }
    }
}
