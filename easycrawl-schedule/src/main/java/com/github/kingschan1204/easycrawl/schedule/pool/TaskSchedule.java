package com.github.kingschan1204.easycrawl.schedule.pool;

import com.github.kingschan1204.easycrawl.schedule.queue.RedisQueue;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public interface TaskSchedule {
  String taskName();

  long taskCount();

  long activeCount();

  long completedTaskCount();

  long queueSize();

  long errorCount();

  String nextRunTime();

  /**
   * 检查线程池是否已经完全终止 true：线程池已经终止 false：线程池尚未终止
   *
   * @return
   */
  boolean isTerminated();

  void pause();

  void resume();

  boolean awaitTermination(long time, TimeUnit timeUnit);

  ScheduledFuture schedule(Runnable command, long delay, TimeUnit unit);

  ScheduledFuture scheduleWithFixedDelay(
      Runnable command, long initialDelay, long delay, TimeUnit unit);

  ScheduledFuture scheduleByQueue(
      BlockingQueue<Map<String, Object>> queue,
      Runnable command,
      long initialDelay,
      long delay,
      TimeUnit unit);

  ScheduledFuture scheduleByRedisQueue(
      RedisQueue queue, Runnable command, long initialDelay, long delay, TimeUnit unit);
}
