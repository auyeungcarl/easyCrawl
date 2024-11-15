package com.github.kingschan1204.easycrawl.thread.pool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * 参数队列为空 自动关闭线程池
 * @author kingschan
 * 2024-11-14
 */
@Slf4j
public class ConditionScheduledPool extends NomalScheduledPool{
    private Supplier<Boolean> condition;
    public ConditionScheduledPool(int corePoolSize, ThreadFactory threadFactory, RejectedExecutionHandler handler, Supplier<Boolean> condition) {
        super(corePoolSize, threadFactory, handler);
        this.condition = condition;
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
            if (null == condition || condition.get()) {
                command.run();
            }
        };
        return conditionalCommand;
    }
}
