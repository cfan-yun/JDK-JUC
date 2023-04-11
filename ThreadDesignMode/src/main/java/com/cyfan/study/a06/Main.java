package com.cyfan.study.a06;

/**
 * Read-Write Lock模式 （读写锁模式）
 *
 *  当线程读取实例状态时，实例状态不会发生变化，实例的状态仅仅在线程执行写入操作时，才会发生变化。
 *  写才会变化、读不会变化。
 *  写的时候不能读。
 *  读的时候不能改。
 *  写的时候其他线程不可修改。
 *  读的时候其他线程可以读。
 *
 *               |      读       写
 *         ------|-----------------------
 *           读  |       yes     no
 *           写  |       no      no
 *
 *    经典应用：数据库的共享锁（读锁）和排他锁（写锁）， 底层是读写锁的并发
 *    case:小说连载，连载写小说，其他人读，没写完不让读。
 *       角色分析：
 *          读线程（多个）
 *          写线程（多个）
 *          文本（共享数据/公共资源）
 *              有数组
 *          自定义读写锁
 *          测试类
 *     难点：
 *          读线程挂起条件：writingWriters > 0;
 *          写线程挂起条件：readingReaders > 0 || writingWriters > 0;
 *          写线程挂起的几率比读线程高，读线程多时，会导致写线程一直拿不到锁，不能写，如何解决这个问题？？？？
 *              解决：增加读线程挂起的几率.两个参数：是否写线程优先（writerHavePriority）， 正在等待的写线程数（waitingWriters）
 *                    1.读锁解锁时，设置 writerHavePriority = true;
 *                    2.写锁解锁时，设置 writerHavePriority = false;
 *                    3.写锁加锁时，wait之前 waitingWriters++ , wait被唤醒继续执行之后 waitingWriters--；
 *                    4.在读线程挂起条件上增加条件为 writingWriters > 0 || (writerHavePriority && waitingWriters > 0); 增加读线程挂起几率
 *    包含的模式：
 *      SingleThreadExecution模式 synchronized
 *      Immutable模式       private final MyAbstractReadWriteLock writeLock = new MyReadWriteLock();
 *      GuardedSuspension模式  保护状态（条件），暂挂读/写线程（wait）
 *          守护条件在readLock和writeLock中
 *          writingWriters > 0 || (writeHavePriority && waitingWriters > 0) 和 readingReaders > 0 || writingWriters > 0
 *      Balking模式 无 表现：不满足条件直接返回
 *
 *
 *   适用场景：读线程比写线程多
 *      物理锁：synchronized
 *      逻辑锁：我们自己借助 synchronized 实现的readLock和writeLock
 *      所谓比较就是比较读写锁和直接用synchronized的性能差别
 *
 */
public class Main {
}
