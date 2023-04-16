package com.cyfan.study.a12.mycase.activeobject;

import com.cyfan.study.a12.mycase.activeobject.result.RealResult;
import com.cyfan.study.a12.mycase.activeobject.result.Result;

/**
 * 打印机服务对象
 */
public class PrinterService implements ActiveObject {

    //打印count个fillChar
    @Override
    public Result<String> printContent(int count, char fillChar) {
        char[] chars = new char[count];
        for (int i = 0; i < count; i++) {
            chars[i] = fillChar;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        String s = new String(chars);
        System.out.println("ExecutorThread [" + Thread.currentThread().getName() + "], --->" + s);
        return new RealResult<>(s);//返回真实的数组
    }

    @Override
    public void copyContent(String content) {
        System.out.println("ExecutorThread [" + Thread.currentThread().getName() + "], --->" + content);
        //睡眠
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
