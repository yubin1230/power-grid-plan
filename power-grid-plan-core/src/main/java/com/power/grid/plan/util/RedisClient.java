package com.power.grid.plan.util;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 作者（@author wangchun8 部门： 技术发展部-中台研发部-交易平台组 ）
 * 版本（@version 1.0）
 * 创建、开发日期（@date 2020/6/29 10:15）
 **/
@Component
public class RedisClient {

    public static final String PREFIX = "biz";
    @Resource
    private StringRedisTemplate stringRedisTemplate;


    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(appendPrefix(key));
    }

    public Long getLong(String key) {
        String value = get(key);
        return Objects.isNull(value) ? null : Long.valueOf(value);
    }

    public List<String> multiGet(String... key) {
        List<String> keys = new ArrayList<>(key.length);
        for (String s : key) {
            keys.add(appendPrefix(s));
        }
        return stringRedisTemplate.opsForValue().multiGet(keys);
    }

    @Retryable(value = {Exception.class})
    public boolean setNx(String key, String value) {
        return stringRedisTemplate.opsForValue().setIfAbsent(appendPrefix(key), value);
    }

    @Retryable(value = {Exception.class})
    public boolean setNxExpire(String key, String value,long expire) {
        return stringRedisTemplate.opsForValue().setIfAbsent(appendPrefix(key), value,expire, TimeUnit.SECONDS);
    }

    @Retryable(value = {Exception.class})
    public Long del(List<String> keys) {
        List<String> keyList = new ArrayList<>(keys.size());
        for (String s : keys) {
            keyList.add(appendPrefix(s));
        }
        return stringRedisTemplate.delete(keyList);
    }

    @Retryable(value = {Exception.class})
    public Boolean del(String key) {
        return stringRedisTemplate.delete(appendPrefix(key));
    }

    @Retryable(value = {Exception.class})
    public boolean exists(String key) {
        return stringRedisTemplate.hasKey(appendPrefix(key));
    }


    @Retryable(value = {Exception.class})
    public Long increment(String key, long quantity) {
        return stringRedisTemplate.opsForValue().increment(appendPrefix(key), quantity);
    }

    public Long increment(String key) {
        return increment(appendPrefix(key), 1);
    }

    public static String appendPrefix(String key) {
        return String.format("%s:%s", PREFIX, key);
    }
}
