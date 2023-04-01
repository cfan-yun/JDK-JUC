package com.cyfan.my.test.threadPool;

import com.cyfan.my.test.threadPool.task.MyFutureTask;
import com.cyfan.my.test.threadPool.task.MyTask;

import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


/**
 * 可扩展点：
 * 经验：protected 修饰并且是空实现，通常就是扩展点
 * terminated     线程池关闭之后执行
 * afterExecute   每一个任务执行完之后来执行这个方法
 * beforeExecute  任务执行完之前来执行这个方法
 * <p>
 * 需求：
 *  1.在多个任务执行完成的时候，可以暂停线程池（比如暂停线程池），暂停后必须可以支持恢复线程池，
 *  2.在每个任务执行后需要获取执行结果，
 *  3.在最后所有任务执行完成之后，必须通知管理员
 */
public class MyExtendPool extends ThreadPoolExecutor {

    private final ReentrantLock lock = new ReentrantLock();//定义锁
    private final Condition condition = lock.newCondition();
    private boolean isPaused;

    public MyExtendPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {//在每个任务执行之前执行
        super.beforeExecute(t, r);
        lock.lock();//加锁
        try {
            while (isPaused) {//检查如果是暂停状态
                condition.await(); //卡住等待唤醒
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }

    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {//任务执行完成之后执行
        super.afterExecute(r, t);
        if (t == null && r instanceof FutureTask<?>){//没有发生异常
            try {
                Object result = ((FutureTask<?>) r).get();
                System.out.println("任务执行完毕，result---------->" + result.toString());
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    protected void terminated() {//线程池关闭前执行
        super.terminated();
        System.out.println("线程池已经关闭");
    }


    /**
     * 暂停线程池
     */
    public void paused(){
        lock.lock();
        try {
            isPaused = true;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 线程池恢复
     */
    public void resume(){
        lock.lock();
        try {
            isPaused = false;
            condition.signalAll();//唤醒线程
        } finally {
            lock.unlock();
        }
    }


    public static void main(String[] args) throws InterruptedException {
        /*
         *    需求：
         *      1.在多个任务执行完成的时候，可以暂停线程池（比如暂
         *      2.在每个任务执行后需要获取执行结果，
         *      3.在最后所有任务执行完成之后，必须通知管理员
         *
         */
        MyExtendPool pool = new MyExtendPool(3, 10, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<>(20));

        for (int i = 0; i < 10; i++) {
            MyTask command = new MyTask(i);
            pool.execute(command);

            MyFutureTask myFutureTask = new MyFutureTask(i);
            pool.submit(myFutureTask);
        }

        Thread.sleep(1000);
        pool.paused();
        System.out.println(">>>>>>>>线程池已经暂停");
        Thread.sleep(5000);
        pool.resume();
        System.out.println(">>>>>>>>>>>>>>>线程池已恢复");

        pool.shutdown();


    }
}
