package com.wyl.zodiac.web.request;

import org.apache.commons.lang.CharEncoding;

import java.io.UnsupportedEncodingException;
import java.util.Random;

/**
* @Title CookieToken 
* @Package com.wyl.zodiac.web.request
* @Description 
* @author
* @date 2017/11/3 15:31
* @version V1.0   
*/
public class CookieToken {
    private static Random random = new Random(System.currentTimeMillis());
    /**
     * 用户统一编号，比如：工号，或者昵称等
     */
    private String userId;
    /**
     * 会话ID
     */
    private String sessionId;
    /**
     * 系统ID
     */
    private String applicationId;

    /**
     * 标识token的唯一ID
     */
    private String uuid;

    /**
     * 失效时间
     */
    private int expireTime;

    /**
     * 登陆令牌的构造函数
     *
     * @param userId
     * @param sessionId
     * @param applicationId
     * @param uuid
     */
    public CookieToken(String userId, String sessionId, String applicationId,String uuid) {
        this.userId = userId;
        this.sessionId = sessionId;
        this.applicationId = applicationId;
        this.uuid=uuid;
    }

    /**
     * byte[]数组的内容复制到Token中
     *
     * @param tokenBytes
     */
    public CookieToken(byte[] tokenBytes) {
        try {
            String token = new String(tokenBytes, CharEncoding.UTF_8);
            String[] keys = token.split(",");
            this.setUserId(keys[0]);
            this.setSessionId(keys[1]);
            this.applicationId = keys[2];
            this.uuid = keys[3];
            this.expireTime = Integer.parseInt(keys[4]);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 返回该对象的字符串表示
     *
     * @return
     */
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer(8);
        sb.append(getUserId()).append(",");
        sb.append(getSessionId()).append(",");
        sb.append(getApplicationId()).append(",");
        sb.append(getUuid()).append(",");
        sb.append(getExpireTime());
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CookieToken) {
            CookieToken objToken = (CookieToken) obj;
            if (objToken.toString().equals(this.toString())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return  this.toString().hashCode();
    }

    /**
     * 返回该对象的byte[]数组表示
     *
     * @return
     * @see
     */
    public byte[] toBytes() {
        try {
            return this.toString().getBytes(CharEncoding.UTF_8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * token是否过期
     * 		1.expireTime >= currentTime 未过期返回false
     * 		2.expireTime < currentTime 已过期返回true
     *
     * @return 1.expireTime >= currentTime 未过期返回false 2.expireTime < currentTime 已过期返回true
     */
    public boolean expired() {
        long millisecond = this.getExpireTime();
        long currentMs = System.currentTimeMillis();
        return millisecond < currentMs;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(int expireTime) {
        this.expireTime = expireTime;
    }
}
