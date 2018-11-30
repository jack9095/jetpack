package com.drouter.api.action;

import com.drouter.api.extra.ActionWrapper;

/**
 * description:
 */
public interface IRouterModule {
    // 通过 Action 的名称找到 Action
    ActionWrapper findAction(String actionName);
}
