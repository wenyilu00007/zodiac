package com.hoau.zodiac.springboot.autoconfig.cas;

import org.jasig.cas.client.authentication.AuthenticationFilter;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import java.lang.reflect.Field;

/**
* @Title: ZodiacAuthenticationFilter 
* @Package com.hoau.zodiac.springboot.autoconfig.cas 
* @Description: 平台实现的cas客户端认证过滤器
* @author 陈宇霖  
* @date 2017/8/29 15:09
* @version V1.0   
*/
public class ZodiacAuthenticationFilter extends AuthenticationFilter {

    private String alwaysRedirectServerUrl;

    @Override
    protected void initInternal(FilterConfig filterConfig) throws ServletException {
        super.initInternal(filterConfig);
        try {
            if (this instanceof ZodiacAuthenticationFilter) {
                Field field = this.getClass().getSuperclass().getDeclaredField("authenticationRedirectStrategy");
                if (field != null) {
                    field.setAccessible(true);
                    Object strategy = field.get(this);
                    if (strategy instanceof ZodiacAuthenticationRedirectStrategy) {
                        ((ZodiacAuthenticationRedirectStrategy)strategy).setAlwaysRedirectServerUrl(alwaysRedirectServerUrl);
                    }
                }
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public String getAlwaysRedirectServerUrl() {
        return alwaysRedirectServerUrl;
    }

    public void setAlwaysRedirectServerUrl(String alwaysRedirectServerUrl) {
        this.alwaysRedirectServerUrl = alwaysRedirectServerUrl;
    }
}
