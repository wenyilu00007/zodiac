package com.wyl.zodiac.springboot.autoconfig.context;

/**
* @Title CookieBasedProperties
* @Package com.wyl.zodiac.springboot.autoconfig.context
* @Description 基于cookie登陆保持context过滤器配置
* @author
* @date 2017/11/3 14:49
* @version V1.0   
*/
public class CookieBasedProperties {

    /**
     * 是否是基于Cookie做的登陆保持
     */
    private boolean enable;

    /**
     * cookie的名称
     */
    private String cookieName;

    /**
     * 项目名称
     */
    private String applicationId;

    /**
     * cookie超时时间
     */
    private int expireTime = 30 * 60;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getCookieName() {
        return cookieName;
    }

    public void setCookieName(String cookieName) {
        this.cookieName = cookieName;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public int getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(int expireTime) {
        this.expireTime = expireTime;
    }
}
