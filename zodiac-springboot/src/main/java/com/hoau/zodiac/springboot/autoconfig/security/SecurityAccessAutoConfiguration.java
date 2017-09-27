package com.hoau.zodiac.springboot.autoconfig.security;

import com.hoau.zodiac.core.message.LocaleMessageSource;
import com.hoau.zodiac.core.security.SecurityAccess;
import com.hoau.zodiac.springboot.autoconfig.message.LocaleMessageAutoConfiguration;
import com.hoau.zodiac.web.interceptor.AccessInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
* @Title: SecurityAccessAutoConfiguration
* @Package com.hoau.leo.config.web 
* @Description: 访问控制自动装配
* @author 陈宇霖  
* @date 2017/8/4 16:45
* @version V1.0   
*/
@Configuration
@ConditionalOnWebApplication
@AutoConfigureAfter(LocaleMessageAutoConfiguration.class)
@EnableConfigurationProperties(SecurityAccessProperties.class)
@ConditionalOnProperty(prefix = "zodiac.security.accessControl", name = "enable")
public class SecurityAccessAutoConfiguration {

    @Autowired
    private SecurityAccessProperties securityAccessProperties;

    @Autowired
    private MessageSource messageSource;

    /**
     * 创建访问控制实体
     * @return
     * @author 陈宇霖
     * @date 2017年08月06日09:59:10
     */
    @Bean
    @ConditionalOnMissingBean
    public SecurityAccess securityAccess () {
        return new SecurityAccess();
    }

    /**
     * 创建访问控制拦截器
     * @return
     * @author 陈宇霖
     * @date 2017年08月06日09:59:27
     */
    @Bean
    @ConditionalOnMissingBean
    public AccessInterceptor accessInterceptor(LocaleMessageSource localeMessageSource) {
        AccessInterceptor interceptor = new AccessInterceptor();
        interceptor.setIgnoreNoneConfigUri(securityAccessProperties.isIgnoreNoneConfigUri());
        interceptor.setSystemCode(securityAccessProperties.getSystemCode());
        interceptor.setLocaleMessageSource(localeMessageSource);
        return interceptor;
    }

}
