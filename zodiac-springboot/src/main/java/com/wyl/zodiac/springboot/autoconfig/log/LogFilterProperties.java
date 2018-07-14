package com.wyl.zodiac.springboot.autoconfig.log;

import java.util.List;

/**
* @Title: LogFilterProperties 
* @Package com.wyl.leo.config.log
* @Description: 日志拦截器相关配置
* @author
* @date 2017/8/2 17:54
* @version V1.0   
*/
public class LogFilterProperties {

    /**
     * 是否开启日志拦截器
     */
    private boolean enable = true;

    /**
     * 是否记录请求日志
     */
    private boolean needLogRequest = true;

    /**
     * 是否记录响应日志
     */
    private boolean needLogResponse = true;

    /**
     * 是否记录header
     */
    private boolean needLogHeader = true;

    /**
     * 是否记录参数
     */
    private boolean needLogPayload = true;

    /**
     * 记录的最大payload大小
     */
    private int maxPayloadLength = 2*1024*1024;

    /**
     * 不记录日志的地址
     */
    private List<String> excludeUrlPatterns;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public boolean isNeedLogRequest() {
        return needLogRequest;
    }

    public void setNeedLogRequest(boolean needLogRequest) {
        this.needLogRequest = needLogRequest;
    }

    public boolean isNeedLogResponse() {
        return needLogResponse;
    }

    public void setNeedLogResponse(boolean needLogResponse) {
        this.needLogResponse = needLogResponse;
    }

    public boolean isNeedLogHeader() {
        return needLogHeader;
    }

    public void setNeedLogHeader(boolean needLogHeader) {
        this.needLogHeader = needLogHeader;
    }

    public boolean isNeedLogPayload() {
        return needLogPayload;
    }

    public void setNeedLogPayload(boolean needLogPayload) {
        this.needLogPayload = needLogPayload;
    }

    public int getMaxPayloadLength() {
        return maxPayloadLength;
    }

    public void setMaxPayloadLength(int maxPayloadLength) {
        this.maxPayloadLength = maxPayloadLength;
    }

    public List<String> getExcludeUrlPatterns() {
        return excludeUrlPatterns;
    }

    public void setExcludeUrlPatterns(List<String> excludeUrlPatterns) {
        this.excludeUrlPatterns = excludeUrlPatterns;
    }
}
