package com.cyfan.study.a12.mycase.activeobject;

import com.cyfan.study.a12.mycase.activeobject.task.MethodRequest;

public class ExecutorThread extends Thread {


    private final TaskQueue<MethodRequest> queue;

    public ExecutorThread(TaskQueue<MethodRequest> queue, String name) {
        super(name);
        this.queue = queue;
    }

    /**
     * 入队,普通方法，由其他线程执行，实际上的生产者是client中的PrintThread 和 CopyThread 线程
     * client -> proxy -> methodRequest -> ExecutorThread -> queue
     *
     * @param request 封装好的请求对象
     */
    public void invoke(MethodRequest request) {
        try {
            queue.put(request);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    //从队列中取出任务执行
    @Override
    public void run() {
        //不断的从队列中获取任务执行

        try {
            while (true) {
                MethodRequest request = queue.take();
                request.execute();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
