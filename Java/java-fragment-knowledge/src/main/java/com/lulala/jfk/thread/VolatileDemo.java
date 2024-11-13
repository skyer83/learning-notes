package com.lulala.jfk.thread;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.*;

/**
 * 参考：https://zhuanlan.zhihu.com/p/180108978 <br/>
 * volatile的作用：<br/>
 * 1、保证变量对所有线程可见性<br/>
 * 2、禁止指令重排<br/>
 * 3、不保证原子性<br/>
 *
 * @author shenjh
 * @version 1.0
 * @since 2023-10-09 16:28
 */
@SuppressWarnings("AlibabaRemoveCommentedCode")
@Slf4j
public class VolatileDemo {

    public static void main(String[] args) throws Exception {
//        NoAtomicity.doTest();
        ThreadVisibility.doTest();
    }

    public static void doSleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            log.error("", e);
        }
    }
}

/** 
 * 参考：https://zhuanlan.zhihu.com/p/180108978 <br/>
 * 保证变量对所有线程可见性 <br/>
 * 
 * @author shenjh
 * @version 1.0
 * @since 2023/10/12 8:55
 */
@Slf4j
class ThreadVisibility {
    public static void doTest() {
        Task task = new Task();
        Thread thread01 = new Thread(task, "线程01");
        Thread thread02 = new Thread(() -> {
            VolatileDemo.doSleep(1000);
            log.info("开始通知其他线程停止任务");
            task.stop = true;
        }, "线程02");
        thread01.start();
        thread02.start();
    }
}
@Slf4j
class Task implements Runnable {
    // 演示线程可见（跳出死循环）
    volatile boolean stop = false;
    // 演示线程不可见（死循环）
//    boolean stop = false;
    int count = 0;

    @Override
    public void run() {
        long start = System.currentTimeMillis();
        while (!stop) {
            count++;
        }
        log.info(StringUtils.join("线程退出:::", count, ":::", (System.currentTimeMillis() - start)));
    }
}

/**
 * 不保证原子性
 *
 * @author shenjh
 * @version 1.0
 * @since 2023/10/11 19:42
 */
@Slf4j
class NoAtomicity {

    private volatile boolean checked = false;
    private int times = 0;

    public void doCheck() {
        // 模拟确保并发线程获取到的初始 checked 为 false
        VolatileDemo.doSleep(100);
        if (!checked) {
            checked = true;
            synchronized (this) {
                times++;
                if (times > 1) {
                    // 会多次打印，表示多次进入，则说明 checked = true 的修改对同样进入本段代码的线程未生效，即无法保证原子性
                    log.info(StringUtils.join(Thread.currentThread().getName(), " 进入 ", times, " 次超过 1 次，无法保证原子性"));
                }
            }
            return;
        }
//        synchronized (this) {
//            // 通过同步锁确保原子性
//            if (!checked) {
//                checked = true;
//                log.info(StringUtils.join(Thread.currentThread().getName(), " 仅会进入 1 次"));
//                return;
//            }
//        }
    }

    public static void doTest() throws Exception {
        while (true) {
            int maxConcurrency = 1000;
            ThreadFactory threadFactory = new ThreadFactoryBuilder().setNamePrefix("demo-pool-").build();
            ExecutorService executorService = new ThreadPoolExecutor(maxConcurrency, 10000, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>(Integer.MAX_VALUE), threadFactory, new ThreadPoolExecutor.AbortPolicy());
            NoAtomicity volatileDemo01 = new NoAtomicity();
            for (int i = 0; i < maxConcurrency; i++) {
                try {
                    executorService.execute(() -> volatileDemo01.doCheck());
                } catch (RejectedExecutionException e) {
                    log.error("", e);
                }
            }
            executorService.shutdown();
            // 等待所有线程都执行完毕
            if (executorService.awaitTermination(5000, TimeUnit.MILLISECONDS)) {
                log.info("批次分割线==========================================================");
                // 等待，避免刷新太快
                VolatileDemo.doSleep(3000);
            }
        }
    }
}
