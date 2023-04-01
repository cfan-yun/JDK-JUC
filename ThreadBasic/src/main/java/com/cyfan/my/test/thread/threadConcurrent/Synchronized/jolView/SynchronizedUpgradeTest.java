package com.cyfan.my.test.thread.threadConcurrent.Synchronized.jolView;


import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

/**
 * 验证锁升级之后，对象头中的比特位信息
 * 此处使用的是jol-core 0.10版本，使用0.15版本，分析会直接告诉你当前是什么锁。
 *         <dependency>
 *             <groupId>org.openjdk.jol</groupId>
 *             <artifactId>jol-core</artifactId>
 *             <version>0.10</version>
 *         </dependency>
 *
 */
public class SynchronizedUpgradeTest {
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
         * jolView.Synchronized.threadConcurrent.thread.test.my.LockObject object internals:
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
 * 偏向锁---->轻量级锁
 */
        System.out.println("=======================================================偏向锁---->轻量级锁=====================================================");
        System.out.println(">>>>>偏向锁在jvm启动后,启动后，启动后，四秒钟才会开启！！！！");
        Thread.sleep(5000); //休眠五秒
        System.out.println(".>>>>>>>>>>>>>>>重赋值之前...");
        System.out.println(ClassLayout.parseInstance(lockObj).toPrintable()); //打印lockObject 的内存布局
        lockObj = new LockObject();//重赋值，重赋值之后分配的对象才是开启偏向锁的
        System.out.println(".>>>>>>>>>>>>>>>重赋值之后...");
        System.out.println(ClassLayout.parseInstance(lockObj).toPrintable()); //打印lockObject 的内存布局

        //子线程加偏向锁
        Runnable runnable =  () -> {
            synchronized (lockObj){
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println(">>>>>>>>>>>>>>>>>>>>"+Thread.currentThread().getName()+",执行完成......");
        };
        Thread thread = new Thread(runnable);
        thread.start();
        Thread.sleep(2000);//休眠2秒确保等待thread线程执行完成，此时main再获取锁应当是轻量级锁
        System.out.println(">>>>>>>>>>>>>>"+thread.getName()+",加锁中.......");
        System.out.println(ClassLayout.parseInstance(lockObj).toPrintable()); //打印lockObject 的内存布局

        //主线程加锁
        synchronized (lockObj){
            System.out.println(">>>>>>>>>>>>>>main线程，加锁中...");
            System.out.println(ClassLayout.parseInstance(lockObj).toPrintable()); //打印lockObject 的内存布局
        }
        System.out.println(">>>>>>>>>>>>>>main线程，加锁后...");
        System.out.println(ClassLayout.parseInstance(lockObj).toPrintable()); //打印lockObject 的内存布局

        /**
         *
         * 01 00 00 00 (*00000001* 00000000 00000000 00000000)
         * 00 00 00 00 (00000000 00000000 00000000 00000000)
         * 43 c1 00 f8 (01000011 11000001 00000000 11111000)
         * 只看*中间的8位，最后三位表示锁状态
         *  001 无锁
         *  101 偏向锁
         *  00  轻量级锁
         *  10  重量级锁
         *
         *
         * 1.重赋值前
         * 00000001    001
         * 2.重赋值后
         * 00000101    101
         * 3.thread线程获取偏向锁
         * 00000101    101
         * 4.thread线程执行完成之后，main线程获取锁，升级为轻量级锁
         * 10000000    00
         * 5.main线程锁退出后，变回无锁状态
         * 00000001    001
         */


        System.out.println("=======================================================偏向锁---->轻量级锁=====================================================");

    }

}
