package com.wyl.zodiac.springboot.autoconfig.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
* @Title: ParamsVerifyProperties 
* @Package com.wyl.zodiac.springboot.autoconfig.security
* @Description: 参数校验过滤器配置
* @author
* @date 2017/8/22 23:05
* @version V1.0   
*/
@ConfigurationProperties(prefix = "zodiac.security.verify")
public class ParamsVerifyProperties {

    /**
     * 是否启用参数校验
     */
    private boolean enable;

    /**
     * 请求延迟最大时间(秒) 默认180秒
     */
    private Long requestTimeDelaySeconds = 180L;

    /**
     * 不进行参数校验的请求
     */
    private List<String> excludeUrlPatterns;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public Long getRequestTimeDelaySeconds() {
        return requestTimeDelaySeconds;
    }

    public void setRequestTimeDelaySeconds(Long requestTimeDelaySeconds) {
        this.requestTimeDelaySeconds = requestTimeDelaySeconds;
    }

    public List<String> getExcludeUrlPatterns() {
        return excludeUrlPatterns;
    }

    public void setExcludeUrlPatterns(List<String> excludeUrlPatterns) {
        this.excludeUrlPatterns = excludeUrlPatterns;
    }
}
