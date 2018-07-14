package com.wyl.zodiac.springboot.autoconfig.security;

import com.wyl.zodiac.core.security.SecurityAccess;
import com.wyl.zodiac.web.filter.ParamsVerifyFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

/**
* @Title: ParamsVerifyAutoConfiguration
* @Package com.wyl.zodiac.springboot.autoconfig.security
* @Description: 参数校验过滤器自动装配
* @author
* @date 2017/8/22 08:47
* @version V1.0   
*/
@Configuration
@EnableConfigurationProperties(ParamsVerifyProperties.class)
@ConditionalOnProperty(prefix = "zodiac.security.verify", name = "enable")
public class ParamsVerifyAutoConfiguration {

    @Autowired
    private ParamsVerifyProperties paramsVerifyProperties;

    /**
     * 创建访问控制实体
     * @return
     * @author
     * @date 2017年08月22日10:43:34
     */
    @Bean
    @ConditionalOnMissingBean
    public SecurityAccess securityAccess () {
        return new SecurityAccess();
    }

    /**
     * 创建访问控制拦截器
     * @return
     * @author
     * @date 2017年08月22日10:43:38
     */
    @Bean
    @ConditionalOnMissingBean
    public ParamsVerifyFilter paramsVerifyInterceptor() {
        ParamsVerifyFilter paramsVerifyFilter = new ParamsVerifyFilter();
        if (paramsVerifyProperties.getRequestTimeDelaySeconds() != null) {
            paramsVerifyFilter.setRequestTimeDelaySeconds(paramsVerifyProperties.getRequestTimeDelaySeconds());
        }
        if (!CollectionUtils.isEmpty(paramsVerifyProperties.getExcludeUrlPatterns())) {
            paramsVerifyFilter.setExcludeUrlPatterns(paramsVerifyProperties.getExcludeUrlPatterns());
        }
        return paramsVerifyFilter;
    }


}
