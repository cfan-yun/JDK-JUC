package com.cyfan.study.a02.locks;

import com.cyfan.study.a02.locks.aqs.b01.execlusive.reentraintlock.MyReentrantLock;
import com.cyfan.study.a02.locks.aqs.b01.execlusive.reentraintlock.MyReentrantLockTest;
import com.cyfan.study.a02.locks.aqs.b02.share.countdown.CountDownLatchTest;
import com.cyfan.study.a02.locks.aqs.b02.share.countdown.MyCountDownLatch;
import com.cyfan.study.a02.locks.aqs.b02.share.countdown.MyCountDownLatchBySynchronized;
import com.cyfan.study.a02.locks.aqs.b02.share.semaphore.MySemaphore;
import com.cyfan.study.a02.locks.aqs.b02.share.semaphore.MySemaphoreBySynchronized;
import com.cyfan.study.a02.locks.aqs.b02.share.semaphore.SemaphoreTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

/**
 * AQS + CAS
 * <p>
 * CAS 提供的功能总结
 * <p>         1.同步状态的原子性管理： state + cas
 * <p>         2.线程的阻塞与解除阻塞： park, unpark
 * <p>         3.队列的管理：双向链表
 * <p> {@link ReentrantLock}--{@link MyReentrantLock}
 * <p> 案例：{@link MyReentrantLockTest}
 * <p>    可重入锁
 * <p>    知识前提：
 * <p>       可重入锁的实现原理：每个锁对象拥有一个锁计数器和一个指向持有该锁的线程的指针
 * <p>       案例比较：ReentrantLock 和 Synchronized 这两者的底层其实是一样的
 * <p>           1.相同点：
 * <p>               synchronized 底层是三大队列 ： cxq entryList waitSet
 * <p>               ReentrantLock 底层是2个队列： (同步队列(cxq和entryList)和条件队列（waitSet）)
 * <p>           2.重入标记
 * <p>               synchronized  -------recursions recursions++
 * <p>               ReentrantLock -------state 0 -> 1 -> 2
 * <p>           3.拥有锁的当前线程的指针（引用）
 * <p>                  synchronized  -------owner(Thread)
 * <p>                  ReentrantLock -------owner
 * <p>            手写实现的重点： 同步队列 state  owner
 * <p>
 * <p>{@link Semaphore }---{@link MySemaphore}
 * <p>   案例: 50个人去吃饭，饭店只有四张桌子  {@link SemaphoreTest}
 * <p>{@link CountDownLatch }-- {@link MyCountDownLatch}
 * <p>   案例：比如2个线程采集服务器数据，线程1 采集cpu实时数据，内存实时数据，线程2 采集qps，流量，主线程等待线程1，2都完成后（主线程等待1，2的数据），然后数据入库{@link CountDownLatchTest}
 * <p>   独占锁和共享锁的异同：
 * <p>        1.独占锁是线程独占的，同一时刻，只有一个线程能拥有独占锁（state同时只能有一个线程拥有），aqs里面，将这个线程放置到exclusiveOwnerThread字段上。
 * <p>        2.共享锁是线程共享的，同一时刻能有多个线程拥有共享锁（state 可以多人共享），不是state共享是state = 10 这个数字是共享的，aqs并没有字段来存储获取到共享锁的线程。
 * <p>        3.如果一个线程刚刚获取了共享锁，那么在其之后等待的线程也有可能获取到锁，但是独占锁不会，因为锁只有一把。
 * <p>        4.不管是独占锁还是共享锁，都需要唤醒在其后面等待的线程。
 * <p>        5.独占锁中释放锁调用的是unparkSuccessor() ,共享中释放锁使用的是doReleaseShared()
 * <p>        6.独占锁和获取锁和释放锁，都没有自旋的操作，而共享锁加锁和释放锁都是有自旋操作的，原因是，共享锁同时有多个线程能够获取和释放锁。
 * <p>        7.独占锁中线程被阻塞唤醒之后去抢锁，抢到锁之后会修改头指针，改完头指针就结束了，而共享锁改完头指针之后，还会去判断是否有多余的锁释放出来，如果有多余的锁释放出来，那么继续去唤醒自己的后继节点。
 * <p>   难点：共享锁出队稍微复杂一些，独占模式下直接出队，没有竞争。
 * <p>         共享模式下，则需要cas设置头结点，因为可能有多个节点同时出队，同时还需要向后传播状态，保证后面的线程可以及时获得锁;此外还可能发生中断或者异常出队，则需要考虑头尾的情况，保证不会影响队列的结构。
 * <p>
 *
 * <p> ReentrantLock,Semaphore,CountDownLatch对state 字段的理解：
 * <p> ReentrantLock是同一个时刻，只有一个线程可以拥有state字段
 * <p> Semaphore 是同一时间段可以有多个线程拥有state字段，state = 10 就代表有10把锁；注意：state 可增可减
 * <p> CountDownLatch 里的state = 5, 表示这把锁（state表示有5个计数的锁），个人认为是只有1把锁，但是这一把锁里面需要5个线程，5次释放（countDown），才能完全释放。注意：state 只能减少
 * <p>
 * <p>  面试题：
 * <p>       1.synchronized 实现countDownLatch {@link MyCountDownLatchBySynchronized}
 * <p>       2.synchronized 实现 Semaphore {@link MySemaphoreBySynchronized}
 * <p>
 * <p>  总结:只要是基于aqs的工具类,几乎都可以用synchronized实现
 * <p>       原因:aqs底层(2个队列)和synchronized底层(3个队列)是一样的
 * <p>       aqs:同步队列、                  条件队列[await,signal/signalAll]
 * <p>       synchronized:cxq、entryList、   waitSet[ wait, notify/notifyAll]。
 * <p>       {@link com.cyfan.my.test.thread.threadCommunication.Cxq_WaitSet_EntryList}
 * <p>       对应关系：同步队列（cxq、entryList） 、条件队列 （waitSet）
 *
 * <table>
 *     <tr>
 *         <td><td/>
 *         <td><td/>
 *     </tr>
 *
 * </table>
 *
 *
 * <p>
 */
public class Main {
}
