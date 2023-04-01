package com.cyfan.my.test.thread.threadCommunication;

/**
 * notify 方法真的是取唤醒，执行unpark 方法？？并没有，只是去挪动了节点waitSet -> entryList。
 *     唤醒的唯一途径是：重量级锁退出才会unPark唤醒，所以notify需要在同步代码块中执行
 * Object.c -> methods[] ->JVM_MonitorNotify -> jvm.cpp ->ObjectSynchronizer::notify
 *  -> synchronizer.cpp -> (monitor->notify) -> ObjectMonitor::notify(唤醒waitSet链表头对应的线程，即最早加入到该链表的线程)
 *
 * notify挪动节点策略：
 *  policy == 0 :放到entryList的排头
 *  policy == 1 :放到entryList的结尾
 *  policy == 2 :entryList不为空，cas插入cxq队列排头， 为空放入entryList(jvm默认策略， 无法修改，想修改只能改源码)
 *  policy == 3 :直接插到cxq的末尾
 *  policy != 前序值 : 直接unPark唤醒，jvm默认为policy == 2 不可修改， unPark不会执行;
 *
 *ObjectMonitor::monitorExit() 重量级锁退出策略：
 *  QMode ==2 :直接从cxq头部unPark唤醒
 *  QMode ==3 :cxq队尾插入entryList后unPark唤醒
 *  QMode ==4 :cxq队头插入entryList后unPark唤醒
 *
 */
public class NotifyTest {
}
