package com.drouter.api.thread;

import android.content.Context;
import com.drouter.api.extra.ActionWrapper;
import com.drouter.api.result.ActionCallback;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * description:
 */
public final class ActionPost {
    private final static List<ActionPost> pendingPostPool = new ArrayList<ActionPost>();

    public Context context;
    public ActionWrapper actionWrapper;
    public Map<String, Object> params;
    public ActionCallback actionCallback;
    ActionPost next;

    private ActionPost(ActionWrapper actionWrapper, Context context, Map<String, Object> params, ActionCallback actionCallback) {
        this.context = context;
        this.actionWrapper = actionWrapper;
        this.params = params;
        this.actionCallback = actionCallback;
    }

    public static ActionPost obtainActionPost(ActionWrapper actionWrapper, Context context, Map<String, Object> params, ActionCallback actionCallback) {
        synchronized (pendingPostPool) {
            int size = pendingPostPool.size();
            if (size > 0) {
                ActionPost actionPost = pendingPostPool.remove(size - 1);
                actionPost.context = context;
                actionPost.actionWrapper = actionWrapper;
                actionPost.params = params;
                actionPost.next = null;
                actionPost.actionCallback = actionCallback;
                return actionPost;
            }
        }
        return new ActionPost(actionWrapper, context, params, actionCallback);
    }

    /**
     * 释放post
     * 置为空闲状态
     */
    public void releasePendingPost() {
        this.context = null;
        this.actionWrapper = null;
        this.next = null;
        this.actionCallback = null;
        synchronized (pendingPostPool) {
            // Don't let the pool grow indefinitely  不要让池子无限增长
            if (pendingPostPool.size() < 10000) {
                pendingPostPool.add(this);
            }
        }
    }
}
