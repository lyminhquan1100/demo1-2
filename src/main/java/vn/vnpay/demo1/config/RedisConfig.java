package vn.vnpay.demo1.config;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Transaction;
import vn.vnpay.demo1.domain.BankRequest;

import java.util.Calendar;


@Configuration
@EnableRedisRepositories
@Slf4j
public class RedisConfig {

    @Value("${spring.redis.port}")
    private Integer port;
    @Value("${spring.redis.host}")
    private String localhost;
    @Value("${spring.redis.database}")
    private Integer database;

    @Bean
    public JedisConnectionFactory connectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(localhost);
        configuration.setPort(port); // yaml
        configuration.setDatabase(database);
        return new JedisConnectionFactory(configuration);
    }

    @Bean
    public RedisTemplate<String, Object> createRedisTemplateForEntity() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory());
        redisTemplate.setEnableTransactionSupport(true);
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    public void save(BankRequest bankRequest) {
        Jedis jedis = null;
        try {
            log.info("Begin init.");
            jedis = JedisConfiguration.getPool().getResource();
            log.info("Get pool success. Saving to Redis...");
            Gson gson = new Gson();
            String bankRequestDTOJson = gson.toJson(bankRequest);
            jedis.hset(bankRequest.getBankCode(),bankRequest.getTokenKey(),bankRequestDTOJson);
            log.info("Set redis expire time.");

        } catch (Exception e) {
            log.error("Exception {} ", e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

}
