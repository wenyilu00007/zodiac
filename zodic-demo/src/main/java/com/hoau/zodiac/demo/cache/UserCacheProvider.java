package com.hoau.zodiac.demo.cache;

import com.hoau.zodiac.cache.provider.ITTLCacheProvider;
import com.hoau.zodiac.demo.dao.UserDao;
import com.hoau.zodiac.demo.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
* @Title: UserCacheProvider 
* @Package com.hoau.leo.business.cache 
* @Description: 用户信息提供者
* @author 陈宇霖  
* @date 2017/8/7 15:21
* @version V1.0   
*/
@Component
public class UserCacheProvider implements ITTLCacheProvider<User> {

    @Autowired
    private UserDao userDao;

    public User get(String s) {
        User queryParam = new User();
        queryParam.setUserName(s);
        return userDao.selectOne(queryParam);
    }

}
