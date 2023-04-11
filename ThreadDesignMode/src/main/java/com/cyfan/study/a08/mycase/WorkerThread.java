package com.cyfan.study.a08.mycase;

import com.cyfan.study.util.RandomUtils;

public class WorkerThread extends Thread{


    private final MyQueue<ContractTask> contractTaskMyQueue;

    public WorkerThread(String name, MyQueue<ContractTask> contractTaskMyQueue){
        super(name);
        this.contractTaskMyQueue = contractTaskMyQueue;
    }

    @Override
    public void run() {
        try {
            while (true){
                ContractTask take = contractTaskMyQueue.take();//施工队获取任务
                take.run();//执行任务
                Thread.sleep(RandomUtils.getRandom(1000));
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
