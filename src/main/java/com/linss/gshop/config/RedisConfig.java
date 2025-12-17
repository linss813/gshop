package com.linss.gshop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.cache.annotation.EnableCaching;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;

import java.time.Duration;

@Configuration
@EnableCaching
public class RedisConfig {

    // 配置Spring Cache的Redis缓存管理器
    @Bean
    public org.springframework.cache.CacheManager cacheManager(RedisConnectionFactory factory) {
        // 使用RedisCacheConfiguration创建RedisCacheManager
        org.springframework.data.redis.cache.RedisCacheConfiguration cacheConfig = 
            org.springframework.data.redis.cache.RedisCacheConfiguration.defaultCacheConfig()
                // 设置缓存过期时间，默认30分钟
                .entryTtl(Duration.ofMinutes(30))
                // 设置缓存键前缀
                .computePrefixWith(cacheName -> cacheName + ":")
                // 配置键序列化方式
                .serializeKeysWith(
                    org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair.fromSerializer(
                        new StringRedisSerializer()))
                // 配置值序列化方式
                .serializeValuesWith(
                    org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair.fromSerializer(
                        new Jackson2JsonRedisSerializer<>(Object.class)));

        return org.springframework.data.redis.cache.RedisCacheManager.builder(factory)
            .cacheDefaults(cacheConfig)
            .build();
    }

    @Value("${spring.redis.host:localhost}")
    private String host;

    @Value("${spring.redis.port:6379}")
    private int port;

    @Value("${spring.redis.password:}")
    private String password;

    @Value("${spring.redis.lettuce.pool.max-active:8}")
    private int maxActive;

    @Value("${spring.redis.lettuce.pool.max-idle:8}")
    private int maxIdle;

    @Value("${spring.redis.lettuce.pool.min-idle:2}")
    private int minIdle;

    @Value("${spring.redis.lettuce.pool.max-wait:2000ms}")
    private Duration maxWait;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        // 配置Redis连接信息
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration(host, port);
        if (!password.isEmpty()) {
            redisConfig.setPassword(password);
        }

        // 配置Lettuce连接池
        org.apache.commons.pool2.impl.GenericObjectPoolConfig<Object> poolConfig = 
            new org.apache.commons.pool2.impl.GenericObjectPoolConfig<>();
        
        // 设置连接池参数
        poolConfig.setMaxTotal(maxActive);
        poolConfig.setMaxIdle(maxIdle);
        poolConfig.setMinIdle(minIdle);
        poolConfig.setMaxWait(maxWait);

        LettucePoolingClientConfiguration.LettucePoolingClientConfigurationBuilder clientConfigBuilder = 
            LettucePoolingClientConfiguration.builder()
                .commandTimeout(Duration.ofSeconds(5))
                .shutdownTimeout(Duration.ofSeconds(10))
                .poolConfig(poolConfig);

        LettuceClientConfiguration clientConfig = clientConfigBuilder.build();

        // 创建Lettuce连接工厂
        return new LettuceConnectionFactory(redisConfig, clientConfig);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        // 使用StringRedisSerializer序列化和反序列化key值
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());

        // 配置Jackson2JsonRedisSerializer序列化value值
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        // 启用默认类型，支持多态
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
        // 配置日期格式
        objectMapper.setDateFormat(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        objectMapper.setTimeZone(java.util.TimeZone.getTimeZone("Asia/Shanghai"));
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.setHashValueSerializer(jackson2JsonRedisSerializer);

        template.afterPropertiesSet();
        return template;
    }
}