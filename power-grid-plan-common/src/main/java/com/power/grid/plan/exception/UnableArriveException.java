package com.power.grid.plan.exception;

/**
 * 开始节点、结束节点无法到达
 * @author yubin
 * @date 2020/12/6 0:29
 */
public class UnableArriveException extends RuntimeException{

    public UnableArriveException(String message){
        super(message);
    }
}
