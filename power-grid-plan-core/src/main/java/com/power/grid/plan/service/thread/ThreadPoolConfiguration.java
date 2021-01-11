package com.power.grid.plan.service.thread;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
* 线程池
* @author yubin
* @date 2021/1/10 12:21
*/
@Component
@Configuration(proxyBeanMethods = false)
@ConfigurationProperties(prefix = ThreadPoolConfiguration.PREFIX)
public class ThreadPoolConfiguration {
    public static final String PREFIX = "threads";
    private static final Logger LOGGER = LogManager.getLogger(ThreadPoolConfiguration.class);
    private static final int CORE_SIZE = Runtime.getRuntime().availableProcessors();
    private static final int DEFAULT_CORE_SIZE = 10;
    private static final int DEFAULT_MAX_SIZE = 15;
    private static final int DEFAULT_QUEUE_SIZE = 100;
    private int coreSize;
    private int maxSize;
    private int queueSize;
    private ExecutorService executorService;


    @PostConstruct
    private void init() {
        coreSize = 0 >= coreSize ? DEFAULT_CORE_SIZE : coreSize;
        maxSize = 0 >= maxSize ? DEFAULT_MAX_SIZE : maxSize;
        queueSize = 0 >= queueSize ? DEFAULT_QUEUE_SIZE : queueSize;
        executorService = new ThreadPoolExecutor(CORE_SIZE * coreSize,
                CORE_SIZE * maxSize, 60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(queueSize));
        LOGGER.info("corePoolSize:[{}] maximumPoolSize:[{}] queueSize:[{}]",
                CORE_SIZE * coreSize, CORE_SIZE * maxSize, queueSize);
    }

    @Bean("executorService")
    public ExecutorService getExecutorService() {
        return executorService;
    }

    public int getCoreSize() {
        return coreSize;
    }

    public void setCoreSize(int coreSize) {
        this.coreSize = coreSize;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public int getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(int queueSize) {
        this.queueSize = queueSize;
    }
}
