package nhn.academy.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class LoginFailService {
    private final StringRedisTemplate redisTemplate;

    public LoginFailService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public int incrementLoginFailCount(String id) {
        String key = "loginFail:" + id;
        Long count = redisTemplate.opsForValue().increment(key);

        redisTemplate.expire(key, 60, java.util.concurrent.TimeUnit.SECONDS);

        return count == null ? 1 : count.intValue();
    }

    public void resetFailCount(String userId) {
        redisTemplate.delete("loginFail:" + userId);
    }

    public int getFailCount(String userId) {
        String value = redisTemplate.opsForValue().get("loginFail:" + userId);
        return value == null ? 0 : Integer.parseInt(value);
    }

    public void setFailCountExpire(String id, long time) {
        redisTemplate.expire("loginFail:" + id, time, TimeUnit.SECONDS);
    }
}