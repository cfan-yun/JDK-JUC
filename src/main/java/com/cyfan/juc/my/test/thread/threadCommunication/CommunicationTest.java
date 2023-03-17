package com.cyfan.juc.my.test.thread.threadCommunication;


/**
 * 1.wait/notify机制
 *    ·不使用wait/notify 机制。怎么进行线程间通信？
 *    通常消费者线程，使用while(true)做轮询，直到生产者生产了产品，那么消费者才会停止轮询，这样就浪费了cpu资源（不可取）
 *
 *    ·什么是wait/notify 机制
 *      notify 唤醒的是正在wait的线程
 *      wait 线程卡死，wait放弃锁，放弃cup执行权，notify之后，需要重新枪锁
 * 问题：notify 真的去唤醒线程了嘛？
 *      其实并没有只是去队列里面移动节点而已, 没有调用unpark 唤醒动作
 */
public class CommunicationTest {


}
