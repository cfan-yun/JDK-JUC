package com.cyfan.study.a12.mycase.activeobject.result;

public class FutureResult<T> implements Result<T> {
    private Result<T> result;//真实数据
    private boolean ready = false;//判断数据是否准备好了

    @Override
    public synchronized T getResultValue() {

        //真实数据是否准备好了，没准备好就挂起
        while (!ready) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        //返回真实数据
        return this.result.getResultValue();

    }


    public synchronized void setResult(Result<T> result) {
        this.result = result;
        this.ready = true;//设置真实数据准备好了
        notifyAll();//唤醒挂起的线程读取真实数据
    }
}
