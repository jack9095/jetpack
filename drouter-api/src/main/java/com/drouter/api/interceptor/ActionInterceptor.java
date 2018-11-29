package com.drouter.api.interceptor;

import com.drouter.api.thread.ActionPost;

/**
 * description: 拦截器
 */
public interface ActionInterceptor {
    void intercept(ActionChain chain);

    interface ActionChain {
        // 打断拦截
        void onInterrupt();

        // 分发给下一个拦截器
        void proceed(ActionPost actionPost);

        // 获取 ActionPost
        ActionPost action();

        String actionPath();
    }
}
