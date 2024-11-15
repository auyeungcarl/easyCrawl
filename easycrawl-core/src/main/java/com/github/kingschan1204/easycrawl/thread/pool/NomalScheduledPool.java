package com.github.kingschan1204.easycrawl.thread.pool;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;

/**
 * @author kingschan
 * 2024-11-14
 */
public class NomalScheduledPool extends ScheduledThreadPoolExecutor {
    public NomalScheduledPool(int corePoolSize) {
        super(corePoolSize);
    }

    public NomalScheduledPool(int corePoolSize, ThreadFactory threadFactory) {
        super(corePoolSize, threadFactory);
    }

    public NomalScheduledPool(int corePoolSize, RejectedExecutionHandler handler) {
        super(corePoolSize, handler);
    }

    public NomalScheduledPool(int corePoolSize, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, threadFactory, handler);
    }
}
