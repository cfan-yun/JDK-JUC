package com.cyfan.my.test.threadPool.task;

import java.util.Arrays;
import java.util.concurrent.Callable;

public class MyFutureTask implements Callable<Object> {


    int num = 0;

    public MyFutureTask(int num) {
        this.num = num;
    }
    @Override
    public Object call() throws Exception {

        System.out.println(Thread.currentThread().getName() +"——>"+ num +"号线程, start.....繁忙");
        try {
            //模拟从数据库获取数据返回
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() +"——>"+ num +"号线程 end.....空闲");
        return Arrays.asList(num+"号","mikel","jo","son");
    }
}
