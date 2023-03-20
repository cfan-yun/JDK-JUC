package com.cyfan.juc.my.test.thread.threadCommunication;

/**
 * Object.c -> methods[] ->JVM_MonitorWait -> jvm.cpp ->ObjectSynchronizer::wait
 *  -> synchronizer.cpp -> (monitor->wait) -> ObjectMonitor::wait（要等待的线程封装成ObjectWaiter对象，并加入链表中，然后释放占有的锁，让当前线程休眠）
 *  -> 处理中断异常 ->AddWait (加入waitSet链表中双向环形链表) ->  parkEvent->park(millis)(挂起线程)
 *
 *  wait方法底层
 *  将当前调用wait方法的线程包装成ObjectWaiter对象，加入到一个叫waitSet的双向环形链表中，然后线程执行park挂起
 *
 *  wait(t) 多少t时间后，自动被唤醒
 *  wait(0)/wait(), 必须等待其他线程将其唤醒。
 */
public class WaitTest {

}
