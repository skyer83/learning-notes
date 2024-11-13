package com.lulala.jfk.thread;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import com.lulala.jfk.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.*;

/**
 * 线程池使用样例
 *
 * @author shenjh
 * @version 1.0
 * @since 2023-10-10 14:13
 */
@Slf4j
public class ThreadPoolExecutorDemo {

    public static void main(String[] args) {
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNamePrefix("demo-pool-").setUncaughtExceptionHandler((thread,  e)->{
            log.error(StringUtils.join(Constants.PRINT_PRE, thread.getName(), "-", "未捕获的异常"), e);
        }).build();
        // 表示空闲线程的存活时间
        long keepAliveTime = 0L;
        // 表示keepAliveTime的单位
        TimeUnit timeUnit = TimeUnit.MILLISECONDS;
        /*
            拒绝策略
            ThreadPoolExecutor.AbortPolicy()            抛出RejectedExecutionException异常
            ThreadPoolExecutor.CallerRunsPolicy()	    由向线程池提交任务的线程来执行该任务
            ThreadPoolExecutor.DiscardPolicy()          抛弃当前的任务
            ThreadPoolExecutor.DiscardOldestPolicy()	抛弃最旧的任务（最先提交而没有得到执行的任务）
         */
        RejectedExecutionHandler rejectedExecutionHandler = new ThreadPoolExecutor.AbortPolicy();

        int corePoolSize = 2;
        int maximumPoolSize = 5;
        BlockingQueue<Runnable> workQueue = new LinkedBlockingDeque<>(8);
        /*
            corePoolSize ：线程池中核心线程数的最大值；
            maximumPoolSize ：线程池中能拥有最多线程数，必须大于等于 corePoolSize 的值；
            workQueue ：用于缓存任务的阻塞队列，线程池支持的最大并发数为：workQueue + maximumPoolSize，若超过则新的任务会被拒绝；
            三者关系说明：先运行 corePoolSize 线程数，当 corePoolSize 满了后，则将新任务入 workQueue 队列，待 workQueue 队列满了，再将新任务交给 maximumPoolSize 运行，
            例如：corePoolSize = 2，maximumPoolSize = 5， workQueue = 8，
                此时如果并发是  1 ，则 corePoolSize 占 1，workQueue 占 0，maximumPoolSize 占 1（corePoolSize 的 1）；
                此时如果并发是  2 ，则 corePoolSize 占 2，workQueue 占 0，maximumPoolSize 占 2（corePoolSize 的 2）；
                此时如果并发是  3 ，则 corePoolSize 占 2，workQueue 占 1，maximumPoolSize 占 2（corePoolSize 的 2）；
                此时如果并发是 10 ，则 corePoolSize 占 2，workQueue 占 8，maximumPoolSize 占 2（corePoolSize 的 2）；
                此时如果并发是 11 ，则 corePoolSize 占 2，workQueue 占 8，maximumPoolSize 占 3（corePoolSize 的 2 + 新创建的 1）；
                此时如果并发是 13 ，则 corePoolSize 占 2，workQueue 占 8，maximumPoolSize 占 5（corePoolSize 的 2 + 新创建的 3）；
                此时如果并发是 14 ，则 corePoolSize 占 2，workQueue 占 8，maximumPoolSize 占 5（corePoolSize 的 2 + 新创建的 3），
                    此时还有一个新的任务无人承接，此时该任务就会被线程池拒绝，并根据设置的拒绝策略进行处理；
         */
        ExecutorService executorService = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, timeUnit, workQueue, threadFactory, rejectedExecutionHandler);
        for (int i = 0; i < 14; i++) {
            try {
                final int j = i;
                executorService.execute(() -> {
                    log.info(StringUtils.join(Constants.PRINT_PRE, Thread.currentThread().getName()));
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        log.error("", e);
                    }
                    if (j == 2) {
                        int a = 1/0;
                        log.debug(a + "");
                    }
                });
            } catch (RejectedExecutionException e) {
                log.error(StringUtils.join(Constants.PRINT_PRE, "第", i, "个任务被拒绝了"), e);
                // 考虑入消息中间件队列，由其他服务器处理？
            }
        }
        executorService.shutdown();
    }
}
