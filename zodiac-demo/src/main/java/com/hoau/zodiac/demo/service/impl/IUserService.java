package com.hoau.zodiac.demo.service.impl;

import com.hoau.zodiac.datasource.mybatis.service.IBaseService;
import com.hoau.zodiac.demo.domain.User;

/**
 * @author 陈宇霖
 * @version V1.0
 * @Title: IUserService
 * @Package com.hoau.zodiac.demo.service
 * @Description: 用户接口
 * @date 2017/8/9 17:38
 */
public interface IUserService<T> extends IBaseService<T> {

    /**
     * 从缓存中读取用户信息
     * @param userName
     * @return
     * @author 陈宇霖
     * @date 2017年08月10日17:25:43
     */
    User selectUserFromCache(String userName);

}
