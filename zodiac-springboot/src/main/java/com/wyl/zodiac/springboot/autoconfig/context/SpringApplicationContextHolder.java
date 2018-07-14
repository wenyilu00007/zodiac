package com.wyl.zodiac.springboot.autoconfig.context;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author DINGYONG
 * @version 1.5
 * @title  SpringApplicationContextHolder
 * @package com.wyl.zodiac.springboot.autoconfig.context
 * @description 
 * @date 2018/2/2
 */
public class SpringApplicationContextHolder implements ApplicationContextAware {

    private ApplicationContext context;

    public ApplicationContext getContext() {
        return context;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}