package com.wyl.zodiac.springboot.autoconfig.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
* @Title: IpWhiteListProperties 
* @Package com.wyl.zodiac.springboot.autoconfig.security
* @Description: 白名单过滤器配置参数
* @author
* @date 2017/8/18 12:06
* @version V1.0   
*/
@ConfigurationProperties(prefix = "zodiac.security.ip.whitelist")
public class IpWhiteListProperties {

    /**
     * 是否启动白名单过滤
     */
    private boolean enable;

    /**
     * ip白名单列表配置
     */
    private List<String> whiteList;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public List<String> getWhiteList() {
        return whiteList;
    }

    public void setWhiteList(List<String> whiteList) {
        this.whiteList = whiteList;
    }
}
