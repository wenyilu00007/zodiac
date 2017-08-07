package com.hoau.zodiac.web.context;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
* @Title: ApplicationContextHolder 
* @Package com.hoau.leo.common.context 
* @Description: 提供静态方法获取ApplicationContext的类
* @author 陈宇霖  
* @date 2017/8/4 09:18
* @version V1.0   
*/
public class ApplicationContextHolder implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (ApplicationContextHolder.applicationContext == null) {
            ApplicationContextHolder.applicationContext = applicationContext;
        }
    }

    /**
     * 获取spring applicationContext
     *
     * @return
     * @author 陈宇霖
     * @date 2017年08月04日09:14:46
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 通过name获取 Bean
     *
     * @author 陈宇霖
     * @date 2017年08月04日09:15:20
     */
    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    /**
     * 通过class获取Bean
     *
     * @author 陈宇霖
     * @date 2017年08月04日09:15:20
     */
    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    /**
     * 通过name,以及Clazz返回指定的Bean
     *
     * @author 陈宇霖
     * @date 2017年08月04日09:15:20
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }
    
}
