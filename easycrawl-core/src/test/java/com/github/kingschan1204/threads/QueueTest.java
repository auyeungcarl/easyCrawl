package com.github.kingschan1204.threads;
import java.util.Date;
import java.util.concurrent.*;

public class QueueTest {

    // 定义阻塞队列，容量为1
    private static final BlockingQueue<String> queue = new ArrayBlockingQueue<>(1);

    public static void main(String[] args) {
        // 使用ThreadPoolExecutor来创建线程池，可更精细地控制参数
        // 这里设置核心线程数为2，最大线程数也为2，任务队列使用ArrayBlockingQueue，容量为10
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(2, new ThreadPoolExecutor.AbortPolicy());

        // 生产者任务，将当前时间放入队列
        Runnable producer = () -> {
            try {
                queue.put(new Date().toString());
            } catch (InterruptedException e) {
                // 这里可以根据业务需求进行更合适的处理，比如记录日志并重新中断线程
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        };

        // 另一个生产者任务，将固定字符串"1"放入队列
        Runnable producer1 = () -> {
            try {
                queue.put("1");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        };

        // 消费者任务，从队列中取出数据并打印
        Runnable consumer = () -> {
            try {
                while (true) {
                    String time = queue.take();
                    System.out.println("Consumer " + time);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        };

        // 每隔5秒执行一次producer任务
        executorService.scheduleAtFixedRate(producer, 1, 5, TimeUnit.SECONDS);
        // 每隔1秒执行一次producer1任务
        executorService.scheduleAtFixedRate(producer1, 1, 1, TimeUnit.SECONDS);
        // 提交消费者任务到线程池
        executorService.submit(consumer);
    }
}
