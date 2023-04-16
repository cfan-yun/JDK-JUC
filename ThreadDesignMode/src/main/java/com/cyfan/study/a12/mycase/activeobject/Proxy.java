package com.cyfan.study.a12.mycase.activeobject;

import com.cyfan.study.a12.mycase.activeobject.result.FutureResult;
import com.cyfan.study.a12.mycase.activeobject.result.Result;
import com.cyfan.study.a12.mycase.activeobject.task.CopyRequest;
import com.cyfan.study.a12.mycase.activeobject.task.MethodRequest;
import com.cyfan.study.a12.mycase.activeobject.task.PrintRequest;

public class Proxy implements ActiveObject{

    private final PrinterService printerService;
//    private final ExecutorThread executorThread;
    private final TaskQueue<MethodRequest> queue;

    public Proxy(PrinterService printerService, TaskQueue<MethodRequest> queue){
        this.printerService = printerService;
        this.queue = queue;
    }

    @Override
    public Result printContent(int count, char fillChar) {
        FutureResult<String> futureResult = new FutureResult<>();
        PrintRequest printRequest = new PrintRequest(printerService, futureResult, count, fillChar);
        //入队
//        executorThread.invoke(printRequest);
        try {
            queue.put(printRequest);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //返回结果
        return  futureResult;
    }

    @Override
    public void copyContent(String content) {

        CopyRequest copyRequest = new CopyRequest(printerService,content);
        //入队
//        executorThread.invoke(copyRequest);

        try {
            queue.put(copyRequest);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
