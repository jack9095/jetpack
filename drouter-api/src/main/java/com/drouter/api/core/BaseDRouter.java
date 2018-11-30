package com.drouter.api.core;

import android.content.Context;

import com.drouter.api.action.IRouterModule;
import com.drouter.api.extra.ActionWrapper;
import com.drouter.api.extra.Consts;
import com.drouter.api.extra.DefaultLogger;
import com.drouter.api.extra.ILogger;
import com.drouter.api.interceptor.ActionInterceptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseDRouter {
     
    /**
     * 是否被初始化
     */
    protected volatile static boolean hasInit = false;
    /**
     * 是否是 debugable 状态
     */
    protected volatile static boolean debuggable = false;
    /**
     * 日志打印
     */
    public volatile static ILogger logger = new DefaultLogger();
    /**
     * 缓存的 RouterAction
     */
    protected volatile static Map<String, ActionWrapper> cacheRouterActions = new HashMap<>();
    /**
     * 缓存的 RouterModule
     */
    protected volatile static Map<String, IRouterModule> cacheRouterModules = new HashMap<>();
    /**
     * 所有 moudle
     */
    protected static List<String> mAllModuleClassName;
    /**
     * 上下文
     */
    protected Context mApplicationContext;
    /**
     * 所有的拦截器
     */
    protected static List<ActionInterceptor> interceptors = new ArrayList<>();

    public static synchronized void openDebug() {
        debuggable = true;
        logger.showLog(true);

        logger.d(Consts.TAG, "DRouter openDebug");
    }

    public static boolean debuggable() {
        return debuggable;
    }
}
