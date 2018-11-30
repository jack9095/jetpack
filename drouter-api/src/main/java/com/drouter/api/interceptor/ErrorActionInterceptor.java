package com.drouter.api.interceptor;


import com.drouter.api.extra.ErrorActionWrapper;
import com.drouter.api.thread.ActionPost;

/**
 * description: 错误的 Action 拦截器，放在拦截链的第一个位置
 */
public class ErrorActionInterceptor implements ActionInterceptor {
    @Override
    public void intercept(ActionChain chain) {
        ActionPost actionPost = chain.action();
        // 拦截错误
        if (actionPost.actionWrapper instanceof ErrorActionWrapper) {
            chain.onInterrupt();
        }

        // 继续分发
        chain.proceed(actionPost);
    }
}
