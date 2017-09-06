package com.hoau.zodiac.log.mybatis;

import org.apache.ibatis.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
* @Title: MybatisLogbackLoggerImpl 
* @Package com.hoau.zodiac.log.mybatis 
* @Description: 使用logback记录mybatis日志实现
* @author 陈宇霖  
* @date 2017/9/6 08:08
* @version V1.0   
*/
public class MybatisLogbackLoggerImpl implements Log {

    static Logger log = LoggerFactory.getLogger(MybatisLogbackLoggerImpl.class);

    public boolean isDebugEnabled() {
        return true;
    }

    public boolean isTraceEnabled() {
        return true;
    }

    public void error(String s, Throwable throwable) {
        log.error(s, throwable);
    }

    public void error(String s) {
        log.error(s);
    }

    public void debug(String s) {
        log.debug(s);
    }

    public void trace(String s) {
        log.trace(s);
    }

    public void warn(String s) {
        log.warn(s);
    }
}
