package com.hoau.zodiac.springboot.autoconfig.cache;

import com.hoau.zodiac.cache.CacheManager;
import com.hoau.zodiac.cache.redis.storage.RedisCacheStorage;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
* @Title: RedisAutoConfiguration 
* @Package com.hoau.zodiac.springboot.autoconfig.cache 
* @Description: Redis缓存存储自动装配
* @author 陈宇霖  
* @date 2017/8/7 10:33
* @version V1.0   
*/
@Configuration
@ConditionalOnClass(CacheManager.class)
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class RedisStorageAutoConfiguration {

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Bean
    @ConditionalOnMissingBean
    public RedisCacheStorage redisCacheStorage(RedisTemplate redisTemplate) {
        RedisCacheStorage storage = new RedisCacheStorage();
//        FastJsonRedisSerializer serializer = new FastJsonRedisSerializer();
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        redisTemplate.setValueSerializer(serializer);
//        redisTemplate.setHashKeySerializer(serializer);
//        redisTemplate.setHashValueSerializer(serializer);
        storage.setRedisTemplate(redisTemplate);
        return storage;
    }

}
