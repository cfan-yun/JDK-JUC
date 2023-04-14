package com.cyfan.study.a09.mycase02;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 未来的数据-提货单
 */
public class FutureProduct extends FutureTask implements Product {


    public FutureProduct(Callable callable) {
        super(callable);//这里set了RealProduct
    }

    public RealProduct getRealProduct() {
        Object o = null;
        try {
            o = get();//获取值
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        return (RealProduct) o;
    }

    @Override
    public String getContent() throws InterruptedException {
        return getRealProduct().getContent();
    }
}
