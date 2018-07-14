package com.wyl.zodiac.core.security;

import com.wyl.zodiac.core.dataprovider.IResourceProvider;
import com.wyl.zodiac.core.dataprovider.ISecureKeyProvider;
import com.wyl.zodiac.core.dataprovider.IUserResourceProvider;
import com.wyl.zodiac.core.entity.IResource;
import com.wyl.zodiac.core.entity.IUser;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
* @Title: SecurityAccess 
* @Description: 访问控制
* @author
* @date 2017/8/6 09:41
* @version V1.0   
*/
public class SecurityAccess implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    /**
     * 校验是否有权限访问指定地址
     * @param user                  访问的用户
     * @param systemCode            访问的系统
     * @param requestUri            访问的地址
     * @param ignoreNoneConfigUri   是否忽略未配置的地址
     * @return
     * @author
     * @date 2017年08月06日09:49:00
     */
    public static boolean checkCanAccess(IUser user, String systemCode, String requestUri, boolean ignoreNoneConfigUri) {
        IUserResourceProvider userResourceProvider = applicationContext.getBean(IUserResourceProvider.class);
        if (userResourceProvider != null) {
            List<IResource> resources = userResourceProvider.getResources(user, systemCode);
            if (CollectionUtils.isEmpty(resources)) {
                return false;
            }
            IResourceProvider resourceProvider = applicationContext.getBean(IResourceProvider.class);
            if (resourceProvider != null) {
                IResource requestResource = resourceProvider.getResource(requestUri, systemCode);
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
     * @author
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
