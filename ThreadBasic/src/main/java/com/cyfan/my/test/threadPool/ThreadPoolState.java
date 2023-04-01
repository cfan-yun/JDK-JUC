package com.cyfan.my.test.threadPool;


import java.util.concurrent.ThreadPoolExecutor;

/**
 * 1.线程池状态：
 * RUNNING -> SHUTDOWN
 *    On invocation of shutdown(), perhaps implicitly in finalize()
 * (RUNNING or SHUTDOWN) -> STOP
 *    On invocation of shutdownNow()
 * SHUTDOWN -> TIDYING
 *    When both queue and pool are empty
 * STOP -> TIDYING
 *    When pool is empty
 * TIDYING -> TERMINATED
 *    When the terminated() hook method has completed
 *
 *    RUNNING
 *      当创建线程后，初始时，线程池就处于RUNNING状态
 *    SHUTDOWN
 *      如果你调用了shutdown方法，线程池就处于SHUTDOWN状态，此时线程池不会接受新的任务，他会等待所有任务执行完成（包括队列里面的任务）
 *    STOP
 *      如果调用了shutdownNow方法，线程池就处于STOP状态，此时线程池不会接受新的任务，并且会中断正在执行的任务，并且会清空队列，并返回剩余的任务
 *    TIDYING
 *      SHUTDOWN或STOP状态下，线程池全部处理完毕。
 *    TERMINATED
 *      在TIDYING状态下调用terminated方法进入TERMINATED状态
 *
 *
 */
public class ThreadPoolState {

    public static void main(String[] args) {

    }



}
