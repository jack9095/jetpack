package com.drouter.api.action;

import com.drouter.api.interceptor.ActionInterceptor;

import java.util.List;

/**
 * description: 路由拦截器
 */
public interface IRouterInterceptor {
    // 通过 Action 的名称找到 Action
    List<ActionInterceptor> getInterceptors();
}
