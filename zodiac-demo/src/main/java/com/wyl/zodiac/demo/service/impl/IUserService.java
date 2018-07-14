package com.wyl.zodiac.demo.service.impl;

import com.wyl.zodiac.datasource.mybatis.service.IBaseService;
import com.wyl.zodiac.demo.domain.User;

/**
 * @author
 * @version V1.0
 * @Title: IUserService
 * @Package com.wyl.zodiac.demo.service
 * @Description: 用户接口
 * @date 2017/8/9 17:38
 */
public interface IUserService<T> extends IBaseService<T> {

    /**
     * 从缓存中读取用户信息
     * @param userName
     * @return
     * @author
     * @date 2017年08月10日17:25:43
     */
    User selectUserFromCache(String userName);

}
