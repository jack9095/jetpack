package com.drouter.api.exception;

/**
 * description: Router Action Exception  路由执行异常
 */
public class RouterActionException extends RuntimeException{
    public RouterActionException(String message) {
        super(message);
    }
}
