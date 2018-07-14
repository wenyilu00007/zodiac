package com.wyl.zodiac.core.dataprovider;

import com.wyl.zodiac.core.entity.IResource;
import com.wyl.zodiac.core.entity.IUser;

import java.util.List;

/**
* @Title: IUserResourceProvider
* @Package com.wyl.leo.common.dataprovider
* @Description: 用户访问权限数据提供者
* @author
* @date 2017/8/4 17:19
* @version V1.0   
*/
public interface IUserResourceProvider {

    /**
     * 根据用户获取用户的访问权限
     * @param user
     * @param systemCode
     * @return
     * @author
     * @date 2017年08月04日17:23:34
     */
    List<IResource> getResources(IUser user, String systemCode);

}
