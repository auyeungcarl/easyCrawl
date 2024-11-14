package com.github.kingschan1204.threads;

import com.github.kingschan1204.easycrawl.helper.datetime.DateHelper;
import com.github.kingschan1204.easycrawl.thread.TaskScheduledThreadPool;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;


public class ScheduledTest {

    public static void main(String[] args) {
        // 使用Lambda表达式创建ThreadFactory
        /*ThreadFactory threadFactory = (Runnable r) -> {
            Thread t = new Thread(r);
            t.setName("ec-task"); // 设置线程名称
            t.setPriority(Thread.NORM_PRIORITY); // 设置线程优先级为正常优先级
            t.setDaemon(false); // 设置为非守护线程，可根据需求修改
            return t;
        };*/

        Runnable runnable = () -> {
            System.out.println(DateHelper.now().dateTime());
        };

        AtomicInteger errorCount = new AtomicInteger(0);

        Supplier<Boolean> condition = () -> {
            double val = Math.random();
//            System.out.println("生成随机数："+val);
            return val > 0.5;
        };
//        RunCondition<Boolean> condition = () -> {
//            double val = Math.random();
////            System.out.println("生成随机数："+val);
//            return val > 0.5;
//        };
        TaskScheduledThreadPool pool = new TaskScheduledThreadPool(condition);

        pool.scheduleWithFixedDelay(runnable, 0, 1, TimeUnit.SECONDS);
        Runtime.getRuntime().addShutdownHook(new Thread(pool::shutdown));
    }
}
