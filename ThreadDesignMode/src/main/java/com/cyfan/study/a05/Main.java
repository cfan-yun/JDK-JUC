package com.cyfan.study.a05;

/**
 * Producer-Consumer模式（生产者-消费者模式）
 *      Producer：生成数据的线程
 *      Consumer：使用数据的线程
 *      Producer-Consumer模式在生产者和消费者之间加入了一个“桥梁角色”，该桥梁（队列）用于消除线程间处理速度的差异
 *  case:
 *      角色分析：
 *          生产者(ProducerThread)：
 *          消费者（ConsumerThread）：
 *          桥梁(队列MyQueue)：
 *          测试类(ProducerConsumerTest)：
 *  该模式其实还包含GuardedSuspension（保护性暂挂：保护queue,队列为空挂起消费者线程） 、SingleThreadExecution（synchronized）
 */
public class Main {
}
