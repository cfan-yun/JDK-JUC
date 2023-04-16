package com.cyfan.study.a12.mycase.activeobject.task;

import com.cyfan.study.a12.mycase.activeobject.PrinterService;
import com.cyfan.study.a12.mycase.activeobject.result.FutureResult;
import com.cyfan.study.a12.mycase.activeobject.result.Result;

public class PrintRequest extends MethodRequest {

    private final int count;
    private final char fillChar;

    public PrintRequest(PrinterService printerService, FutureResult<String> futureResult, int count, char fillChar) {
        super(printerService, futureResult);
        this.count = count;
        this.fillChar = fillChar;
    }

    @Override
    public void execute() {
        Result<String> result = printerService.printContent(count, fillChar);
        futureResult.setResult(result);//设置真实的返回结果
    }
}
