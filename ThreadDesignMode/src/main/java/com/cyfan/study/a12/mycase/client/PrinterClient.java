package com.cyfan.study.a12.mycase.client;

import com.cyfan.study.a12.mycase.activeobject.ExecutorThread;
import com.cyfan.study.a12.mycase.activeobject.PrinterService;
import com.cyfan.study.a12.mycase.activeobject.Proxy;
import com.cyfan.study.a12.mycase.activeobject.TaskQueue;
import com.cyfan.study.a12.mycase.activeobject.task.MethodRequest;

public class PrinterClient {

    private final Proxy proxy;
    public PrinterClient() {
        TaskQueue<MethodRequest> queue = new TaskQueue<>();
        PrinterService printerService = new PrinterService();
        ExecutorThread executorThread1 = new ExecutorThread(queue, "ExecuteThread1");
        ExecutorThread executorThread2 = new ExecutorThread(queue, "ExecuteThread1");
        executorThread1.start();//需要先把消费者线程启动起来，取队列中获取任务执行
        executorThread2.start();
        this.proxy = new Proxy(printerService, queue);
    }

    public void print(String content){
        new PrintThread(content,proxy).start();//发送一个打印请求
    }



    public void  copy(String content){
        new CopyThread(content, proxy).start();
    }

}
