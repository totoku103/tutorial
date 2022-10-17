package me.totoku103.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class RepositoryTest {

    @Autowired
    private Repository repository;

    @Test
    public void save() throws JsonProcessingException {
        repository.save();
    }

    @Test
    public void find() throws JsonProcessingException {
        List<RedisThrottleInfo> redisThrottleInfos = repository.find();
        redisThrottleInfos
                .stream()
                .forEach(d -> System.out.println(d));
    }

    @Test
    public void increment() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(50);
        for (int i = 0; i < 50; i++) {
            executorService.submit(() -> {
                for (int y = 0; y < 500; y++)
                    repository.execute();
            });
        }
        executorService.shutdown();
        executorService.awaitTermination(100, TimeUnit.MINUTES);
    }

}