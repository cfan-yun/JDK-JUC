package com.cyfan.study.a08.mycase;

/**
 * 合同文件夹
 */
public class TaskExecutor {

    private final Thread[] workerThreadQueue;
    private final MyQueue<ContractTask> contractTaskQueue;


    public TaskExecutor(int threadCount, int taskCount) throws InterruptedException {
        workerThreadQueue =  new Thread[threadCount];
        contractTaskQueue =  new MyQueue<>(taskCount);

        for (int i = 0; i < threadCount; i++) {
            Thread thread = new WorkerThread("建设"+i + "队", contractTaskQueue);//创建施工队线程
            workerThreadQueue[i] =  thread;
        }
    }


    /**
     * 启动所有线程
     */
    public void startWorks(){
        for (int i = 0; i < workerThreadQueue.length; i++) {
            workerThreadQueue[i].start();
        }
    }

    public Thread[] getWorkerThreadQueue() {
        return workerThreadQueue;
    }

    public MyQueue<ContractTask> getContractTaskQueue() {
        return contractTaskQueue;
    }
}
