package com.cyfan.juc.my.test.thread.threadConcurrent.Synchronized.jolView;


import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

/**
 * 锁降级验证(我自己没有验证出来可能和jdk版本有关)
 * 我验证出来是都降级失败失败了！！！
 *
 * 锁是否可以降级？？？？请证明
 *      锁可以降级的
 *      降级条件：
 *          stw安全点的时候，vmThread 轮询所有的ObjectMonitor对象，将此时没有被使用的重量级锁降级为轻量级锁
 *      锁降级只发生在重量级锁->轻量级锁 这个过程
 *
 *
 *
 * 此处使用的是jol-core 0.10版本，使用0.15版本，分析会直接告诉你当前是什么锁。
 *         <dependency>
 *             <groupId>org.openjdk.jol</groupId>
 *             <artifactId>jol-core</artifactId>
 *             <version>0.10</version>
 *         </dependency>
 *
 */
public class SynchronizedDegradeTest {
    static LockObject lockObj = new LockObject();
    public static void main(String[] args) throws InterruptedException {

        /**
         * # Running 64-bit HotSpot VM.m   // 64位虚拟机
         * # Using compressed oop with 3-bit shift.   // 采用压缩oop
         * # Using compressed klass with 3-bit shift. // 采用压缩klass
         * # Objects are 8 bytes aligned.   // 8个字节的填充
         * # Field sizes by type: 4, 1, 1, 2, 2, 4, 4, 8, 8 [bytes]
         * # Array element sizes: 4, 1, 1, 2, 2, 4, 4, 8, 8 [bytes]
         */
        System.out.println(VM.current().details());// 打印虚拟机的细节

        /**
         * com.cyfan.juc.my.test.thread.threadConcurrent.Synchronized.jolView.LockObject object internals:
         * // object header 这三行都代表对象头信息
         *  OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
         *       0     4        (object header)                           01 00 00 00 (00000001 00000000 00000000 00000000) (1)
         *       4     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
         *       8     4        (object header)                           43 c1 00 f8 (01000011 11000001 00000000 11111000) (-134168253)
         *      12     4        (loss due to the next object alignment)   //这里是用来填充的对齐的
         * Instance size: 16 bytes
         * Space losses: 0 bytes internal + 4 bytes external = 4 bytes total
         */
        //System.out.println(ClassLayout.parseInstance(noLockObj).toPrintable()); //打印lockObject 的内存布局
/**
 * 锁是否可以降级？？？？请证明
 *      锁可以降级的
 *      降级条件：
 *          stw安全点的时候，vmThread 轮询所有的ObjectMonitor对象，将此时没有被使用的重量级锁降级为轻量级锁
 *      锁降级只发生在重量级锁->轻量级锁 这个过程
 */
        System.out.println("=======================================================锁降级 lambda 表达式实现Runnable接口降级失败=====================================================");
        //lambdaLockDegrade();

        /**
         *
         */


        System.out.println("=======================================================锁降级 lambda 表达式实现Runnable接口降级失败=====================================================");


        System.out.println("=======================================================锁降级 锁降级内部类实现Runnable接口降级成功=====================================================");
        innerClassLockDegrade();

        /**
         *
         */
        System.out.println("=======================================================锁降级 内部类实现Runnable接口降级成功====================================================");



    }

    private static void lambdaLockDegrade() throws InterruptedException {
        System.out.println(">>>>>偏向锁在jvm启动后,启动后，启动后，四秒钟才会开启！！！！");
        Thread.sleep(5000); //休眠五秒
        System.out.println(".>>>>>>>>>>>>>>>重赋值之前...");
        System.out.println(ClassLayout.parseInstance(lockObj).toPrintable()); //打印lockObject 的内存布局
        lockObj = new LockObject();//重赋值，重赋值之后分配的对象才是开启偏向锁的
        System.out.println(".>>>>>>>>>>>>>>>重赋值之后...");
        System.out.println(ClassLayout.parseInstance(lockObj).toPrintable()); //打印lockObject 的内存布局

        //子线程加偏向锁
        Thread thread1 = new Thread(() -> {
            synchronized (lockObj){
                System.out.println(">>>>>>>>>>>>>>"+Thread.currentThread().getName()+",加偏向锁中.......");
                System.out.println(ClassLayout.parseInstance(lockObj).toPrintable()); //打印lockObject 的内存布局
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "thread1");


        //thread2获取重量级锁
        Thread thread2 = new Thread(() -> {
            synchronized (lockObj){
                System.out.println("thread2 与thread1 竞争锁，thread1尚未执行完成，thread2等待thread1执行完成，获取锁最终升级为重量级锁");
                System.out.println(">>>>>>>>>>>>>>"+Thread.currentThread().getName()+",加锁中.......");
                System.out.println(ClassLayout.parseInstance(lockObj).toPrintable()); //打印lockObject 的内存布局
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "thread2");


        thread1.start();
        Thread.sleep(2000);//休眠2秒确保thread1先启动获取到偏向锁
        thread2.start(); // thread2在thread1后启动两秒，thread1共需执行5秒，此时必然发生抢锁

        thread1.join();
        thread2.join();

        Thread.sleep(5000); //休眠5秒,进入stw安全点检查，如果后续获取锁变为轻量级锁，那么说明发生了锁降级

        Thread thread3 = new Thread(() -> {
            synchronized (lockObj){
                System.out.println(">>>>>>>>>>>>>>"+Thread.currentThread().getName()+",加锁中.......");
                System.out.println(ClassLayout.parseInstance(lockObj).toPrintable()); //打印lockObject 的内存布局
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "thread3");
        thread3.start();
        //thread3.join();
    }

    private static void innerClassLockDegrade() throws InterruptedException {
        System.out.println(">>>>>偏向锁在jvm启动后,启动后，启动后，四秒钟才会开启！！！！");
        Thread.sleep(5000); //休眠五秒
        System.out.println(".>>>>>>>>>>>>>>>重赋值之前...");
        System.out.println(ClassLayout.parseInstance(lockObj).toPrintable()); //打印lockObject 的内存布局
        lockObj = new LockObject();//重赋值，重赋值之后分配的对象才是开启偏向锁的
        System.out.println(".>>>>>>>>>>>>>>>重赋值之后...");
        System.out.println(ClassLayout.parseInstance(lockObj).toPrintable()); //打印lockObject 的内存布局

        //子线程加偏向锁
        Thread thread11 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (lockObj){
                    System.out.println(">>>>>>>>>>>>>>"+Thread.currentThread().getName()+",加偏向锁中.......");
                    System.out.println(ClassLayout.parseInstance(lockObj).toPrintable()); //打印lockObject 的内存布局
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }, "thread11");


        //thread2获取重量级锁
        Thread thread21 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (lockObj){
                    System.out.println("thread2 与thread1 竞争锁，thread1尚未执行完成，thread2等待thread1执行完成，获取锁最终升级为重量级锁");
                    System.out.println(">>>>>>>>>>>>>>"+Thread.currentThread().getName()+",加锁中.......");
                    System.out.println(ClassLayout.parseInstance(lockObj).toPrintable()); //打印lockObject 的内存布局
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }, "thread21");


        thread11.start();
        Thread.sleep(2000);//休眠2秒确保thread1先启动获取到偏向锁
        thread21.start(); // thread2在thread1后启动两秒，thread1共需执行5秒，此时必然发生抢锁

        thread11.join();
        thread21.join();

        Thread.sleep(5000); //休眠5秒,进入stw安全点检查，如果后续获取锁变为轻量级锁，那么说明发生了锁降级

        Thread thread31 = new Thread(() -> {
            synchronized (lockObj){
                System.out.println(">>>>>>>>>>>>>>"+Thread.currentThread().getName()+",加锁中.......");
                System.out.println(ClassLayout.parseInstance(lockObj).toPrintable()); //打印lockObject 的内存布局
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "thread31");
        thread31.start();
        thread31.join();
    }

}
