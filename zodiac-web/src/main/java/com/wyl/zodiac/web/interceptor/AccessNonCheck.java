package com.wyl.zodiac.web.interceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author
 * @version V1.0
 * @Title: AccessNonCheck
 * @Package com.wyl.zodiac.web.interceptor
 * @Description: 不进行菜单访问权限校验的方法
 * @date 2017/8/22 23:30
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AccessNonCheck {
}
