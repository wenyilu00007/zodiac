package com.hoau.zodiac.demo.cache;

import com.hoau.zodiac.cache.redis.DefaultTTLRedisCache;
import com.hoau.zodiac.cache.redis.storage.RedisCacheStorage;
import com.hoau.zodiac.demo.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
* @Title: UserCache 
* @Package com.hoau.leo.business.cache 
* @Description: 用户信息缓存
* @author 陈宇霖  
* @date 2017/8/7 15:17
* @version V1.0   
*/
@Component
public class UserCache extends DefaultTTLRedisCache<User> {

    @Autowired
    private RedisCacheStorage redisCacheStorage;

    @Autowired
    private UserCacheProvider userCacheProvider;

    public static final String UUID = UserCache.class.getName();

    public String getUUID() {
        return UUID;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        setCacheStorage(redisCacheStorage);
        setCacheProvider(userCacheProvider);
        setTimeOut(10 * 60);
    }
}
