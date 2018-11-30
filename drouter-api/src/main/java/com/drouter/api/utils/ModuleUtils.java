package com.drouter.api.utils;

import android.content.Context;
import android.widget.Toast;

import com.drouter.api.extra.Consts;
import com.drouter.api.extra.ILogger;

import java.util.List;

public class ModuleUtils {

    /**
     * 显示 debug 信息
     *
     * @param message
     */
    public static void debugMessage(String message,boolean debuggable,ILogger logger,Context mApplicationContext) {
        if (debuggable) {
            logger.e(Consts.TAG, message);
            ModuleUtils.showToast(message,mApplicationContext);
        }
    }

    /**
     * 打印显示 Toast
     *
     * @param message
     */
    public static void showToast(String message,Context mApplicationContext) {
        Toast.makeText(mApplicationContext, message, Toast.LENGTH_LONG).show();
    }

    /**
     * 根据 moduleName 查询 module 的全类名
     *
     * @param moduleName
     * @return
     */
    public static String searchModuleClassName(String moduleName,List<String> mAllModuleClassName) {
        for (String moduleClassName : mAllModuleClassName) {
            if (moduleClassName.contains(moduleName)) {
                return moduleClassName;
            }
        }
        return null;
    }

}
