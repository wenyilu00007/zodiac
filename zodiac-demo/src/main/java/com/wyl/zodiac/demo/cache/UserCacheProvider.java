package com.wyl.zodiac.demo.cache;

import com.wyl.zodiac.cache.provider.ITTLCacheProvider;
import com.wyl.zodiac.demo.dao.UserDao;
import com.wyl.zodiac.demo.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
* @Title: UserCacheProvider 
* @Package com.wyl.leo.business.cache
* @Description: 用户信息提供者
* @author
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
