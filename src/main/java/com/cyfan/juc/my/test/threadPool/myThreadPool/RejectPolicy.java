package com.cyfan.juc.my.test.threadPool.myThreadPool;

/**
 * 拒绝策略接口
 */
public interface RejectPolicy {

    void rejectHandler();

}
