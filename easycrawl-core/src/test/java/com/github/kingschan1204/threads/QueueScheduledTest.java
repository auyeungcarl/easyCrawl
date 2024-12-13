package com.github.kingschan1204.threads;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.TtlRunnable;
import com.github.kingschan1204.easycrawl.thread.TaskThreadFactory;
import com.github.kingschan1204.easycrawl.thread.pool.QueueAutoCloseScheduledPool;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class QueueScheduledTest {
    static TransmittableThreadLocal<String> ttl = new TransmittableThreadLocal<>();
    public static void main(String[] args) {
        ttl.set("xxx");
        Queue<Map<String, Object>> queue = new LinkedBlockingQueue<>();
        for (int i = 0; i < 4; i++) {
            queue.add(Map.of(String.valueOf(i), "value" + i));
        }
        QueueAutoCloseScheduledPool pool = new QueueAutoCloseScheduledPool(10, new TaskThreadFactory(), new ThreadPoolExecutor.AbortPolicy(), queue);

        Runnable runnable = () -> {
            Object v = queue.poll();
            System.out.println(String.format("%s -> ttl:%s queue:%s", Thread.currentThread().getName(), ttl.get(), v));
            // 不用TtlRunnable包装，会获取不到ttl
            /*CompletableFuture.runAsync(TtlRunnable.get(() -> {
                System.out.println(String.format("【CompletableFuture】 %s -> ttl:%s queue:%s", Thread.currentThread().getName(), ttl.get(), v));
            }));*/
            ttl.set(Thread.currentThread().getName());
            System.out.println("-----------------------------------");
        };
        for (int i = 1; i < 3; i++) {
            pool.scheduleWithFixedDelay(runnable, i*2, 1, TimeUnit.SECONDS);
        }

    }
}
