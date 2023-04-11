package com.cyfan.study.a06.mycase02;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MyDataBase<K,V> {

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock(true);
    private final Lock readLock = readWriteLock.readLock();
    private final Lock writeLock = readWriteLock.writeLock();
    private final Map<K,V> map = new HashMap<K,V>();

    public V get(K key) throws InterruptedException {
        readLock.lock();
        try {
            Thread.sleep(100); //读比写快一般，所以模拟执行时间短一点
            System.out.println("get Key =>"+ key + ",value =>"+ map.get(key));
            return map.get(key);
        } finally {
            readLock.unlock();
        }
    };


    public V set(K key,V value) throws InterruptedException {
        writeLock.lock();
        try {
            Thread.sleep(500); //读比写快一般，所以模拟执行时间短一点
            System.out.println("set Key ->"+ key + ",value ->"+ value);
            return map.put(key,value);
        } finally {
            writeLock.unlock();
        }
    }




}
