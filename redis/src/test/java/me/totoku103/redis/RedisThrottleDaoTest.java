package me.totoku103.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RedisThrottleDaoTest {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    private RedisThrottleDao redisThrottleDao;

    private final String AGENT_ID = "dev002";

    @BeforeEach
    public void initialize() {
        System.out.println("INIT");
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        redisThrottleDao = new RedisThrottleDao(stringRedisTemplate, objectMapper);
    }

    @Test
    public void incrementTempCount() {
        Long aLong = redisThrottleDao.incrementTempCount(AGENT_ID, 1);
        System.out.println(aLong);
//        Long aLong1 = redisThrottleDao.decrementTempCount(AGENT_ID, 1);
//        System.out.println(aLong1);
    }

    @Test
    public void incrementCompleteCount() {
        Long aLong = redisThrottleDao.incrementCompleteCount(AGENT_ID, 1);
        System.out.println(aLong);
//        Long aLong1 = redisThrottleDao.decrementCompleteCount(AGENT_ID, 1);
//        System.out.println(aLong1);
    }

    @Test
    public void put() throws JsonProcessingException {
        List<RedisThrottleInfo> redisThrottleInfo = new ArrayList<>();
        redisThrottleInfo.add(getRedisThrottleInfo(1, "AT", 10, 20));
        redisThrottleInfo.add(getRedisThrottleInfo(2, "MT", 20, 30));
        redisThrottleInfo.add(getRedisThrottleInfo(3, "IM", 30, 40));
        this.redisThrottleDao.put(AGENT_ID, redisThrottleInfo);
    }

    public RedisThrottleInfo getRedisThrottleInfo(int id, String channelType, int dailyCount, int monthlyCount) {
        RedisThrottleInfo redisThrottleInfo = new RedisThrottleInfo();
        redisThrottleInfo.setId(id);
        redisThrottleInfo.setAgentId(AGENT_ID);
        redisThrottleInfo.setChannelType(channelType);
        redisThrottleInfo.setDailyLimitCount(dailyCount);
        redisThrottleInfo.setMonthlyLimitCount(monthlyCount);
        redisThrottleInfo.setCreatedBy("tester");
        redisThrottleInfo.setCreatedDate(LocalDateTime.now());
        redisThrottleInfo.setLastModifiedBy("tester");
        redisThrottleInfo.setLastModifiedDate(LocalDateTime.now());
        return redisThrottleInfo;
    }
}