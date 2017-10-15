package com.hoau.zodiac.web.proxy;
/**   
* @Title: Constants 
* @Package com.hoau.zodiac.web.proxy
* @Description: 通用服务调用相关常量
* @author 陈宇霖  
* @date 2017/10/14 10:42
* @version V1.0   
*/
public class Constants {

    /**
     * 邮件发送服务名
     */
    public static final String VIRGO_SEND_MAIL_SERVICE_NAME = "virgo-mail";

    //发送异步邮件接口地址
    public static final String VIRGO_SEND_ASYNC_MAIL_SERVICE_URL = "/mails/v1/mail/async";

    //发送同步邮件接口地址
    public static final String VIRGO_SEND_SYNC_MAIL_SERVICE_URL = "/mails/v1/mail/sync";
}
