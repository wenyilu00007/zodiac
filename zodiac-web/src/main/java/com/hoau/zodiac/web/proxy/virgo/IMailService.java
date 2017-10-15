package com.hoau.zodiac.web.proxy.virgo;

import com.hoau.zodiac.web.proxy.virgo.dto.MailEntity;
import com.hoau.zodiac.web.proxy.Constants;
import com.hoau.zodiac.web.response.Response;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
* @Title: ISendMailService 
* @Package com.hoau.libra.proxy.virgo 
* @Description: 发送邮件服务
* @author 陈宇霖  
* @date 2017/9/26 23:51
* @version V1.0   
*/
@FeignClient(value = Constants.VIRGO_SEND_MAIL_SERVICE_NAME)
public interface IMailService {

    /**
     * 发送异步邮件
     * @param mailEntity
     * @return
     * @author 陈宇霖
     * @date 2017年09月26日23:57:16
     */
    @RequestMapping(value = Constants.VIRGO_SEND_ASYNC_MAIL_SERVICE_URL,
            method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    Response sendMailAsync(@RequestBody MailEntity mailEntity);

    /**
     * 发送同步邮件
     * @param mailEntity
     * @return
     * @author 陈宇霖
     * @date 2017年09月26日23:57:16
     */
    @RequestMapping(value = Constants.VIRGO_SEND_SYNC_MAIL_SERVICE_URL, method = RequestMethod.POST)
    Response sendMailSync(@RequestBody MailEntity mailEntity);

}
