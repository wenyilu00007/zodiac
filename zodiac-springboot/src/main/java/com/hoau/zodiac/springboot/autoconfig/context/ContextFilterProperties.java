package com.hoau.zodiac.springboot.autoconfig.context;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
* @Title: ContextFilterProperties 
* @Package com.hoau.zodiac.springboot.autoconfig.context 
* @Description: context过滤器配置参数
* @author 陈宇霖  
* @date 2017/8/17 09:47
* @version V1.0   
*/
@ConfigurationProperties(prefix = "zodiac.web.context")
public class ContextFilterProperties {

    /**
     * 是否启用过滤器
     */
    private boolean enable;

    /**
     * 是否启用通过网关，gatewayEnable和enable不能同时为true，否则用户信息读取会有问题
     */
    private boolean gatewayEnable;

    /**
     * 启用网关情况下，服务器登陆地址
     */
    private String gatewayLogoutServerUrl;

    /**
     * 启用网关情况下，登出后重定向地址
     */
    private String gatewayLogoutRedirectUrl;

    /**
     * 不进行过滤的地址列表
     */
    private List<String> excludeUrlPatterns;

    public boolean isEnable() {
        return enable;
    }

    public boolean isGatewayEnable() {
        return gatewayEnable;
    }

    public void setGatewayEnable(boolean gatewayEnable) {
        this.gatewayEnable = gatewayEnable;
    }

    public String getGatewayLogoutServerUrl() {
        return gatewayLogoutServerUrl;
    }

    public void setGatewayLogoutServerUrl(String gatewayLogoutServerUrl) {
        this.gatewayLogoutServerUrl = gatewayLogoutServerUrl;
    }

    public String getGatewayLogoutRedirectUrl() {
        return gatewayLogoutRedirectUrl;
    }

    public void setGatewayLogoutRedirectUrl(String gatewayLogoutRedirectUrl) {
        this.gatewayLogoutRedirectUrl = gatewayLogoutRedirectUrl;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public List<String> getExcludeUrlPatterns() {
        return excludeUrlPatterns;
    }

    public void setExcludeUrlPatterns(List<String> excludeUrlPatterns) {
        this.excludeUrlPatterns = excludeUrlPatterns;
    }
}
