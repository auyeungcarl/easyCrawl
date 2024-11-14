package com.github.kingschan1204.easycrawl.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author kingschan
 * 2024-11-14
 */
public class TaskThreadFactory implements ThreadFactory {
    final AtomicInteger poolNumber = new AtomicInteger(0);

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r);
        t.setName("crawl-task-" + poolNumber.getAndIncrement());
        t.setPriority(Thread.NORM_PRIORITY); // 设置线程优先级为正常优先级
        t.setDaemon(false); // 设置为非守护线程，可根据需求修改
        return t;
    }
}
