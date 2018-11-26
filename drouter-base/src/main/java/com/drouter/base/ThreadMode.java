package com.drouter.base;

/**
 * description: thanks EventBus
 * author: Darren on 2018/1/23 08:57
 * email: 240336124@qq.com
 * version: 1.0
 */
public enum ThreadMode {
    /**
     * Action will be called directly in the same thread, which is posting the event. This is the default. Event delivery
     * implies the least overhead because it avoids thread switching completely. Thus this is the recommended mode for
     * simple tasks that are known to complete in a very short time without requiring the main thread. Event handlers
     * using this mode must return quickly to avoid blocking the posting thread, which may be the main thread.
     *
     * 事件的处理在和事件的发送在相同的进程，所以事件处理时间不应太长，不然影响事件的发送线程，而这个线程可能是UI线程
     */
    POSTING,

    /**
     * On Android, action will be called in Android's main thread (UI thread). If the posting thread is
     * the main thread, action methods will be called directly, blocking the posting thread. Otherwise the event
     * is queued for delivery (non-blocking). Action using this mode must return quickly to avoid blocking the main thread.
     * If not on Android, behaves the same as {@link #POSTING}.
     * 主线程
     */
    MAIN,

    /**
     * On Android, action will be called in a background thread. If posting thread is not the main thread, action methods
     * will be called directly in the posting thread. If the posting thread is the main thread, EventBus uses a single
     * background thread, that will deliver all its events sequentially. Action using this mode should try to
     * return quickly to avoid blocking the background thread. If not on Android, always uses a background thread.
     *
     * 事件的处理会在一个后台线程中执行，尽管是在后台线程中运行，事件处理时间不应太长。如果事件分发在主线程，件会被加到一个队列中，
     * 由一个线程依次处理这些事件，如果某个事件处理时间太长，会阻塞后面的事件的派发或处理。如果事件分发在后台线程，事件会立即执行处理
     *
     */
    BACKGROUND,

    /**
     * Action will be called in a separate thread. This is always independent from the posting thread and the
     * main thread. Posting events never wait for action methods using this mode. Action methods should
     * use this mode if their execution might take some time, e.g. for network access. Avoid triggering a large number
     * of long running asynchronous action methods at the same time to limit the number of concurrent threads. EventBus
     * uses a thread pool to efficiently reuse threads from completed asynchronous action notifications.
     *
     * 事件处理会在单独的线程中执行，主要用于在后台线程中执行耗时操作，每个事件会开启一个线程（有线程池），但最好限制线程的数目
     */
    ASYNC
}
