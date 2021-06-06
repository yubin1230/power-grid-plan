package com.power.grid.plan;

import java.util.Optional;

/**
 * 请求戳
 * @author yubin
 * @date 2021/5/30 16:24
 */
public class LogUtil {


    private static final ThreadLocal<String> REQUEST_IDENTIFIER=new ThreadLocal<>();

    private LogUtil() throws IllegalAccessException {
        throw new IllegalAccessException("Utility Class");
    }

    public static String getRequestNo(){
        return Optional.ofNullable(REQUEST_IDENTIFIER.get()).orElse("");
    }

    public static void requestStart(String requestNo){
        LogUtil.requestEnd();
        REQUEST_IDENTIFIER.set(Optional.ofNullable(requestNo).orElse(""));
    }

    public static void requestEnd(){
        REQUEST_IDENTIFIER.remove();
    }

}
