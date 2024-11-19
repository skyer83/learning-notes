package com.lulala.jfk.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程并发执行样例，参见：<br/>
 * 1、CountdownLatch和CyclicBarrier的区别使用场景与具体实现：https://zhuanlan.zhihu.com/p/139020914 <br/>
 * 2、Java实现多个线程一起并发执行：https://blog.csdn.net/qq_34896199/article/details/109184142 <br/>
 * @author shenjh
 * @version 1.0
 * @since 2024/11/19 21:26
 */
@Slf4j
public class ThreadConcurrentExecute {

    public static void main(String[] args) throws Exception {
//        testCountDownLatch01();
//        testCountDownLatch02();
        testCyclicBarrier();
    }

    /**
     * 测试线程并发执行
     * @author shenjh
     * @since 2024/11/19 21:38
     */
    public static void testCountDownLatch01() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        int concurrentNum = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(concurrentNum);
        for (int i = 0; i < concurrentNum; i++) {
            Thread.sleep(500);
            executorService.execute(() -> {
                try {
                    // 阻塞线程
                    countDownLatch.await();
                    log.info("线程" + Thread.currentThread().getName() + "开始执行");
                } catch (InterruptedException e) {
                    log.error("", e);
                }
            });
        }
        // 执行 countDown 计数器 -1 ，count 值变为 0，停止阻塞，所有被阻塞的线程并发执行
        countDownLatch.countDown();

        // 关闭线程池
        executorService.shutdown();
    }

    /**
     * 测试等待所有前置任务都执行完成后，才执行下一步的任务。<br/>
     * 模拟场景：某公司一共有十个人,门卫要等十个人都来上班以后,才可以休息<br/>
     * @author shenjh
     * @since 2024/11/19 22:15
     */
    public static void testCountDownLatch02() throws InterruptedException {
        int concurrentNum = 10;
        CountDownLatch countDownLatch = new CountDownLatch(concurrentNum);
        ExecutorService executorService = Executors.newFixedThreadPool(concurrentNum);
        for (int i = 0; i < concurrentNum; i++) {
            executorService.execute(() -> {
                try {
                    String threadName = Thread.currentThread().getName();
                    log.info("员工 " + threadName + " 正在赶路");
                    Thread.sleep(1000);
                    log.info("员工 " + threadName + " 到公司了");
                    // 调用 countDownLatch 的 countDown 方法使计数器-1
                    countDownLatch.countDown();
                    Thread.sleep(1);
                    log.info("员工 " + threadName + " 开始工作");
                } catch (InterruptedException e) {
                    log.error("", e);
                }
            });
        }
        log.info("门卫等待员工上班中...");
        // 主线程阻塞，等待计数器归零后才继续往下执行
        // 等待所有员工到公司了才去休息
        countDownLatch.await();
        log.info("员工都来了,门卫去休息了");

        // 关闭线程池
        executorService.shutdown();
    }

    /**
     * 测试线程并发执行，模拟跑步场景：十名运动员各自准备比赛，需要等待所有运动员都准备好以后，裁判才能说开始然后所有运动员一起跑
     * @author shenjh
     * @since 2024/11/19 22:29
     */
    public static void testCyclicBarrier() throws InterruptedException {
        int concurrentNum = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(concurrentNum);
        AtomicInteger atomicInteger = new AtomicInteger(1);
        CyclicBarrier cyclicBarrier = new CyclicBarrier(concurrentNum, () -> {
            log.info("第 " + atomicInteger + " 批所有运动员都准备好了，裁判开枪");
            atomicInteger.getAndIncrement();
        });
        for (int n = 0; n < 2; n++) {
            final int batchNum = n + 1;
            for (int i = 0; i < concurrentNum; i++) {
                executorService.execute(() -> {
                    try {
                        String threadName = Thread.currentThread().getName();
                        log.info("第 " + batchNum + " 批运动员 " + threadName + " 正在准备");
                        Thread.sleep(1000);
                        log.info("第 " + batchNum + " 批运动员 " + threadName + " 准备好了");
                        // 等到有 10 个线程都执行了 await() 之后，才继续执行，且 cyclicBarrier 计数器会自动重置为 10 ，可以接着用
                        cyclicBarrier.await();
                        Thread.sleep(100);
                        log.info("第 " + batchNum + " 批运动员 " + threadName + " 开始跑了");
                    } catch (InterruptedException | BrokenBarrierException e) {
                        log.error("", e);
                    }
                });
            }
        }

        // 关闭线程池
        executorService.shutdown();
    }
}
