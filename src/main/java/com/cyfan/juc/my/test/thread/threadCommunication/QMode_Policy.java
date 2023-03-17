package com.cyfan.juc.my.test.thread.threadCommunication;



/**
 * QMode和Policy策略综合案例分析
 * <p>
 *  entryList :线程执行notify后进入的队列，notify执行后也有可能直接进入cxq的头部根据policy决定
 *               * notify挪动节点策略：获取waitSet队列的头结点
 *                  *  policy == 0 :放到entryList的排头
 *                  *  policy == 1 :放到entryList的结尾
 *                  *  policy == 2 :entryList不为空，cas插入cxq队列排头， 为空放入entryList(jvm默认策略， 无法修改，想修改只能改源码)
 *                  *  policy == 3 :直接插到cxq的末尾
 *                  *  policy != 前序值 : 直接unPark唤醒，jvm默认为policy == 2 不可修改， unPark不会执行;
 *  重量级锁退出:真正执行unPark唤醒线程，根据QMode策略
 *              * ObjectMonitor::monitorExit() 重量级锁退出策略：
 *                  *  QMode ==2 :直接从cxq头部unPark唤醒
 *                  *  QMode ==3 :cxq队尾插入entryList后unPark唤醒
 *                  *  QMode ==4 :cxq队头插入entryList后unPark唤醒
 */
public class QMode_Policy {
    /**
     * 三个线程A、B C
     */

    private final  static Object LOCK =  new Object();


    public static void main(String[] args) {

        QMode_Policy qMode_policy = new QMode_Policy();
        qMode_policy.startThreadA();
        /**
         * thread-A, 获取到锁！
         * thread-A, 开始wait！
         * Thread-B, 获取到锁！
         * Thread-B, 开始sleep！
         * Thread-C, start！
         * Thread-B, 获取到锁之后，执行完sleep方法,开始执行notify
         * Thread-B,notify 执行完成， 即将释放锁
         * Thread-B, 释放锁完成
         * thread-A, 获取到锁之后，wait方法执行完了
         * thread-A, 即将释放锁
         * thread-A, 释放锁完成
         * Thread-C, 获取到锁！
         * Thread-C, 即将释放锁
         * Thread-C, 释放锁完成
         */
        /**
         *
         *
         * 分析代码执行过程：
         * 1.a启动获取到锁之后，a启动了b线程后，a wait , a线程wait之后释放锁，b线程获得锁启动了c线程。
         * b启动c后，b休眠2000毫秒，（此时c到达锁入口），然后b唤醒a，此时a, c一起抢锁
         * 结果：永远是是a先抢到锁，为什么？？？？？
         *
         * 思考问题：
         *1. 线程a在wait的时候做了什么？？？
         *   1)包装a线程为ObjectWaiter对象
         *   2)ObjectWait对象进入waitSet队列（waitSet本质是个双向循环链表）
         * 问题：wait时，为什么不需要cas抢着进入队列
         *      因为wait执行的时候是线程a是拥有锁的，线程a入队没人和他抢。
         *
         * 2. 线程c启动之后，由于线程b持有锁，那么线程c在干什么？？
         *       线程c在竞争锁，如果竞争不到（本例子肯定竞争不到），进入cxq队列阻塞，在cxq的头部
         *
         * 3. 线程b在notify之后发生了什么？？？
         *              notify挪动节点策略：！！！！！获取waitSet队列的头结点（此处对应的则是线程a）
         *                  *  policy == 0 :放到entryList的排头
         *                  *  policy == 1 :放到entryList的结尾
         *                  *  policy == 2 :entryList不为空，cas插入cxq队列排头， 为空放入entryList(jvm默认策略， 无法修改，想修改只能改源码)
         *                  *  policy == 3 :直接插到cxq的末尾
         *                  *  policy != 前序值 : 直接unPark唤醒，jvm默认为policy == 2 不可修改， unPark不会执行;
         *     由于默认 policy == 2,  entryList此时为空，线程a 从waitSet头节点转移到entryList中
         *4. 此时c 在哪里？？？
         *    !!!!c在cxq的头部
         *5. 那么b在notify执行之后，c在cxq头部，a在entryList中, 此时线程b释放重量级锁，重量级锁退出时，会根据QMode参数抉择究竟谁先被唤醒
         *          重量级锁退出:真正执行unPark唤醒线程，根据QMode策略
         *              * ObjectMonitor::monitorExit() 重量级锁退出策略：
         *                  *  QMode ==2 :直接从cxq头部unPark唤醒
         *                  *  QMode ==3 :cxq队尾插入entryList后unPark唤醒 (此时entryList 和 cxq顺序一致)
         *                  *  QMode ==4 :cxq队头插入entryList后unPark唤醒 (此时entryList 和 cxq顺序相反)
         *                  *  QMode ==0 :啥都不做，继续往下走。（jvm默认是0）。
         *    !!!!!!***** 后续判断entryList是否为空，不为空唤醒entryList中第一个元素， entryList为空，判断
         *        head        tail         元素1，2，3，4，5，6顺序头插入cxq ，即是6，5，4，3，2，1
         *                                  cxq 中元素从头部取出， 尾插入entryList 6，5，4，3，2，1
         *                                  cxq 中元素从头部取出， 头插入entryList 1，2，3，4，5，6
         *
         * ！！！！！！！！！！！！！！！由此可以确定线程a一定在线程c之前被唤醒
         *
         */


    }

    public  void startThreadA(){
            new Thread(()->{
                synchronized (LOCK){
                    System.out.println(Thread.currentThread().getName() + ", 获取到锁！" );
                    //启动线程B
                    startThreadB();
                    System.out.println(Thread.currentThread().getName() + ", 开始wait！" );

                    try {
                        //线程A wait
                        LOCK.wait();//A卡死在这了，（wait会释放锁）
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println(Thread.currentThread().getName()+", 获取到锁之后，wait方法执行完了");
                    System.out.println(Thread.currentThread().getName()+", 即将释放锁");
                }
                System.out.println(Thread.currentThread().getName()+", 释放锁完成");

            }, "thread-A").start();

    }

    private void startThreadB() {

        new Thread(()->{
            synchronized (LOCK){
                System.out.println(Thread.currentThread().getName() + ", 获取到锁！" );
                //启动线程C
                startThreadC();
                System.out.println(Thread.currentThread().getName() + ", 开始sleep！" );

                try {
                    //线程B sleep
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(Thread.currentThread().getName()+", 获取到锁之后，执行完sleep方法,开始执行notify");
                //线程B唤醒其他线程,
                LOCK.notify();//挪动线程A所在节点
                System.out.println(Thread.currentThread().getName()+",notify 执行完成， 即将释放锁");
            }
            System.out.println(Thread.currentThread().getName()+", 释放锁完成");

        }, "Thread-B").start();
    }

    private void startThreadC() {
        new Thread(()->{
            System.out.println(Thread.currentThread().getName() + ", start！" );
            synchronized (LOCK){
                System.out.println(Thread.currentThread().getName() + ", 获取到锁！" );
                //线程B唤醒其他线程
                //LOCK.notify();
                System.out.println(Thread.currentThread().getName()+", 即将释放锁");
            }
            System.out.println(Thread.currentThread().getName()+", 释放锁完成");

        }, "Thread-C").start();
    }
}
