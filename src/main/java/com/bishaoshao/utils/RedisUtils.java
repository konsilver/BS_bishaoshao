package com.bishaoshao.utils;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RedisUtils {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 设置序列化器，避免 key 的序列化导致乱码
     */
    @PostConstruct
    public void setUp() {
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
    }

    /**
     * 缓存数据
     */
    public boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            log.error("Set key error: {}", key, e);
            return false;
        }
    }

    /**
     * 缓存数据并设置过期时间
     */
    public boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                return set(key, value);
            }
            return true;
        } catch (Exception e) {
            log.error("Set key with expire error: {}", key, e);
            return false;
        }
    }

    /**
     * 获取缓存数据
     */
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 获取缓存过期时间
     */
    public long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 删除缓存
     */
    public void del(String... keys) {
        if (keys != null && keys.length > 0) {
            for (String key : keys) {
                boolean result = redisTemplate.delete(key);
                log.info("删除缓存 key：{}, 结果：{}", key, result);
            }
        }
    }

    /**
     * 设置缓存过期时间
     */
    public boolean expire(String key, long time, TimeUnit timeUnit) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, timeUnit);
                return true;
            }
        } catch (Exception e) {
            log.error("Set key expire error: {}", key, e);
        }
        return false;
    }
}
