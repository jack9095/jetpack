package com.example.circle_module;

import android.content.Context;
import android.content.Intent;

import com.drouter.api.action.IRouterAction;
import com.drouter.api.result.RouterResult;
import com.drouter.base.ThreadMode;
import com.drouter.base.annotation.Action;

import java.util.Map;

/**
 * description:
 */
@Action(path = "circlemodule/test", threadMode = ThreadMode.MAIN)
public class CircleAction implements IRouterAction {
    @Override
    public RouterResult invokeAction(Context context, Map<String, Object> requestData) {
        Intent intent = new Intent(context, CircleActivity.class);
        intent.putExtra("key", (String) requestData.get("key"));
        context.startActivity(intent);
        return new RouterResult.Builder().success().build();
    }
}
