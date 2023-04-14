package com.cyfan.study.a09.mycase;

/**
 * 未来的数据-提货单
 */
public class FutureProduct implements Product {

    private boolean ready = false;

    private RealProduct realProduct;

    @Override
    public String getContent() throws InterruptedException {
        synchronized (this){
            if (!ready){//数据未准备好，那么先挂起
                System.out.println(Thread.currentThread().getName() + ", 卡住了，等等");
                this.wait(); //GuardedSus
            }
            return realProduct.getContent();
        }
    }

    public RealProduct getRealProduct() {
        return realProduct;
    }

    public void setRealProduct(RealProduct realProduct) {
        synchronized (this){//数据准备好了唤醒线程
            if (ready){
                return; //Balking
            }
            this.realProduct = realProduct;
            this.ready = true;
            System.out.println(Thread.currentThread().getName() + ", 唤醒了其他等待线程");
            this.notifyAll();
        }

    }
}
