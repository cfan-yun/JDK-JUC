package com.cyfan.study.a12.mycase.activeobject.task;

import com.cyfan.study.a12.mycase.activeobject.PrinterService;

public class CopyRequest extends  MethodRequest{

    private  final String content;

    public CopyRequest(PrinterService printerService, String content) {
        super(printerService, null);
        this.content = content;
    }

    @Override
    public void execute() {
        printerService.copyContent(content);
    }
}
