package com.hoau.zodiac.core.security;

import com.hoau.zodiac.core.dataprovider.IResourceProvider;
import com.hoau.zodiac.core.dataprovider.ISecureKeyProvider;
import com.hoau.zodiac.core.dataprovider.IUserResourceProvider;
import com.hoau.zodiac.core.entity.IResource;
import com.hoau.zodiac.core.entity.IUser;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
* @Title: SecurityAccess 
* @Package com.hoau.leo.common.security 
* @Description: 访问控制
* @author 陈宇霖  
* @date 2017/8/6 09:41
* @version V1.0   
*/
public class SecurityAccess implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    /**
     * 校验是否有权限访问指定地址
     * @param requestUri            访问的地址
     * @param ignoreNoneConfigUri   是否忽略未配置的地址
     * @return
     * @author 陈宇霖
     * @date 2017年08月06日09:49:00
     */
    public static boolean checkCanAccess(IUser user, String requestUri, boolean ignoreNoneConfigUri) {
        IUserResourceProvider userResourceProvider = applicationContext.getBean(IUserResourceProvider.class);
        if (userResourceProvider != null) {
            List<IResource> resources = userResourceProvider.getResources(user);
            if (CollectionUtils.isEmpty(resources)) {
                return false;
            }
            IResourceProvider resourceProvider = applicationContext.getBean(IResourceProvider.class);
            if (resourceProvider != null) {
                IResource requestResource = resourceProvider.getResource(requestUri);
                if (requestResource == null) {
                    if (ignoreNoneConfigUri) {
                        return true;
                    } else {
                        return false;
                    }
                }
                if (!resources.contains(requestResource)) {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    /**
     * 根据apiKey获取私钥
     * @param apiKey
     * @return
     * @author 陈宇霖
     * @date 2017年08月21日17:00:17
     */
    public static String getSecureKey(String apiKey) {
        ISecureKeyProvider secureKeyProvider = applicationContext.getBean(ISecureKeyProvider.class);
        return secureKeyProvider.getSecureKey(apiKey);
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SecurityAccess.applicationContext = applicationContext;
    }
}
