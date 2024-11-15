package com.github.kingschan1204.easycrawl.thread.pool;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * 参数队列为空 自动关闭线程池
 *
 * @author kingschan
 * 2024-11-14
 */
@Slf4j
public class QueueAutoCloseScheduledPool extends NomalScheduledPool {

    final Queue<Map<String, Object>> queue;

    public QueueAutoCloseScheduledPool(int corePoolSize, ThreadFactory threadFactory, RejectedExecutionHandler handler, Queue<Map<String, Object>> queue) {
        super(corePoolSize, threadFactory, handler);
        this.queue = queue;
    }

    @Override
    public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
        return super.schedule(build(command), delay, unit);
    }

    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
        return super.scheduleAtFixedRate(build(command), initialDelay, period, unit);
    }

    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
        return super.scheduleWithFixedDelay(build(command), initialDelay, delay, unit);
    }

    Runnable build(Runnable command) {
        Runnable conditionalCommand = () -> {
            if (queue.size() > 0) {
                command.run();
            } else {
                shutdownNow();
                log.warn("参数队列为空，关闭线程！");
            }
        };
        return conditionalCommand;
    }
}
