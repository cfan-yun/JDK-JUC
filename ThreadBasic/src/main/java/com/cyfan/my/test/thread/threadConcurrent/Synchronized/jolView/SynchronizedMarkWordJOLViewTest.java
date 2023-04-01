package com.cyfan.my.test.thread.threadConcurrent.Synchronized.jolView;


import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

/**
 * 查看jvm锁升级之后，对象头中的比特位信息
 * 此处使用的是jol-core 0.10版本，使用0.15版本，分析会直接告诉你当前是什么锁。
 *         <dependency>
 *             <groupId>org.openjdk.jol</groupId>
 *             <artifactId>jol-core</artifactId>
 *             <version>0.10</version>
 *         </dependency>
 *
 */
public class SynchronizedMarkWordJOLViewTest {
    static LockObject noLockObj = new LockObject();
    static LockObject biasedLockObj = new LockObject();
    static LockObject fastLockObj = new LockObject();
    static LockObject slowLockObj =  new LockObject();
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
 * 1.无锁状态
 */
        System.out.println("=======================================================无锁=====================================================");
        System.out.println(">>>>>>>>>>>>>Hash前");
        System.out.println(ClassLayout.parseInstance(noLockObj).toPrintable()); //打印lockObject 的内存布局

        /**
         * 由分析可知：
         *  对象头有12个字节
         *      64位虚拟机markWord只有64位
         *      12*8 = 96位
         *      96 - 64 =  32 那么这多余的32位是用来干嘛的？？？  官网得到信息 https://openjdk.org/groups/hotspot/docs/HotSpotGlossary.html
         *      对象头object header = mark word(64) + klass pointer(32) //由于默认开启指针压缩，所以是32位，可添加jvm参数-XX:-UseCompressedOops 关闭指针压缩，就是64位了
         *  难点：分析对象头中的markWord比特位存储的值是什么？？ 顺序是什么样的。突破点：无锁状态的hash值
         */
        System.out.println(">>>>>>hashCode>>>>>>>0x"+Integer.toHexString(noLockObj.hashCode()));
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>hash后");
        System.out.println(ClassLayout.parseInstance(noLockObj).toPrintable()); //打印lockObject 的内存布局
        /**
         * |-----------------------------------------------------------------------------------------------------------------|
         * |                                             Object Header(128bits)                                              |
         * |-----------------------------------------------------------------------------------------------------------------|
         * |                                   Mark Word(64bits)               |  Klass Word(64bits)    |      State         |
         * |-----------------------------------------------------------------------------------------------------------------|
         * | unused:25|identity_hashcode:31|unused:1|age:4|biase_lock:1|lock:2 | OOP to metadata object |      Nomal         |
         * |-----------------------------------------------------------------------------------------------------------------|
         * | thread:54|      epoch:2       |unused:1|age:4|biase_lock:1|lock:2 | OOP to metadata object |      Biased        |
         * |-----------------------------------------------------------------------------------------------------------------|
         * |                     ptr_to_lock_record:62                 |lock:2 | OOP to metadata object | Lightweight Locked |
         * |-----------------------------------------------------------------------------------------------------------------|
         * |                    ptr_to_heavyweight_monitor:62          |lock:2 | OOP to metadata object | Heavyweight Locked |
         * |-----------------------------------------------------------------------------------------------------------------|
         * |                                                           |lock:2 | OOP to metadata object |    Marked for GC   |
         * |-----------------------------------------------------------------------------------------------------------------|
         */

        /**
         * 括号中是二进制位，外是对应的16进制数
         *
         *
         * hash前
         * 01 00 00 00 (00000001 00000000 00000000 00000000)
         * 00 00 00 00 (00000000 00000000 00000000 00000000)
         * 43 c1 00 f8 (01000011 11000001 00000000 11111000)
         *
         * >>>>>>hashCode>>>>>>>0x 0e e7 d9 f1
         *
         * hash后
         * 01 f1 d9 e7 (00000001 *11110001 11011001 11100111)
         * 0e 00 00 00 (00001110* 00000000 00000000 00000000)
         * 43 c1 00 f8 (01000011 11000001 00000000 11111000)
         * |                                   Mark Word(64bits)                |  Klass Word(64bits)    |      State|
         * | unused:25|identity_hashcode:31|unused:1|age:4|biase_lock:1|lock:2  | OOP to metadata object |      Nomal|
         * 由此可以看出，无锁状态下，hashcode存储是在*号中间部分比特位，
         *              第二行*号以后的24位0， 与*前面1位0，共25位高位未使用，
         *              第一行*号前的最低位8位存储的则是无锁时的未使用1位，分代年龄4位，偏向锁标志1位，锁标志2位 共8位
         */
        System.out.println("=======================================================无锁=====================================================");
/**
 * 2.偏向锁
 */
        System.out.println("=======================================================偏向锁=====================================================");
        System.out.println(">>>>>偏向锁在jvm启动后,启动后，启动后，四秒钟才会开启！！！！");
        Thread.sleep(5000); //休眠五秒
        System.out.println(".>>>>>>>>>>>>>>>重赋值之前...");
        System.out.println(ClassLayout.parseInstance(biasedLockObj).toPrintable()); //打印lockObject 的内存布局
        biasedLockObj = new LockObject();//重赋值
        System.out.println(">>>>>>>>>>>>>>>>>线程ID="+Thread.currentThread().getId());
        System.out.println(">>>>>>>>>>>>>>>>加锁之前....");
        System.out.println(ClassLayout.parseInstance(biasedLockObj).toPrintable()); //打印lockObject 的内存布局
        synchronized (biasedLockObj){
            System.out.println(">>>>>>>>>>>>>>加锁中...");
            System.out.println(ClassLayout.parseInstance(biasedLockObj).toPrintable()); //打印lockObject 的内存布局
        }
        System.out.println(">>>>>>>>>>>>>>加锁之后...");
        System.out.println(ClassLayout.parseInstance(biasedLockObj).toPrintable()); //打印lockObject 的内存布局

        /**
         * 1.虚拟机在启动后4秒中才会开启偏向锁，因此要在虚拟机启动四秒之后，才能使用偏向锁，因此要在虚拟机启动后四秒初始化锁对象
         * 或者设置jvm参数 修改偏向锁延迟时间：-XX:+UseBiasedLocking -XX:BiasedLockingStartupDelay=0
         * 01 00 00 00 (00000001 00000000 00000000 00000000)
         * 00 00 00 00 (00000000 00000000 00000000 00000000)
         * 43 c1 00 f8 (01000011 11000001 00000000 11111000)
         * 2.加锁之前
         * 05 00 00 00 (00000101 00000000 00000000 00000000)
         * 00 00 00 00 (00000000 00000000 00000000 00000000)
         * 43 c1 00 f8 (01000011 11000001 00000000 11111000)
         * 3.加锁中
         * 05 40 63 03 (00000101 01000000 01100011 00000011)
         * 00 00 00 00 (00000000 00000000 00000000 00000000)
         * 43 c1 00 f8 (01000011 11000001 00000000 11111000)
         * 4.加锁后
         * 05 40 63 03 (00000101 01000000 01100011 00000011)
         * 00 00 00 00 (00000000 00000000 00000000 00000000)
         * 43 c1 00 f8 (01000011 11000001 00000000 11111000)
         * 4秒后开启偏向锁之后，可以看到，最低的8位00000101 最后两位 01 无锁，倒数第三位 1 是偏向锁，前五位|unused:1|age:4|
         *
         *
         * |                                   Mark Word(64bits)               |  Klass Word(64bits)    |      State         |
         * |-----------------------------------------------------------------------------------------------------------------|
         * | thread:54|      epoch:2       |unused:1|age:4|biase_lock:1|lock:2 | OOP to metadata object |      Biased        |
         *
         * 为什么偏向锁要延迟4秒？？
         *  因为jvm启动的时候需要加载很多资源， 在这些对象上加上偏向锁没有意义，减少了大量偏向锁撤销的成本，所以把偏向锁延迟四秒。
         *
         *
         */


        System.out.println("=======================================================偏向锁=====================================================");


/**
 * 3.轻量级锁
 */
        System.out.println("=======================================================轻量级锁=====================================================");
        System.out.println(">>>>>>>>>>>>>>>>加锁之前....");
        System.out.println(ClassLayout.parseInstance(fastLockObj).toPrintable()); //打印lockObject 的内存布局
        synchronized (fastLockObj){
            System.out.println(">>>>>>>>>>>>>>加锁中...");
            System.out.println(ClassLayout.parseInstance(fastLockObj).toPrintable()); //打印lockObject 的内存布局
        }
        System.out.println(">>>>>>>>>>>>>>加锁之后...");
        System.out.println(ClassLayout.parseInstance(fastLockObj).toPrintable()); //打印lockObject 的内存布局

        /**
         * 1.加锁之前
         * 01 00 00 00 (00000001 00000000 00000000 00000000)
         * 00 00 00 00 (00000000 00000000 00000000 00000000)
         * 43 c1 00 f8 (01000011 11000001 00000000 11111000)
         * 2.加锁中
         * 08 f2 24 03 (00001000 11110010 00100100 00000011)
         * 00 00 00 00 (00000000 00000000 00000000 00000000)
         * 43 c1 00 f8 (01000011 11000001 00000000 11111000)
         * 3.加锁后
         * 01 00 00 00 (00000001 00000000 00000000 00000000)
         * 00 00 00 00 (00000000 00000000 00000000 00000000)
         * 43 c1 00 f8 (01000011 11000001 00000000 11111000)
         *
         *  可以看到加锁中 00001000 最后2位是00 ，那么说明是轻量级锁
         *
         * |                                   Mark Word(64bits)               |  Klass Word(64bits)    |      State         |
         * |-----------------------------------------------------------------------------------------------------------------|
         * |                     ptr_to_lock_record:62                 |lock:2 | OOP to metadata object | Lightweight Locked |
         *
         */


        System.out.println("=======================================================轻量级锁=====================================================");



/**
* 4.重量级锁
*/
        System.out.println("=======================================================重量量级锁=====================================================");
        System.out.println(">>>>>>>>>>>>>>>>加锁之前....");
        System.out.println(ClassLayout.parseInstance(slowLockObj).toPrintable()); //打印lockObject 的内存布局

        Runnable runnable =  () -> {
            synchronized (slowLockObj){
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println(">>>>>>>>>>>>>>>>>>>>"+Thread.currentThread().getName()+",执行完成......");
        };
        Thread thread = new Thread(runnable);
        thread.start();
        Thread.sleep(1000);//休眠一秒确保等待thread线程加锁，并且thread线程没有执行完成
        System.out.println(">>>>>>>>>>>>>>"+thread.getName()+",加锁中.......");
        System.out.println(ClassLayout.parseInstance(slowLockObj).toPrintable()); //打印lockObject 的内存布局

        //主线程加锁
        synchronized (slowLockObj){
            System.out.println(">>>>>>>>>>>>>>main线程，加锁中...");
            System.out.println(ClassLayout.parseInstance(slowLockObj).toPrintable()); //打印lockObject 的内存布局
        }
        System.out.println(">>>>>>>>>>>>>>main线程，加锁后...");
        System.out.println(ClassLayout.parseInstance(slowLockObj).toPrintable()); //打印lockObject 的内存布局
        Thread.sleep(1000);
        System.out.println(">>>>>>>>>>>>>>main线程，加锁后休眠1秒...");
        System.out.println(ClassLayout.parseInstance(slowLockObj).toPrintable()); //打印lockObject 的内存布局
        Thread.sleep(1000);
        System.out.println(">>>>>>>>>>>>>>main线程，加锁后休眠2秒...");
        System.out.println(ClassLayout.parseInstance(slowLockObj).toPrintable()); //打印lockObject 的内存布局
        Thread.sleep(1000);
        System.out.println(">>>>>>>>>>>>>>main线程，加锁后休眠3秒...");
        System.out.println(ClassLayout.parseInstance(slowLockObj).toPrintable()); //打印lockObject 的内存布局
        /**
         * 1.加锁之前
         * 2.thread子线程加锁中
         * 3.main线程加锁中
         * 4.main线程加锁后
         * 5.main线程加锁后1秒
         * 6.main线程加锁后2秒
         *
         * 重量级锁在所有线程跑完之后延迟几秒钟才会恢复为无锁
         *
         *思路主要看第一行第一列的最后2位 01 无锁或偏向锁（前看1位 0为无锁， 1为偏向锁）， 10 重量级锁， 00 轻量级锁
         * 1.加锁之前00000001  无锁 看最后三位
         * 01 00 00 00 (00000001 00000000 00000000 00000000)
         * 00 00 00 00 (00000000 00000000 00000000 00000000)
         * 43 c1 00 f8 (01000011 11000001 00000000 11111000)
         * 2.thread子线程加锁中 00100000 轻量级锁 看最后两位
         * 20 f4 d4 1f (00100000 11110100 11010100 00011111)
         * 00 00 00 00 (00000000 00000000 00000000 00000000)
         * 43 c1 00 f8 (01000011 11000001 00000000 11111000)
         * 3.main线程加锁中 10111010 重量级锁 看最后两位
         * ba 1c 05 1c (10111010 00011100 00000101 00011100)
         * 00 00 00 00 (00000000 00000000 00000000 00000000)
         * 43 c1 00 f8 (01000011 11000001 00000000 11111000)
         * 4.main线程加锁后 10111010 重量级锁 看最后两位
         * ba 1c 05 1c (10111010 00011100 00000101 00011100)
         * 00 00 00 00 (00000000 00000000 00000000 00000000)
         * 43 c1 00 f8 (01000011 11000001 00000000 11111000)
         * 5.main线程加锁后1秒 10111010 重量级锁 看最后两位
         * ba 1c 05 1c (10111010 00011100 00000101 00011100)
         * 00 00 00 00 (00000000 00000000 00000000 00000000)
         * 43 c1 00 f8 (01000011 11000001 00000000 11111000)
         * 6.main线程加锁后2秒 00000001 无锁 看最后三位
         * 01 00 00 00 (00000001 00000000 00000000 00000000)
         * 00 00 00 00 (00000000 00000000 00000000 00000000)
         * 43 c1 00 f8 (01000011 11000001 00000000 11111000)
         *
         *
         */
        System.out.println("=======================================================重量量级锁=====================================================");



    }

}
