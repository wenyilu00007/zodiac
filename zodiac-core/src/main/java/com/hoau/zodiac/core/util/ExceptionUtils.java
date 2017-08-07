package com.hoau.zodiac.core.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
* @Title: ExceptionUtil 
* @Package com.hoau.hbdp.framework.shared.util 
* @Description: 异常处理工具类
* @author 陈宇霖  
* @date 2017/6/26 16:05
* @version V1.0   
*/
public class ExceptionUtils {

    /**
     * 从异常中读取堆栈信息
     *
     * @param iex
     * @return
     * @author 陈宇霖
     * @date 2017年06月26日16:07:13
     */
    public static String getStack(Throwable iex) {
        if (iex == null) {
            return "";
        }
        ByteArrayOutputStream buf = null;
        try {
            buf = new ByteArrayOutputStream();
            iex.printStackTrace(new java.io.PrintWriter(buf, true));
            return buf.toString();
        } finally {
            try {
                if (buf != null) {
                    buf.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
