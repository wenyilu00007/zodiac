package com.hoau.zodiac.springboot.autoconfig.log;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
* @Title: LogProperties 
* @Package com.hoau.leo.config.log 
* @Description: 日志配置
* @author 陈宇霖  
* @date 2017/8/2 16:55
* @version V1.0   
*/
@ConfigurationProperties(prefix = "zodiac.log")
public class LogProperties {

    /**
     * 请求日志拦截器相关配置
     */
    private LogFilterProperties filter;

    public LogFilterProperties getFilter() {
        return filter;
    }

    public void setFilter(LogFilterProperties filter) {
        this.filter = filter;
    }
}
