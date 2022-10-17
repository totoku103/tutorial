package me.totoku103.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.totoku103.redis.component.CustomRedisOperation;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
@Component
@RequiredArgsConstructor
public class Repository {

    private final RedisTemplate<String, Object> redisTemplate;

    private final String key = "ImcThrottleInfo";
    private final int LIMIT = 3_000;

    public void execute() {
//        Random random = new Random();
//        int i = random.nextInt(10);
        int i = 1;
        Long dev001 = incrementTemp("dev001", i);

        if (dev001 > 100) {
            Long dev0011 = decrymentTemp("dev001", i);
        } else {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            increment("dev001", i);
        }
    }

    public void save() throws JsonProcessingException {
        List<RedisThrottleInfo> redisThrottleInfo = new ArrayList<>();
        redisThrottleInfo.add(getRedisThrottleInfo(1, "AT", 10, 20));
        redisThrottleInfo.add(getRedisThrottleInfo(2, "MT", 20, 30));
        redisThrottleInfo.add(getRedisThrottleInfo(3, "IM", 30, 40));

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String s = objectMapper.writeValueAsString(redisThrottleInfo);
        redisTemplate.opsForHash().put(key, "dev001Test", s);
    }

    public List<RedisThrottleInfo> find() throws JsonProcessingException {
        String dev001Test = (String) redisTemplate.opsForHash().get(key, "dev001Test");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        List<RedisThrottleInfo> list = objectMapper.readValue(dev001Test, new TypeReference<List<RedisThrottleInfo>>() {
        });
        log.info("list: {}", list);
        return list;
    }


    public RedisThrottleInfo getRedisThrottleInfo(int id, String channelType, int dailyCount, int monthlyCount) {
        RedisThrottleInfo redisThrottleInfo = new RedisThrottleInfo();
        redisThrottleInfo.setId(id);
        redisThrottleInfo.setAgentId("dev001Test");
        redisThrottleInfo.setChannelType(channelType);
        redisThrottleInfo.setDailyLimitCount(dailyCount);
        redisThrottleInfo.setMonthlyLimitCount(monthlyCount);
        redisThrottleInfo.setCreatedBy("tester");
        redisThrottleInfo.setCreatedDate(LocalDateTime.now());
        redisThrottleInfo.setLastModifiedBy("tester");
        redisThrottleInfo.setLastModifiedDate(LocalDateTime.now());
        return redisThrottleInfo;
    }

    public Long incrementTemp(String agentId, int count) {
        Long increment = redisTemplate.opsForValue().increment(String.format("%s:DAILY:TEMP:COUNT", agentId), count);
        return increment;
    }

    public Long decrymentTemp(String agentId, int count) {
        Long decrement = redisTemplate.opsForValue().decrement(String.format("%s:DAILY:TEMP:COUNT", agentId), count);
        return decrement;
    }


    public Long increment(String agentId, int count) {
        Long increment = redisTemplate.opsForValue().increment(String.format("%s:DAILY:COMPLETE:COUNT", agentId), count);
        return increment;
    }

    public Long decryment(String agentId, int count) {
        Long decrement = redisTemplate.opsForValue().decrement(String.format("%s:DAILY:COMPLETE:COUNT", agentId), count);
        return decrement;
    }


}
