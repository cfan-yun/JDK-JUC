package com.cyfan.study.a06.mycase;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 共享数据、公共资源
 */
public class TextData {

    private final char[] buffer;

    private final MyAbstractReadWriteLock myReadWriteLock = new MyReadWriteLock();
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock(true);//公平读写锁
    private final Lock readLock = readWriteLock.readLock();
    private final Lock writeLock = readWriteLock.writeLock();

    public TextData(int size) {
        this.buffer = new char[size];
        for (int i = 0; i < size; i++) {
            this.buffer[i] = '*';
        }
    }

    //直接在读方法上使用synchronized 关键字的话，读读并发也被控制住了，只能一个线程读
    public char[] read() throws InterruptedException {
        //加读锁
        myReadWriteLock.readLock(); //before
        try {
            char[] chars = doRead();
            return chars;
        } finally {
            //读锁解锁
            myReadWriteLock.readUnLock(); //after

        }
    }

    private char[] doRead() {
        char[] newChars = new char[buffer.length];
        for (int i = 0; i < buffer.length; i++) {
            newChars[i] = buffer[i];
        }
        return newChars;
    }

    public void write(char newChar) throws InterruptedException {
        //加写锁
        myReadWriteLock.writeLock();
        try {
            doWrite(newChar);
        } finally {
            //写锁解锁
            myReadWriteLock.writeUnLock();
        }


    }

    private void doWrite(char newChar) {
        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = newChar;
        }

        //睡眠
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
