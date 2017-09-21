package com.hoau.zodiac.springboot.autoconfig.ftp;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
* @Title: FtpConfiguration 
* @Package com.hoau.zodiac.springboot.autoconfig.ftp 
* @Description: ftp服务器配置
* @author 陈宇霖  
* @date 2017/9/21 11:43
* @version V1.0   
*/
@ConfigurationProperties("zodiac.ftp")
public class FtpConfiguration {

    /**
     * 是否需要创建ftp工具
     */
    private boolean enable;

    /**
     * 服务器地址
     */
    private String host;

    /**
     * 服务器端口
     */
    private int port;

    /**
     * 用户登录名
     */
    private String username;

    /**
     * 用户登录密码
     */
    private String password;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
