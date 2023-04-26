package com.cyfan.study.a02.locks;

/**
 * AQS + CAS
 * *         CAS 提供的功能总结
 * *         1.同步状态的原子性管理： state + cas
 * *         2.线程的阻塞与解除阻塞： park, unpark
 * *         3.队列的管理：双向链表
 * *    知识前提：
 * *       可重入锁的实现原理：每个锁对象拥有一个锁计数器和一个指向持有该锁的线程的指针
 * *       案例比较：ReentrantLock 和 Synchronized 这两者的底层其实是一样的
 * *           1.相同点：
 * *               synchronized 底层是三大队列 ： cxq entryList waitSet
 * *               ReentrantLock 底层是2个队列： (同步队列(cxq和entryList)和条件队列（waitSet）)
 * *           2.重入标记
 * *               synchronized  -------recursions recursions++
 * *               ReentrantLock -------state 0 -> 1 -> 2
 * *           3.拥有锁的当前线程的指针（引用）
 * *                  synchronized  -------owner(Thread)
 * *                  ReentrantLock -------owner
 * *            手写实现的重点： 同步队列 state  owner
 */
public class Main {
}
