package com.drouter.api.action;

import android.content.Context;

import com.drouter.api.result.RouterResult;

import java.util.Map;

/**
 * description:
 */
public interface IRouterAction {
    // 执行 Action 方法
    RouterResult invokeAction(Context context, Map<String, Object> requestData);
}
