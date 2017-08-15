package com.hoau.zodiac.demo.service.impl.impl;

import com.hoau.zodiac.cache.CacheManager;
import com.hoau.zodiac.cache.ICache;
import com.hoau.zodiac.datasource.mybatis.service.AbstractBaseService;
import com.hoau.zodiac.demo.cache.UserCache;
import com.hoau.zodiac.demo.dao.UserDao;
import com.hoau.zodiac.demo.domain.User;
import com.hoau.zodiac.demo.service.impl.IUserService;
import org.springframework.stereotype.Service;

/**
* @Title: UserService 
* @Package com.hoau.zodiac.demo.service.impl 
* @Description: 用户信息
* @author 陈宇霖  
* @date 2017/8/9 18:27
* @version V1.0   
*/
@Service
public class UserService extends AbstractBaseService<UserDao, User> implements IUserService<User> {
    /**
     * 从缓存中读取用户信息
     *
     * @param userName
     * @return
     * @author 陈宇霖
     * @date 2017年08月10日17:25:43
     */
    public User selectUserFromCache(String userName) {
        ICache<String, User> cache = CacheManager.getInstance().getCache(UserCache.UUID);
        return cache.get(userName);
    }
}
