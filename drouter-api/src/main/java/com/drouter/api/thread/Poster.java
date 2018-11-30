package com.drouter.api.thread;

/**
 * description: 异步
 */
public interface Poster {
    void enqueue(ActionPost actionPost);
}
