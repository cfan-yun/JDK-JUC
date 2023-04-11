package com.cyfan.study.a08.mycase;

import com.cyfan.study.a06.mycase.ReadThread;
import com.cyfan.study.util.RandomUtils;

/**
 * 地产商线程
 */
public class RealEstateThread  extends Thread{

    private final MyQueue<ContractTask> contractTaskMyQueue;

    public RealEstateThread(String name, MyQueue<ContractTask> contractTaskMyQueue){
        super(name);
        this.contractTaskMyQueue = contractTaskMyQueue;
    }

    @Override
    public void run() {
        for (int i = 0; i < contractTaskMyQueue.getCapacity(); i++) {
            try {
                contractTaskMyQueue.put(new ContractTask(i, getName()));//生产合同。。。
                Thread.sleep(RandomUtils.getRandom(1000));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
