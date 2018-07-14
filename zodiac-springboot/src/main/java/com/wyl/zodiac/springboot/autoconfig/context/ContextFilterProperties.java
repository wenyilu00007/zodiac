package com.wyl.zodiac.springboot.autoconfig.context;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
* @Title: ContextFilterProperties 
* @Package com.wyl.zodiac.springboot.autoconfig.context
* @Description: context过滤器配置参数
* @author
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
     * 不启用网关情况下，登出后重定向地址
     */
    private String withoutCasOfflineRedirectUrl;

    /**
     * 不进行过滤的地址列表
     */
    private List<String> excludeUrlPatterns;

    /**
     * 基于cookie登陆保持的配置
     */
    private CookieBasedProperties cookieBasedProperties;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
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

    public String getWithoutCasOfflineRedirectUrl() {
        return withoutCasOfflineRedirectUrl;
    }

    public void setWithoutCasOfflineRedirectUrl(String withoutCasOfflineRedirectUrl) {
        this.withoutCasOfflineRedirectUrl = withoutCasOfflineRedirectUrl;
    }

    public List<String> getExcludeUrlPatterns() {
        return excludeUrlPatterns;
    }

    public void setExcludeUrlPatterns(List<String> excludeUrlPatterns) {
        this.excludeUrlPatterns = excludeUrlPatterns;
    }

    public CookieBasedProperties getCookieBasedProperties() {
        return cookieBasedProperties;
    }

    public void setCookieBasedProperties(CookieBasedProperties cookieBasedProperties) {
        this.cookieBasedProperties = cookieBasedProperties;
    }
}
