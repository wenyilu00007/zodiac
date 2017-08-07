package com.hoau.zodiac.core.dataprovider;

import com.hoau.zodiac.core.entity.IUser;

/**
* @Title: IUserProvider 
* @Package com.hoau.leo.common.dataprovider 
* @Description: 提供用户获取
* @author 陈宇霖  
* @date 2017/8/2 21:26
* @version V1.0   
*/
public interface IUserProvider {

    /**
     * 根据用户id获取用户
     * @param userId
     * @return
     * @author 陈宇霖
     * @date 2017年08月02日21:28:42
     */
    IUser getUserById(String userId);

}
