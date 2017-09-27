package com.hoau.zodiac.springboot.autoconfig.cas;

import com.alibaba.fastjson.JSON;
import com.hoau.zodiac.web.response.Response;
import org.apache.http.entity.ContentType;
import org.jasig.cas.client.authentication.AuthenticationRedirectStrategy;
import org.jasig.cas.client.util.CommonUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
* @Title: ZodiacAuthenticationRedirectStrategy
* @Package com.hoau.framework.module.login.server.cas 
* @Description: 自定义CAS校验连接器跳转规则
* @author 陈宇霖  
* @date 2017/7/19 21:46
* @version V1.0   
*/
public class ZodiacAuthenticationRedirectStrategy implements AuthenticationRedirectStrategy {

    private String alwaysRedirectServerUrl;

    /**
     * 由于前端框架中使用的是iframe，菜单中是个独立的界面，
     * 如果使用默认的重定向策略，当用户掉线时点击到菜单，跳转到cas登陆界面后，重定向的页面为iframe内部界面，导致前端框架错误，
     * 所以此处需要将重定向的地址修改掉
     * @param request
     * @param response
     * @param potentialRedirectUrl
     * @throws IOException
     */
    public void redirect(final HttpServletRequest request, final HttpServletResponse response,
                         final String potentialRedirectUrl) throws IOException {
        //只有配置了重定向地址的才进行处理
        response.setContentType(ContentType.APPLICATION_JSON.toString());
        Response redirectResponse = new Response();
        redirectResponse.setErrorCode(Response.ERROR_REDIRECT);
        if (CommonUtils.isBlank(alwaysRedirectServerUrl)) {
//            response.sendRedirect(potentialRedirectUrl);
            redirectResponse.setResult(potentialRedirectUrl);
        } else {
            //入参中的potentialRedirectUrl是由AuthenticationFilter拼接过的，拼接的逻辑如下，所以要进行拆分，将原来的跳转地址抽取出来换掉
//        casServerLoginUrl + (casServerLoginUrl.contains("?") ? "&" : "?") + serviceParameterName + "="
//                + urlEncode(serviceUrl) + (renew ? "&renew=true" : "") + (gateway ? "&gateway=true" : "");
            String finalRedirectUrl = potentialRedirectUrl;
            String casServerLoginUrl; //cas服务器登陆地址
            String serviceParameterName; //重定向地址参数名称(默认为:service)
            boolean gateway = potentialRedirectUrl.contains("&gateway=true");
            boolean renew = potentialRedirectUrl.contains("&renew=true");
            if (gateway) {
                finalRedirectUrl = potentialRedirectUrl.substring(0, potentialRedirectUrl.lastIndexOf("&gateway=true"));
            }
            if (renew) {
                finalRedirectUrl = finalRedirectUrl.substring(0, finalRedirectUrl.lastIndexOf("&renew=true"));
            }
            //判断cas服务器登陆地址是否有自己默认的参数
            if (finalRedirectUrl.contains("&")) {
                casServerLoginUrl = finalRedirectUrl.substring(0, finalRedirectUrl.lastIndexOf("&") + 1);
                String redirectFont = finalRedirectUrl.substring(finalRedirectUrl.lastIndexOf("&") + 1);
                serviceParameterName = redirectFont.substring(0, redirectFont.indexOf("="));
            } else {
                casServerLoginUrl = finalRedirectUrl.substring(0, finalRedirectUrl.lastIndexOf("?") + 1);
                String redirectFont = finalRedirectUrl.substring(finalRedirectUrl.lastIndexOf("?") + 1);
                serviceParameterName = redirectFont.substring(0, redirectFont.indexOf("="));
            }
            finalRedirectUrl = casServerLoginUrl + serviceParameterName + "=" + CommonUtils.urlEncode(alwaysRedirectServerUrl)
                    + (renew ? "&renew=true" : "") + (gateway ? "&gateway=true" : "");
//            response.sendRedirect(finalRedirectUrl);
            redirectResponse.setResult(finalRedirectUrl);
        }
        PrintWriter writer = response.getWriter();
        writer.write(JSON.toJSONString(redirectResponse));
    }

    public String getAlwaysRedirectServerUrl() {
        return alwaysRedirectServerUrl;
    }

    public void setAlwaysRedirectServerUrl(String alwaysRedirectServerUrl) {
        this.alwaysRedirectServerUrl = alwaysRedirectServerUrl;
    }
}
