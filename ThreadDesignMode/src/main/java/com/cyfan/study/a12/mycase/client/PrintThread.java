package com.cyfan.study.a12.mycase.client;

import com.cyfan.study.a12.mycase.activeobject.ActiveObject;
import com.cyfan.study.a12.mycase.activeobject.result.Result;

/**
 * 打印机打印线程
 */
public class PrintThread extends Thread {

    private final ActiveObject proxy;
    private final char fillChar;

    public PrintThread(String name, ActiveObject proxy) {
        super(name);
        this.proxy = proxy;
        this.fillChar = name.charAt(0); //打印名称的第一个字母

    }

    @Override
    public void run() {
        try {
            for (int i = 0; true; i++) {
                Result<String> result = proxy.printContent(i, fillChar);//第几次打印字符char 通过Executor Thread执行run方法获取到真实的返回值
                Thread.sleep(10);//睡10毫秒之后
                Object value = result.getResultValue();
                System.out.println(Thread.currentThread().getName() + "-->打印内容：" + i + "个" + fillChar + " [" + value.toString() + "]");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
