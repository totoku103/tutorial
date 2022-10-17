package me.totoku103.redis.config;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * @author jh.kim
 *
 */
@Configuration
public class RedisConfig {

	private final RedisProperties redisProperties;

	public RedisConfig(RedisProperties redisProperties) {
		this.redisProperties = redisProperties;
	}

	@Bean
	public LettuceConnectionFactory lettuceConnectionFactory() {
		GenericObjectPoolConfig<?> poolConfig = new GenericObjectPoolConfig<>();
		poolConfig.setMaxIdle(redisProperties.getLettuce().getPool().getMaxIdle());
		poolConfig.setMaxTotal(redisProperties.getLettuce().getPool().getMaxActive());
		poolConfig.setMaxWaitMillis(redisProperties.getLettuce().getPool().getMaxWait().toMillis());
		poolConfig.setMinIdle(redisProperties.getLettuce().getPool().getMinIdle());

		LettuceClientConfiguration lettuceClientConfiguration = LettucePoolingClientConfiguration.builder()
				.commandTimeout(Duration.ofSeconds(3))
				.shutdownTimeout(Duration.ZERO)
				.poolConfig(poolConfig)
				.build();

		LettuceConnectionFactory lettuceConnectionFactory;
		RedisProperties.Cluster cluster = redisProperties.getCluster();

		// cluster
		if (cluster != null && !ListUtils.emptyIfNull(cluster.getNodes()).isEmpty()) {
			RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration();
			cluster.getNodes().forEach(s -> {
				String[] url = s.split(":");
				redisClusterConfiguration.clusterNode(url[0], Integer.parseInt(url[1]));
				redisClusterConfiguration.setPassword(redisProperties.getPassword());
			});

			lettuceConnectionFactory = new LettuceConnectionFactory(redisClusterConfiguration,
					lettuceClientConfiguration);
		} else {
			RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(
					redisProperties.getHost(), redisProperties.getPort());
			redisStandaloneConfiguration.setPassword(RedisPassword.of(redisProperties.getPassword()));
			redisStandaloneConfiguration.setDatabase(redisProperties.getDatabase());

			lettuceConnectionFactory = new LettuceConnectionFactory(redisStandaloneConfiguration,
					lettuceClientConfiguration);
		}

		return lettuceConnectionFactory;
	}

	@Bean
	public RedisTemplate<String, Object> redisTemplate() {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new StringRedisSerializer());
		redisTemplate.setConnectionFactory(lettuceConnectionFactory());
		return redisTemplate;
	}

}
