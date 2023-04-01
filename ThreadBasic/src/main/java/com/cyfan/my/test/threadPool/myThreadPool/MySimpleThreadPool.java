package com.cyfan.my.test.threadPool.myThreadPool;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 自定义线程池
 * 1.定义一个队列，用来存放提交的任务
 * 2.线程也需要定义一个队列存放
 * 3.内部就是线程操作任务
 * 4.外部线程池提交任务
 * 5.拒绝策略
 * 6.关闭线程池（shutDown 1.不接受新任务 2.必须把任务完成之后，任务出队列，线程出队列 ）
 * 7.线程池的扩容策略和缩容策略（未实现）
 *  常识：更少的线程可以处理处理更多的任务，所以设置线程的三个参数,min,max,core(active)
 *  1)min < 任务数 < core 或者 任务数 < min ：线程数取min就可-----默认
 *  2)core < 任务数 < max && 当前线程数 < core ：线程数扩展到core就可
 *  3)max < 任务数 && 当前线程数 < max  : 线程数扩展到Max就可
 *
 *8.缩容策略
 *
 *
 */
public class MySimpleThreadPool extends Thread{

    private static final Object TASK_QUEUE_LOCK = new Object(); //TASK_QUEUE锁
    private static final LinkedList<Runnable> TASK_QUEUE = new LinkedList<>();//任务队列

    private static final Object CORE_THREAD_QUEUE_LOCK = new Object();//CORE_THREAD_QUEUE锁
    private static final List<WorkThread> CORE_THREAD_QUEUE = new ArrayList<>();//核心线程

    private int maxPoolSize;//最大线程数
    private int corePoolSize;//核心线程数
    private int minPoolSize; //最小线程数

    private int currentThreadSize;//当前线程数
    private RejectPolicy rejectPolicy;//拒绝策略
    private final static int DEFAULT_CORE_POOL_SIZE = 5; //默认核心线程数
    private final static int DEFAULT_MAX_POOL_SIZE = 20;//默认最大核心线程数
    //默认拒绝策略
    private final static RejectPolicy DEFAULT_REJECT_POLICY = () -> {
        throw new RuntimeException("任务被拒绝执行！！");
    };


    private boolean isClose = false;//线程池是否关闭


    /**
     * 有参构造函数
     * @param corePoolSize
     * @param maxPoolSize
     * @param rejectPolicy
     */
    public MySimpleThreadPool(int minPoolSize, int corePoolSize, int maxPoolSize, RejectPolicy rejectPolicy) {
        this.maxPoolSize = maxPoolSize;
        this.rejectPolicy = rejectPolicy;
        this.corePoolSize = corePoolSize;
        this.minPoolSize = minPoolSize;
        init(this.corePoolSize);//初始化线程池，核心线程数
    }

    /**
     * 有参构造函数
     * @param corePoolSize
     */
    public MySimpleThreadPool(int corePoolSize) {
        this(corePoolSize,corePoolSize, DEFAULT_MAX_POOL_SIZE, DEFAULT_REJECT_POLICY);
    }

    /**
     * 无参构造函数
     */
    public MySimpleThreadPool() {
        this(DEFAULT_CORE_POOL_SIZE,DEFAULT_CORE_POOL_SIZE, DEFAULT_MAX_POOL_SIZE, DEFAULT_REJECT_POLICY);
    }

    @Override
    public void run() {

        if (!isClose){//如果线程池未关闭
            if (this.corePoolSize < TASK_QUEUE.size() && this.currentThreadSize < this.corePoolSize){
                //初次扩容
            }else if(this.maxPoolSize < this.corePoolSize && this.maxPoolSize  < this.currentThreadSize){

            }
        }
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

    /**
     * 创建线程并入队
     */
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

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        mySimpleThreadPool.shutDown();

        mySimpleThreadPool.submit(()->{
            System.out.println("=========再次提交");
        });
    }

    /**
     * 关闭线程池
     */
    private void shutDown() {
        System.out.println("shutDown执行");
        //如果任务队列不为空,休眠一秒后继续检查
        while (!TASK_QUEUE.isEmpty()) {
            try {
                Thread.sleep(1000);
                System.out.println("队列不为空！！");
            } catch (InterruptedException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        System.out.println("队列为空！！");
        synchronized (CORE_THREAD_QUEUE_LOCK){
            //处理线程队列
            int size = CORE_THREAD_QUEUE.size();
            while (size > 0){
                System.out.println("CORE_THREAD_QUEUE.size > 0");
                for (WorkThread workThread:CORE_THREAD_QUEUE) {
                    System.out.println(workThread.poolStatus);
                    //判断阻塞状态
                    if (workThread.poolStatus == ThreadPoolStatus.BLOCKED) {
                        workThread.interrupt(); //中断线程
                        workThread.close(); //修改线程池状态
                        size--;//关闭一个线程之后，需要队列大小--
                    }
                    //线程池为空闲状态，不用判断，因为迟早会变成BLOCKED状态
                    else if (workThread.poolStatus == ThreadPoolStatus.FREE) {
                        //workThread.close();//
                    }else{//RUNNING状态等他跑,睡一会儿再检查
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
            this.isClose = true; //线程池已经关闭
            System.out.println("线程池已经关闭！！！");
        }
    }


    /**
     * 线程池提交任务
     * @param runnable
     */
    private void submit(Runnable runnable) {

        //循环判断是否
        while (isClose){
            throw  new RuntimeException("线程池已经关闭，不允许提交任务！！");
        }

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

    /**
     * 线程池状态
     */
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

            outer:
            while (poolStatus != ThreadPoolStatus.DEAD) {
                Runnable task;
                //多线程从队列中获取对象需要加锁
                synchronized (TASK_QUEUE_LOCK) {
                    while (TASK_QUEUE.isEmpty()) {//队列为空，线程夯住
                        try {
                            poolStatus = ThreadPoolStatus.BLOCKED;  //线程夯住之前，修改状态线程池状态为BLOCKED状态，为了ShutDown时能识别到状态
                            TASK_QUEUE_LOCK.wait(); //队列为空，线程夯住
                        } catch (InterruptedException e) {
                            //throw new RuntimeException(e);
                            System.out.println("线程被中断，线程池即将关闭");
                            break outer;
                        }
                    }
                    task = TASK_QUEUE.remove();
                }
                if (task != null) {//任务多线程执行，但是从队列中获取任务需要加锁
                    this.poolStatus = ThreadPoolStatus.RUNNING; //修改线程池状态为RUNNING
                    task.run();
                    this.poolStatus = ThreadPoolStatus.FREE;//任务执行完成修改状态为空闲装填
                }
            }
        }

        public void close() {
            this.poolStatus = ThreadPoolStatus.DEAD;
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

    public boolean isClose() {
        return isClose;
    }

    public void setClose(boolean close) {
        isClose = close;
    }
}
