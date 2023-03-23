package com.cyfan.juc.my.test.threadPool.myThreadPool;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MySimpleThreadPool {

    private static final Object TASK_QUEUE_LOCK = new Object();
    private static final LinkedList<Runnable> TASK_QUEUE = new LinkedList<>();

    private static final List<Thread> CORE_THREAD_QUEUE = new ArrayList<>();

    private int maxPoolSize;
    private int corePoolSize;
    private RejectPolicy rejectPolicy;
    private final static int DEFAULT_CORE_POOL_SIZE = 5;
    private final static int DEFAULT_MAX_POOL_SIZE = 10;
    private final static RejectPolicy DEFAULT_REJECT_POLICY = () -> {
        throw new RuntimeException(Thread.currentThread().getName() + "被拒绝执行！！");
    };


    public MySimpleThreadPool(int corePoolSize, int maxPoolSize, RejectPolicy rejectPolicy) {
        this.maxPoolSize = maxPoolSize;
        this.rejectPolicy = rejectPolicy;
        this.corePoolSize = corePoolSize;
        init(this.corePoolSize);//初始化线程池，核心线程数
    }

    public MySimpleThreadPool(int corePoolSize) {
        this(corePoolSize, DEFAULT_MAX_POOL_SIZE, DEFAULT_REJECT_POLICY);
    }

    public MySimpleThreadPool() {
        this(DEFAULT_CORE_POOL_SIZE, DEFAULT_MAX_POOL_SIZE, DEFAULT_REJECT_POLICY);
    }

    /**
     * 初始化线程池
     *
     * @param corePoolSize 线程池核心线程数大小
     */
    private void init(int corePoolSize) {
        for (int i = 0; i < corePoolSize; i++) {
            createThread();
        }
    }

    private static void createThread() {
        WorkThread workThread = new WorkThread();
        CORE_THREAD_QUEUE.add(workThread);
        workThread.start();

    }


    public static void main(String[] args) {

        //1.创建线程池对象，核心线程数为10
        MySimpleThreadPool mySimpleThreadPool = new MySimpleThreadPool(5);
        //2.如何存取任务？？？ 线程池提交任务，使用队列存取即可
        for (int i = 0; i < 20; i++) {
            final int finalI = i;
            mySimpleThreadPool.submit(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                System.out.println("当前线程为：" + Thread.currentThread().getName() + ", 处理任务为：" + finalI);
            });
        }


    }

    private void submit(Runnable runnable) {

        //提交入队
        synchronized (TASK_QUEUE_LOCK) {
            if (TASK_QUEUE.size() > this.maxPoolSize) {
                System.out.println("当前队列中的任务数量" + TASK_QUEUE.size());
                this.rejectPolicy.rejectHandler();
            }
            TASK_QUEUE.addLast(runnable);
            //添加队列之后，唤醒工作线程消费
            TASK_QUEUE_LOCK.notifyAll();
        }
    }

    enum ThreadPoolStatus {
        FREE,   //空闲
        RUNNING,//运行
        BLOCKED,//阻塞
        DEAD    //关闭
    }


    /**
     * 工作线程
     */
    public static class WorkThread extends Thread {
        private volatile ThreadPoolStatus poolStatus = ThreadPoolStatus.FREE;//线程池状态

        @Override
        public void run() {
            Runnable task;
            while (poolStatus != ThreadPoolStatus.DEAD) {
                //多线程从队列中获取对象需要加锁
                synchronized (TASK_QUEUE_LOCK) {
                    if (TASK_QUEUE.isEmpty()) {//队列为空，线程夯住
                        try {
                            TASK_QUEUE_LOCK.wait(); //队列为空，线程夯住
                            poolStatus = ThreadPoolStatus.BLOCKED;
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    task = TASK_QUEUE.remove();
                }
                if (task != null) {//任务多线程执行，但是从队列中获取任务需要加锁
                    task.run();
                    this.poolStatus = ThreadPoolStatus.FREE;//任务执行完成修改状态为空闲装填
                }
            }
        }
    }


    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public RejectPolicy getRejectPolicy() {
        return rejectPolicy;
    }

    public void setRejectPolicy(RejectPolicy rejectPolicy) {
        this.rejectPolicy = rejectPolicy;
    }

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }
}
