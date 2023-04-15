package com.cyfan.study.a11;

/**
 * ThreadSpecialStorage 模式 线程特有存储模式
 *      即使只有一个入口，也会在内部为   每一线程分配特有的存储空间   的模式。
 *      说白了就是，每个线程一份存储空间各自玩各自的。有两种方式：
 *          1.局部变量（定义在方法里的，或者方法参数）。
 *              天然的一个线程一份。
 *          2.实例变量（定义在类里面）共有资源。
 *              new instance
 *              ThreadLocal
 *
 *
 *  主要用来做线程里的上下文资源共享
 *
 *
 */
public class Main {
}
