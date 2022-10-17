package me.totoku103.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class RedisThrottleDao {

    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;
    private final String KEY_THROTTLE_INFO = RedisThrottleKey.ImcThrottleInfo.name();
    private final String KEY_COUNT_TEMP = "THROTTLE:%s:%s:COUNT:TEMP";
    private final String KEY_COUNT_COMPLETE = "THROTTLE:%s:%s:COUNT:COMPLETE";

    private final String DATE_FORMAT = "yyyy-MM-dd";

    public RedisThrottleInfo getThrottleInfo(String agentId) throws JsonProcessingException {
        final String result = (String) this.stringRedisTemplate.opsForHash().get(this.KEY_THROTTLE_INFO, agentId);
        if (result == null) return null;
        return this.objectMapper.readValue(result, RedisThrottleInfo.class);
    }

    public long getMonthlySendCount(String agentId) {
        final List<String> multiGetParams = getMonthlyList().stream().map(date -> String.format(KEY_COUNT_COMPLETE, date, agentId)).collect(Collectors.toList());
        final List<String> result = this.stringRedisTemplate.opsForValue().multiGet(multiGetParams);
        if (result == null || result.size() == 0) return 0;
        else return result.stream().map(d -> Integer.parseInt(d)).count();
    }

    public void put(String hashKey, List<RedisThrottleInfo> redisThrottleInfo) throws JsonProcessingException {
        final String hashValue = this.objectMapper.writeValueAsString(redisThrottleInfo);
        this.stringRedisTemplate.opsForHash().put(this.KEY_THROTTLE_INFO, hashKey, hashValue);
    }

    public Long incrementTempCount(String agentId, int incrementCount) {
        final String key = String.format(this.KEY_COUNT_TEMP, getCurrentDate(), agentId);
        return this.stringRedisTemplate.opsForValue().increment(key, incrementCount);
    }

    public Long decrementTempCount(String agentId, int decrementCount) {
        final String key = String.format(this.KEY_COUNT_TEMP, getCurrentDate(), agentId);
        return this.stringRedisTemplate.opsForValue().decrement(key, decrementCount);
    }

    public Long incrementCompleteCount(String agentId, int incrementCount) {
        final String key = String.format(this.KEY_COUNT_COMPLETE, getCurrentDate(), agentId);
        return this.stringRedisTemplate.opsForValue().increment(key, incrementCount);
    }

    public Long decrementCompleteCount(String agentId, int decrementCount) {
        final String key = String.format(this.KEY_COUNT_COMPLETE, getCurrentDate(), agentId);
        return this.stringRedisTemplate.opsForValue().decrement(key, decrementCount);
    }

    private String getCurrentDate() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern(this.DATE_FORMAT));
    }

    private List<String> getMonthlyList() {
        final LocalDate now = LocalDate.now();
        final int dayOfMonth = now.getDayOfMonth();

        final ArrayList<String> result = new ArrayList<>();
        if (dayOfMonth == 1) {
            result.add(getCurrentDate());
        } else {
            for (int i = 1; i <= dayOfMonth; i++) {
                final String day = LocalDate.of(now.getYear(), now.getMonth(), i).format(DateTimeFormatter.ofPattern(this.DATE_FORMAT));
                result.add(day);
            }
        }
        return result;
    }

}
