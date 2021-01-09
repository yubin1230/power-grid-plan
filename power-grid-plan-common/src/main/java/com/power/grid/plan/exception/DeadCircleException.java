package com.power.grid.plan.exception;

/**
 * 死循环异常
 * @author yubin
 * @date 2020/12/6 0:29
 */
public class DeadCircleException extends RuntimeException{

    public DeadCircleException(String message){
        super(message);
    }
}
