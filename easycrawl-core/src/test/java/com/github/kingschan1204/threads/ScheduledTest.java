package com.github.kingschan1204.threads;

import com.github.kingschan1204.easycrawl.core.agent.WebAgent;
import com.github.kingschan1204.easycrawl.helper.datetime.DateHelper;
import com.github.kingschan1204.easycrawl.helper.json.JsonHelper;
import com.github.kingschan1204.easycrawl.task.EasyCrawl;
import com.github.kingschan1204.easycrawl.thread.TaskScheduledThreadPool;
import com.github.kingschan1204.easycrawl.thread.TaskThreadFactory;
import com.github.kingschan1204.easycrawl.thread.pool.ConditionScheduledPool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;


public class ScheduledTest {

//    String curl = """
//                curl 'https://www.szse.cn/api/report/exchange/onepersistenthour/monthList?month=${month}&v=${timestamp}' \\
//                  -H 'Accept: */*' \\
//                  -H 'Accept-Language: zh-CN,zh;q=0.9' \\
//                  -H 'Cache-Control: no-cache' \\
//                  -H 'Connection: keep-alive' \\
//                  -H 'DNT: 1' \\
//                  -H 'Pragma: no-cache' \\
//                  -H 'Referer: https://www.szse.cn/disclosure/index.html' \\
//                  -H 'Sec-Fetch-Dest: empty' \\
//                  -H 'Sec-Fetch-Mode: cors' \\
//                  -H 'Sec-Fetch-Site: same-origin' \\
//                  -H 'User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/129.0.0.0 Safari/537.36' \\
//                  -H 'X-Requested-With: XMLHttpRequest' \\
//                  -H 'sec-ch-ua: "Google Chrome";v="129", "Not=A?Brand";v="8", "Chromium";v="129"' \\
//                  -H 'sec-ch-ua-mobile: ?0' \\
//                  -H 'sec-ch-ua-platform: "Windows"'
//                """;
//        new EasyCrawl<JsonHelper>()
//                .webAgent(curl)
//                .args("month","2024-10")
//                .analyze(WebAgent::getJson)
//                .schedule(2, TimeUnit.SECONDS, r -> {
//        System.out.println(r);
//    });
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
        ConditionScheduledPool pool = new ConditionScheduledPool(2,new TaskThreadFactory(),  new ThreadPoolExecutor.AbortPolicy(),condition);

        pool.scheduleWithFixedDelay(runnable, 0, 1, TimeUnit.SECONDS);
        Runtime.getRuntime().addShutdownHook(new Thread(pool::shutdown));
    }
}
