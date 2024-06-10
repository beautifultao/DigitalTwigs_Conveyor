package com.cumt.data.config;

import org.springframework.context.annotation.Bean;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPoolConfig {
    /*
     * 核心线程数, 2N + 1 (N为CPU核数)
     */
    private static final int CORE_POOL_SIZE = 17;
    /*
     * 最大线程数
     */
    private static final int MAX_POOL_SIZE = 50;
    /*
     * 队列最大长度
     */
    private static final int QUEUE_CAPACITY = 1000;
    /*
     * 空闲线程存活时间
     */
    private static final long KEEP_ALIVE_SECONDS = 500;

    @Bean("taskExecutor")
    public ExecutorService executorService() {
        AtomicInteger counter = new AtomicInteger(1);
        LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>(QUEUE_CAPACITY);
        return new ThreadPoolExecutor(
                CORE_POOL_SIZE,
                MAX_POOL_SIZE,
                KEEP_ALIVE_SECONDS,
                TimeUnit.SECONDS,
                queue,
                r -> new Thread(r,"ThreadPool-" + counter.getAndIncrement()),
                new ThreadPoolExecutor.DiscardPolicy());
    }
}
