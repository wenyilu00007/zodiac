package com.wyl.zodiac.core.dataprovider;

import com.wyl.zodiac.core.entity.IUser;

/**
* @Title: IUserProvider 
* @Package com.wyl.leo.common.dataprovider
* @Description: 提供用户获取
* @author
* @date 2017/8/2 21:26
* @version V1.0   
*/
public interface IUserProvider {

    /**
     * 根据用户id获取用户
     * @param userId
     * @return
     * @author
     * @date 2017年08月02日21:28:42
     */
    IUser getUserById(String userId);

}
