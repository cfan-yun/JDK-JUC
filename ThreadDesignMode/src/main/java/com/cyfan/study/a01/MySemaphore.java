package com.cyfan.study.a01;

public class MySemaphore {

    private  int count;

    public MySemaphore(int count){
        this.count = count;
    }

    public void acquire() throws InterruptedException {
        synchronized (this){
            while (true){
                if (this.count >0){
                    this.count--;
                    break;
                }
                this.wait();

                //唤醒之后继续判断是否有锁可拿，如果有，则继续唤醒后继节点
                if (this.count > 0) {//这一段可有可无,aqs源码实现是两处唤醒,分叉执行
                    this.notifyAll();
                }
            }
        }
    }

    public void  release(){
        synchronized (this){
            this.count++;
            this.notifyAll();
        }
    }
}
