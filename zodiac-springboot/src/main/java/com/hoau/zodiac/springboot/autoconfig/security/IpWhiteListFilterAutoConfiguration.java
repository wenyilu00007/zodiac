package com.hoau.zodiac.springboot.autoconfig.security;

import com.hoau.zodiac.web.filter.IpWhiteListFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

/**
* @Title: IpWhiteListFilterAutoConfiguration
* @Package com.hoau.zodiac.springboot.autoconfig.security 
* @Description: ip白名单过滤器自动装配
* @author 陈宇霖  
* @date 2017/8/18 12:05
* @version V1.0   
*/
@Configuration
@EnableConfigurationProperties(IpWhiteListProperties.class)
@ConditionalOnProperty(prefix = "zodiac.security.ip.whitelist", name = "enable")
public class IpWhiteListFilterAutoConfiguration {

    @Autowired
    private IpWhiteListProperties ipWhiteListProperties;

    /**
     * 创建ip白名单过滤器
     * @return
     * @author 陈宇霖
     * @date 2017年08月18日14:06:27
     */
    @Bean
    public IpWhiteListFilter ipWhiteListFilter() {
        IpWhiteListFilter ipWhiteListFilter = new IpWhiteListFilter();
        if (!CollectionUtils.isEmpty(ipWhiteListProperties.getWhiteList())) {
            ipWhiteListFilter.setWhiteList(ipWhiteListProperties.getWhiteList());
        }
        return ipWhiteListFilter;
    }
}
