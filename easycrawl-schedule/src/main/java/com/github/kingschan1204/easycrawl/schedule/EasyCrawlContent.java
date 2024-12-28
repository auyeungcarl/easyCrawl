package com.github.kingschan1204.easycrawl.schedule;

import com.github.kingschan1204.easycrawl.schedule.pool.TaskSchedule;
import com.github.kingschan1204.easycrawl.schedule.pool.impl.EasyCrawlScheduledPool;
import com.github.kingschan1204.easycrawl.schedule.queue.RedisQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class EasyCrawlContent {
  private static ConcurrentHashMap<String, TaskSchedule> threadContent;

  static {
    threadContent = new ConcurrentHashMap<>();
  }

  private EasyCrawlContent() {}

  private static class Holder {
    private static EasyCrawlContent instance = new EasyCrawlContent();
  }

  public static EasyCrawlContent getInstance() {
    return Holder.instance;
  }

  public void scheduleByRedisQueue(
      String taskName,
      RedisQueue queue,
      Runnable command,
      int runQuantity,
      long initialDelay,
      long delay,
      TimeUnit unit) {
    if (threadContent.containsKey(taskName)) {
      throw new RuntimeException("任务已存在，请忽重复创建！");
    }
    EasyCrawlScheduledPool pool = new EasyCrawlScheduledPool(taskName, runQuantity);
    for (int i = 0; i < runQuantity; i++) {
      pool.scheduleByRedisQueue(queue, command, initialDelay, delay, unit);
    }
    threadContent.put(taskName, pool);
  }

  public void remove(String taskName) {
    if (!threadContent.containsKey(taskName)) {
      throw new RuntimeException("任务不存在！");
    }
    if (!threadContent.get(taskName).isTerminated()) {
      throw new RuntimeException("任务未执行完毕，无法删除！");
    }
    threadContent.remove(taskName);
  }

  public void pauseAll() {
    threadContent.forEach(
        (k, v) -> {
          v.pause();
        });
  }

  public void resumeAll() {
    threadContent.forEach(
        (k, v) -> {
          v.resume();
        });
  }
}
