package com.pet_care.medical_prescription_service.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CacheService {

    RedisTemplate<String, Object> redisTemplate;

    public void saveCache(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);  // Lưu dữ liệu vào Redis
    }

    public Object getCache(String key) {
        return redisTemplate.opsForValue().get(key);  // Lấy dữ liệu từ Redis
    }
}
