package com.hoau.zodiac.springboot.autoconfig.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
* @Title: SecurityAccessProperties 
* @Package com.hoau.zodiac.springboot.autoconfig.security 
* @Description: 资源访问权限控制配置
* @author 陈宇霖  
* @date 2017/9/11 14:53
* @version V1.0   
*/
@ConfigurationProperties(prefix = "zodiac.security.accessControl")
public class SecurityAccessProperties {

    /**
     * 是否启用资源访问权限控制拦截器
     */
    private boolean enable;

    /**
     * 如果访问资源未配置权限，是否默认允许访问
     */
    private boolean ignoreNoneConfigUri;

    /**
     * 需要进行控制的系统编码
     */
    private String systemCode;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public boolean isIgnoreNoneConfigUri() {
        return ignoreNoneConfigUri;
    }

    public void setIgnoreNoneConfigUri(boolean ignoreNoneConfigUri) {
        this.ignoreNoneConfigUri = ignoreNoneConfigUri;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }
}
