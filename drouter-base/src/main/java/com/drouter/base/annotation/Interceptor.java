package com.drouter.base.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * description: 拦截器
 */
@Target({ElementType.TYPE})   // 作用在类上
@Retention(RetentionPolicy.CLASS)  // 编译时执行注解
public @interface Interceptor {
    int priority(); // 优先级
}
