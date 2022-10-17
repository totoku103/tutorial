package me.totoku103.redis.component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomRedisOperation {

    public Long count(RedisOperations<String, Object> operations, String key) {
        Long count = (Long) operations.opsForValue().get(key);
        return count;
    }

    public Long increment(RedisOperations<String, Object> operations, String key) {
        Long increment = operations.opsForValue().increment(key);
        return increment;
    }

    public void execute(RedisOperations<String, Object> operations, String key, int limit) {
        Long count = this.count(operations, key);
        log.info("count: {}, limit: {}", count, limit);
        if (limit < count)
            return;
        this.increment(operations, key);
    }
}
