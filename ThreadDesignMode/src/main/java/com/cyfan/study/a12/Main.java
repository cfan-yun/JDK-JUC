package com.cyfan.study.a12;

/**
 * ActiveObject模式（Actor模式） - 主动对象模式 主要功能其实是为了实现异步
 *  在ActiveObject模式中，出场的主动对象不仅仅是"有自己特有的线程"，它同时还具有可以从外部接收和处理异步消息并根据需要返回处理结果的特征。
 *  ActiveObject模式中的主动对象（是一个对象的集合）会通过自己特有的线程在合适的时机处理从外部接收到的异步消息。
 *  ActiveObject模式综合了ThreadPerMessage、Future、ProducerConsumer、GuardedSuspension、SingleThreadExecution、Balking模式
 *  也称为Actor模式。reactor模型的雏形。
 *
 *  目的：实现方法调用和任务执行进行分离
 *
 *
 *  case:
 *      需求：打印机发起请求（打印、复印）
 *      角色分析：
 *          委托者（客户端）：
 *              发起请求
 *          主动对象（active Object）
 *              给委托者提供接口
 *          proxy(代理)：
 *              负责把请求转换为对象，并传递给执行者线程（不是真正的去处理）
 *          执行者（调度者）线程：
 *              又是 消费者线程，具体负责入队和出队（出队后调用服务，真正执行）
 *          封装好的请求对象（task）：
 *              请求转换为具体的task(MethodRequest对象)，该对象会被塞进队列，且定义对应的服务处理类和返回值，入参
 *          服务类：
 *              具体处理请求的服务对应
 *          队列：
 *              存放Task(MethodRequest对象)
 *          返回值：
 *              FutureResult(提货单返回结果)
 *              RealResult（真实的结果）
 *         主动对象（Proxy、服务类）：都需要实现ActiveObject接口
 *
 *
 *   分析：
 *      1.实现了调用和执行的分离
 *      2.遵循开闭原则
 *      proxy是否安全？？ 安全
 *      service是否安全？？ 安全
 *
 *
 */
public class Main {
}
