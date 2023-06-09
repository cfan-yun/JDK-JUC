package com.cyfan.my.test.threadPool;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池：
 *      Java中线程跟原生线程是一一对应的
 *      通过pthread_create()由操作系统创建，需要切换到内核态执行创建
 *      通过pthread_exit()由操作系统销毁，需要切换到内核态执行销毁
 *      实现思想：创建多个线程，放到池子里面待用，如果由业务过来，那么从池子中取出来用，用完之后还回去。
 *                不执行创建、销毁的动作。这两个步骤非常消耗资源，需要切换到内核态，线程池节省资源。
 * 1.为什么使用线程池？
 *  1、降低了资源消耗（避免了用户态和内核态的频繁切换，从而节省了资源）
 *  2、提高了响应速度，提高了吞吐量。节省了线程创建和销毁动作所需的时间。（任务抵达的时候可以立即执行，不需创建和销毁）
 *  3、提高了线程的可管理性：可以使用线程池对线程做统一的管理、分配以及监控。
 *
 *
 * 线程池和工厂类比：
 *  工厂--线程池
 *  订单--任务
 *  正式员工--核心线程
 *  临时工--普通线程
 *  总工人--最大线程数
 *  仓库--任务队列
 *  调度员--getTask()
 *
 *  线程池细节：
 *      1.构造函数
 *      2.线程池推荐的3个队列
 *          -SynchronousQueue(同步移交)
 *              此队列里面没有容器，一个生产者线程，当他生产产品（put）的时候，如果当前没有消费者想要消费
 *              那么此生产者线程必须阻塞，等待一个消费者线程来调用(take),take操作会唤醒生者线程，同时消费者
 *              线程进行消费，所以这叫做一次配对
 *                  1.put 和take方法必须配对
 *  *               2.默认非公平，后启动的生产者先被消费
 *                          非公平使用栈，公平使用队列实现
 *              原理：内部是使用cas来实现线程的安全访问，不向LinkedBlockingQueue、ArrayBlockingQueue使用aqs,并且由公平非公平之分（队列和栈）
 *
 *          -LinkedBlockingQueue
 *              无界队列（严格意义上讲，不是无界队列，最大容量是Integer.MAX_VALUE。），基于链表结构。如果使用这个无界队列的话，
 *              当核心线程都繁忙的时候，后续任务就会加入这个无限队列，此时线程池中的线程数不会超过总线程数量，
 *              好处是提高了系统吞吐量，但是代价就是牺牲了内存，甚至会引发OOM异常，常见的做法是指定队列容量。
 *          -ArrayBlockingQueue
 *              有界队列，基于数组实现，在线程池初始化时，指定了队列容量，缺点就是无法再调整，好处就是可以防止资源耗尽。
 *
 */
public class PoolNote {

    public static void main(String[] args) {
        //ThreadPoolExecutor
    }
}
