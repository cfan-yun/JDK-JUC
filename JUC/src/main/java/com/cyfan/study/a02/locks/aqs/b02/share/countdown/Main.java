package com.cyfan.study.a02.locks.aqs.b02.share.countdown;

/**
 * <p>案例1： main线程等待两个子线程先执行完之后再执行 {@link CountDownLatchTest}
 * <p>案例2：子线程等待main线程执行之后再执行(main未结束)，子线程执行完之后，再唤醒mian线程执行，线程之间交替执行 需要两个CountDownLatch {@link CountDownLatchTest1}
 *
 */
public class Main {
}
