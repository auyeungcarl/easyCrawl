package com.github.kingschan1204.easycrawl.schedule.pool;

import com.github.kingschan1204.easycrawl.schedule.TaskThreadFactory;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author kingschan 2024-12-28
 */
public class PausableScheduledThreadPool extends ScheduledThreadPoolExecutor {
  private boolean isPaused;
  private final Lock pauseLock = new ReentrantLock();
  private final Condition unpaused = pauseLock.newCondition();

  public PausableScheduledThreadPool(String taskName, int corePoolSize) {
    super(corePoolSize);
    int core = Runtime.getRuntime().availableProcessors();
    // 设置最大线程数
    setMaximumPoolSize(core * 2);
    setCorePoolSize(corePoolSize);
    // 设置空闲线程的存活时间(当线程池中的线程数超过 corePoolSize 时)
    setKeepAliveTime(1, TimeUnit.SECONDS);
    // 取消任务时从队列中移除任务 默认为false
    setRemoveOnCancelPolicy(true);
    setThreadFactory(new TaskThreadFactory(taskName));
  }

  @Override
  protected void beforeExecute(Thread t, Runnable r) {
    super.beforeExecute(t, r);
    pauseLock.lock();
    try {
      while (isPaused) {
        unpaused.await();
      }
    } catch (InterruptedException ie) {
      t.interrupt();
    } finally {
      pauseLock.unlock();
    }
  }

  public void pause() {
    System.out.println("---------暂停任务---------");
    pauseLock.lock();
    try {
      isPaused = true;
    } finally {
      pauseLock.unlock();
    }
  }

  public void resume() {
    System.out.println("---------恢复任务---------");
    pauseLock.lock();
    try {
      isPaused = false;
      unpaused.signalAll();
    } finally {
      pauseLock.unlock();
    }
  }

  public static void main(String[] args) throws InterruptedException {
    PausableScheduledThreadPool executor = new PausableScheduledThreadPool("", 2);

    Runnable task = () -> System.out.println("Executing task at " + System.currentTimeMillis());

    executor.scheduleWithFixedDelay(task, 0, 1, TimeUnit.SECONDS);
    int i = 0;
    do {
      TimeUnit.SECONDS.sleep(5);
      executor.pause();

      TimeUnit.SECONDS.sleep(5);
      executor.resume();
      i++;
      System.out.println("loop :" + i);
    } while (i <= 2);
    executor.shutdown();
    // 等待所有任务完成或超时
    if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
      // 如果超时未完成，强制关闭
      System.out.println("任务执行超时，强制关闭");
      executor.shutdownNow();
    } else {
      System.out.println("任务执行完成");
    }
  }
}
