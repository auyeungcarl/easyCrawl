package com.github.kingschan1204.easycrawl.schedule;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author kingschan 2024-11-14
 */
public class TaskThreadFactory implements ThreadFactory {
  final AtomicInteger poolNumber = new AtomicInteger(0);
  private final String prefix;

  public TaskThreadFactory(String prefix) {
    this.prefix = prefix;
  }

  @Override
  public Thread newThread(Runnable r) {
    Thread t = new Thread(r);
    t.setName(String.format("%s-%d", prefix, poolNumber.incrementAndGet()));
    t.setPriority(Thread.NORM_PRIORITY); // 设置线程优先级为正常优先级
    t.setDaemon(false); // 设置为非守护线程，可根据需求修改
    return t;
  }
}
