package com.cyfan.study.a08.mycase;

import com.cyfan.study.a06.mycase.ReadThread;

public class Test {

    public static void main(String[] args) {
        try {
            TaskExecutor taskExecutor = new TaskExecutor(10, 100);
            taskExecutor.startWorks();


            new RealEstateThread("soho",taskExecutor.getContractTaskQueue()).start();
            new RealEstateThread("万科",taskExecutor.getContractTaskQueue()).start();
            new RealEstateThread("恒大",taskExecutor.getContractTaskQueue()).start();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
