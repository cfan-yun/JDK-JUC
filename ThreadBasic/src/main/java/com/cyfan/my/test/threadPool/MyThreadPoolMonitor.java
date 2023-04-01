package com.cyfan.my.test.threadPool;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.*;

/**
 * 线程池监控案例
 *
 * 线程池的最优解决方案？？
 *  线程池参数的动态化配置
 *  重点是动态配置：核心线程数、最大线程数、队列容量
 *      1)动态配置理论基础
 *          核心线程数、最大线程数、队列容量都可修改，并且即时生效
 *      2)动态修改参数过程中有哪些需要注意的地方
 *          经验：核心线程调整的时候，连同最大线程一起调整，尤其是新的核心线程数> 旧的核心线程数时，一定要注意，
 *          两个参数一起调整，否则只调整核心线程数无效。
 *      3）队列容量如何调整？？？
 *          复制LinkedBlockingQueue源码修改capacity为非final修饰
 *      4）面试题：
 *          线程池被创建之后，没有线程？？如果没有的话，如何进行预热？ prestartCoreThread 和 prestartAllCoreThreads
 *          核心线程数会被回收吗?   allowCoreThreadTimeOut 设置为true则超时回收
 */
public class MyThreadPoolMonitor {

    public static void main(String[] args) {
        try {
            dynamicModifyPool();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }


    private static ThreadPoolExecutor createPoolExecutor() {
        return  new ThreadPoolExecutor(2, 5, 60, TimeUnit.SECONDS, new MyLinkedBlockingQueue<>(10));
    }


    /**
     * 创建了一个线程池，核心线程数2，最大线程5， 队列大小10
     * 定义15个任务，每个任务耗时10s
     * 这样设置正好5个最大线程打满。队列10，打满
     * 注意：15个任务提交后，5个线程在处理，还有10个在队列中等待，最终需要30s全部执行完毕
     *
     * 调整之前：15个任务30秒
     * 调整之后：15个任务20秒
     * @throws InterruptedException
     */
    public static void dynamicModifyPool() throws InterruptedException {
        ThreadPoolExecutor poolExecutor = createPoolExecutor();//创建线程池
        for (int i = 0; i < 15; i++) {

            poolExecutor.submit(()->{
                threadPoolMonitor(poolExecutor, "创建任务");
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        threadPoolMonitor(poolExecutor, "调整之前");
        poolExecutor.setCorePoolSize(10);
        poolExecutor.setMaximumPoolSize(10);
        MyLinkedBlockingQueue<Runnable> queue = (MyLinkedBlockingQueue<Runnable>)poolExecutor.getQueue();
        queue.setCapacity(100);
        threadPoolMonitor(poolExecutor, "调整之后");
        Thread.currentThread().join();
    }

    private static void threadPoolMonitor(ThreadPoolExecutor poolExecutor, String describe) {
        BlockingQueue<Runnable> queue = poolExecutor.getQueue();
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss");
        System.out.println(simpleDateFormat.format(date) + "     "+Thread.currentThread().getName() + "-"+describe+": "+
                "核心线程数："+poolExecutor.getCorePoolSize() + "," +
                "最大线程数："+poolExecutor.getMaximumPoolSize() + ","+
                "活跃线程数："+poolExecutor.getActiveCount() + ","+
                "完成任务数："+poolExecutor.getCompletedTaskCount() + ","+
                "线程池活跃度："+division(poolExecutor.getActiveCount(), poolExecutor.getMaximumPoolSize())+ ","+
                "队列大小："+(queue.size() + queue.remainingCapacity()) +","+
                "当前队列中的任务数："+queue.size() +","+
                "队列剩余大小："+queue.remainingCapacity()+ ","+
                "队列使用情况："+division(queue.size(), (queue.size() + queue.remainingCapacity())) );

    }

    private static String division(int num1, int num2) {
        return String.format("%1.2f%%", ((double) num1 / (double) num2)*100);
    }
}
