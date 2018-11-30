package com.drouter.api.extra;

/**
 * description: 日志接口
 */
public interface ILogger {
    void showLog(boolean isShowLog);

    void d(String tag, String message);

    void i(String tag, String message);

    void w(String tag, String message);

    void e(String tag, String message);
}
