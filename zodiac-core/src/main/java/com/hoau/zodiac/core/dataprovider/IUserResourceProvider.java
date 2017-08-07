package com.hoau.zodiac.core.dataprovider;

import com.hoau.zodiac.core.entity.IResource;
import com.hoau.zodiac.core.entity.IUser;

import java.util.List;

/**
* @Title: IUserResourceProvider
* @Package com.hoau.leo.common.dataprovider 
* @Description: 用户访问权限数据提供者
* @author 陈宇霖  
* @date 2017/8/4 17:19
* @version V1.0   
*/
public interface IUserResourceProvider {

    /**
     * 根据用户获取用户的访问权限
     * @param user
     * @return
     * @author 陈宇霖
     * @date 2017年08月04日17:23:34
     */
    List<IResource> getResources(IUser user);

}
