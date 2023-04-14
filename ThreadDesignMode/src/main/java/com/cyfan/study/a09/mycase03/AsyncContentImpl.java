package com.cyfan.study.a09.mycase03;

/**
 * 实现异步抓取
 */
public class AsyncContentImpl implements IContent {

    private byte[] contentBytes;
    private IContent content;

    private boolean ready;

    public AsyncContentImpl() {
    }

    @Override
    public byte[] getContent() {
        synchronized (this) {
            try {
                if (!ready) {
                    wait();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return this.contentBytes;
        }

    }

    public void setContent(IContent content) {
        synchronized (this) {
            this.content = content;
            this.contentBytes = content.getContent();
            ready = true;
            //唤醒其他线程
            this.notifyAll();
        }

    }
}
