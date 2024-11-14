package com.github.kingschan1204.easycrawl.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

/**
 * @author kingschan
 */
@Slf4j
public class TaskScheduledThreadPool extends ScheduledThreadPoolExecutor {


    /**
     * 错误次数
     */
    private AtomicInteger errorNumber;
    /**
     * 最大错误次数
     */
    private Integer maxErrorNumber;
    /**
     * 记录开始时间
     */
    long start = 0L;
    /**
     * 任务耗时毫秒数
     */
    private Double runMillSeconds = 0.0;

    private final Supplier<Boolean> condition;


    boolean isPause = false;
    ReentrantLock lock = new ReentrantLock();
    Condition conditionLock = lock.newCondition();


    public TaskScheduledThreadPool(int corePoolSize, ThreadFactory threadFactory, RejectedExecutionHandler handler, AtomicInteger errorNumber, Integer maxErrorNumber, Supplier<Boolean> condition) {
        super(corePoolSize, threadFactory, handler);
        this.errorNumber = errorNumber;
        this.maxErrorNumber = maxErrorNumber;
        this.condition = condition;
    }

    public TaskScheduledThreadPool( Supplier<Boolean> condition) {
//        Executors.defaultThreadFactory()
        super(10, new TaskThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
        this.errorNumber = new AtomicInteger(0);
        this.maxErrorNumber = 3;
        this.condition = condition;
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        try {
            start = System.currentTimeMillis();
            lock.lock();
            super.beforeExecute(t, r);
            while (isPause) {
                long ms = 10L;
                log.info("{} 任务已被暂停!", t.getName());
                Thread.sleep(ms);
                conditionLock.await();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }


    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long period, TimeUnit unit) {
        Runnable conditionalCommand = () -> {
            if(null == condition || condition.get()){
                command.run();
            }

        };
        return super.scheduleWithFixedDelay(conditionalCommand, initialDelay, period, unit);
    }

    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
        Runnable conditionalCommand = () -> {
            if(null == condition || condition.get()){
                command.run();
            }
        };
        return super.scheduleAtFixedRate(conditionalCommand, initialDelay, period, unit);
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        //如果最大错误次数为0 则不限制错误次数
        if (maxErrorNumber != 0 && errorNumber.get() > maxErrorNumber) {
            log.error("错误次数过多，即将关闭线程任务，当前错误数:{} 最大允许错误次数：{}", errorNumber.get(), maxErrorNumber);
            shutdown();
        }
        trace();
        runMillSeconds = Double.valueOf(System.currentTimeMillis() - start);
    }

    void trace() {
        log.info("【{}】 - {} 本次耗时{}秒",
                Thread.currentThread().getName(),
                String.format("总线程数:%s,活动线程数:%s,执行完成线程数:%s,排队线程数:%s,错误次数：%s",
                        getTaskCount(),
                        getActiveCount(),
                        getCompletedTaskCount(),
                        getQueue().size(),
                        errorNumber
                )
                , runMillSeconds
        );
    }


    @Override
    protected void terminated() {
        super.terminated();
        log.warn("线程{}已被关闭！ ", Thread.currentThread().getName());
    }

    /**
     * 暂停
     */
    public void pause() {
        lock.lock();
        isPause = true;
        lock.unlock();
    }

    /**
     * 继续执行
     */
    public void resume() {
        lock.lock();
        isPause = false;
        conditionLock.signalAll();
        lock.unlock();
    }

    /**
     * 任务是否被暂停
     *
     * @return true 暂停  false 正常
     */
    public boolean isPause() {
        return isPause;
    }

    /**
     * 得到上次运行时间
     *
     * @return
     */
    public Double getRunMillSeconds() {
        return this.runMillSeconds;
    }
}
