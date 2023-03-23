package com.cyfan.juc.my.test.thread.threadCommunication.threadLoal;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * threadLocal内存泄露模拟
 *      jvm：
 *          -Xms50M
 *          -Xmx50M
 *      产生原因：threadLocal和线程池一起使用时，threadLocal的entry中的value是强引用，由于线程没有结束，导致value一直回收不掉，但是弱引用Key已经被回收了。
 *          value 在gc时回收不掉，导致内存泄露OOM
 * 如何防止内存泄露？？
 *  线程池中threadLocal使用完之后，调用remove方法。
 */
public class ThreadLocalForOOM {

    private static class OOM {
        private Long[] longs = new Long[2*1024 * 1024];// 1M空间占用
    }

    final static ThreadLocal<OOM> threadLocal = new ThreadLocal<>();

    final static ThreadPoolExecutor pool = new ThreadPoolExecutor(5,5,1, TimeUnit.MINUTES,new LinkedBlockingDeque<>());

        public static void main(String[] args) {
        for (int i = 0; i < 500; i++) {
            int finalI = i;
            pool.execute(()->{
                threadLocal.set(new OOM());
                System.out.println("OOM Object + "+ finalI);
                OOM oom = threadLocal.get();
                System.out.println("oom -> " + oom);
                threadLocal.remove();//用完之后进行remove操作
            });
            try {
                Thread.sleep(40);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
