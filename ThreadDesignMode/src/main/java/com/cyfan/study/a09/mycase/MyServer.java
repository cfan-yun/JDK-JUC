package com.cyfan.study.a09.mycase;

/**
 * 模拟服务端
 */
public class MyServer {

    public Product request(int count, char c) {
        System.out.println(Thread.currentThread().getName() + ", server request start .....count = "+count +", char = "+c);
        FutureProduct futureProduct = new FutureProduct();
        //这里异步处理futureProduct 中的RealProduct 是和main线程同步执行的
        new Thread(()->{
            RealProduct realProduct = new RealProduct(count,c);
            futureProduct.setRealProduct(realProduct);
        }).start();
        System.out.println(Thread.currentThread().getName() + ", server request end .....count = "+count +", char = "+c);
        return futureProduct;
    }
}
