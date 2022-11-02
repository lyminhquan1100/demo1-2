package vn.vnpay.demo1.config;

import org.springframework.beans.factory.annotation.Value;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
public class JedisConfiguration {
    private static JedisPool jedisPool;
    @Value("${spring.redis.port}")
    private static Integer port;
    @Value("${spring.redis.host}")
    private static String localhost;
    @Value("${spring.redis.database}")
    private Integer database;

    public static JedisPool getPool() {
        if (JedisConfiguration.jedisPool == null) {
            JedisPoolConfig poolConfig = new JedisPoolConfig();
            poolConfig.setMaxTotal(8);
            poolConfig.setMaxIdle(8);
            poolConfig.setMinIdle(0);
//            poolConfig.setMinEvictableIdleTime(Duration.ofSeconds(Constant.Redis.MIN_EVICTABLE_IDLE_SEC));
//            poolConfig.setTimeBetweenEvictionRuns(Duration.ofSeconds(Constant.Redis.TIME_BETWEEN_EVICTION_RUNS_SEC));
//            poolConfig.setBlockWhenExhausted(Constant.Redis.BLOCK_WHEN_EXHAUSTED);

            JedisConfiguration.jedisPool = new JedisPool(poolConfig, "localhost", 6379);
        }
        return jedisPool;
    }
}
