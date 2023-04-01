package com.cyfan.my.test.thread.threadCommunication;

/**
 *
 * 总结：一个线程不可能同时出现在3个队列，同一时刻只能出现在一个队列里面
 *
 * cxq 移动节点和waitSet移动节点到entryList有什么区别？？ cxq整个移动到entryList waitSet只移动头节点到entryList
 *
 * 三大队列：
 *  cxq :线程抢锁后阻塞队列（park）
 *  waitSet : 线程执行wait方法进入的队列 (park）
 *  entryList :线程执行notify后进入的队列，notify执行后也有可能直接进入cxq的头部根据policy决定
 *               * notify挪动节点策略：
 *                  *  policy == 0 :放到entryList的排头
 *                  *  policy == 1 :放到entryList的结尾
 *                  *  policy == 2 :entryList不为空，cas插入cxq队列排头， 为空放入entryList(jvm默认策略， 无法修改，想修改只能改源码)
 *                  *  policy == 3 :直接插到cxq的末尾
 *                  *  policy != 前序值 : 直接unPark唤醒，jvm默认为policy == 2 不可修改， unPark不会执行;
 *  重量级锁退出:真正执行unPark唤醒线程，根据QMode策略
 *              * ObjectMonitor::monitorExit() 重量级锁退出策略：
 *                  *  QMode ==2 :直接从cxq头部unPark唤醒
 *                  *  QMode ==3 :cxq队尾插入entryList后unPark唤醒
 *                  *  QMode ==4 :cxq队头插入entryList后unPark唤醒
 *
 *
 *
 */
public class Cxq_WaitSet_EntryList {
}
