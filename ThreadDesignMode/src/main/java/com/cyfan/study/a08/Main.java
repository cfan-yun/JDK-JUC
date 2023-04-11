package com.cyfan.study.a08;

/**
 * Worker Thread 模式
 *  在 Worker Thread 模式中，工人线程（workerThread） 会逐个取回工作，并进行处理。当所有工作都完成之后，工人线程会等待新的工作到来
 *  又叫做线程池模式
 *
 *  核心：两个队列
 *    任务队列：源码LinkedBlockingQueue
 *    线程队列：源码HashSet
 *
 * 需求：
 *    一个通用的地产商、建筑承建商系统。
 *      地产商发布合同，由建筑商施工小队获取对应合同，执行合同任务
 *    角色分析：
 *      地产商线程：生产者
 *      工程任务（合同）队列：taskQueue(contractQueue)
 *      工程任务（合同）：task（contract）
 *      施工队容器：workerThreadQueue
 *      施工队线程：workerThread 消费者
 *
 */
public class Main {


}
