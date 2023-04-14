package com.cyfan.study.a09.mycase02;

/**
 * 模拟服务端
 */
public class MyServer {

    public Product request(int count, char c) {
        System.out.println(Thread.currentThread().getName() + ", server request start .....count = "+count +", char = "+c);

        //callable + futureTask
        FutureProduct futureProduct = new FutureProduct(()->{
            return new RealProduct(count, c);
        });
        //这里异步处理futureProduct 中的RealProduct 是和main线程同步执行的
        Thread thread = new Thread(futureProduct);
        thread.start();
        System.out.println(Thread.currentThread().getName() + ", server request end .....count = "+count +", char = "+c);
        return futureProduct;
    }
}
