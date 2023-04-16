package com.cyfan.study.a12.mycase.activeobject.task;

import com.cyfan.study.a12.mycase.activeobject.PrinterService;
import com.cyfan.study.a12.mycase.activeobject.result.FutureResult;

public abstract class MethodRequest {
    protected PrinterService printerService;
    protected FutureResult<String> futureResult;

    public MethodRequest(PrinterService printerService, FutureResult<String> futureResult) {
        this.printerService = printerService;
        this.futureResult = futureResult;
    }

    //真正执行处理请求的方法，并返回真正的处理结果
    public abstract void execute();

}
