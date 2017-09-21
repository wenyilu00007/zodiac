package com.hoau.zodiac.springboot.autoconfig.ftp;

import com.hoau.zodiac.core.util.FtpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
* @Title: FtpAutoConfiguration 
* @Package com.hoau.zodiac.springboot.autoconfig.ftp 
* @Description: ftp客户端自动装配
* @author 陈宇霖  
* @date 2017/9/21 11:50
* @version V1.0   
*/
@Configuration
@EnableConfigurationProperties(FtpConfiguration.class)
@ConditionalOnProperty(prefix = "zodiac.ftp", name = "enable")
public class FtpAutoConfiguration {

    @Autowired
    private FtpConfiguration ftpConfiguration;

    /**
     * 用户操作ftp服务器的工具类自动装配
     * @return
     * @author 陈宇霖
     * @date 2017年09月21日11:59:07
     */
    @Bean
    @ConditionalOnMissingBean
    public FtpUtils ftpUtils() {
        return new FtpUtils(ftpConfiguration.getHost(),
                ftpConfiguration.getPort(),
                ftpConfiguration.getUsername(),
                ftpConfiguration.getPassword());
    }
}
