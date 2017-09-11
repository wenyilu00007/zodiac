package com.hoau.zodiac.core.util.date;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;

/**
* @Title: NewDateUtils 
* @Package com.hoau.zodiac.core.util.date 
* @Description: jdk8的日期操作工具类
* @author 陈宇霖  
* @date 2017/9/6 11:32
* @version V1.0   
*/
public class NewDateUtils {

    public static void main(String[] args) {
        Date date = new Date();
        System.out.println(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(date));
        System.out.println(date);
        Instant instant = date.toInstant();
        date.getTimezoneOffset();
        LocalDateTime localDateTime = LocalDateTime.now();
    }

}
