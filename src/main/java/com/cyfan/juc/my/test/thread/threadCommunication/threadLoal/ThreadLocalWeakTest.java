package com.cyfan.juc.my.test.thread.threadCommunication.threadLoal;


import java.lang.ref.WeakReference;

/**
 *  ThreadLocal原理
 *  2.threadLocal内存泄露分析
 *      threadLocal中threadLocalMap的Entry中Key为什么是若引用
 *      目的是弱引用可以被垃圾回收！！
 *      threadLocal -> threadLocalMap -> Entry (key, value)
 *                                              key是一个弱引用
 *
 *
 */
public class ThreadLocalWeakTest {
    private  final  ThreadLocal<Integer> threadLocal = new ThreadLocal<>();

    public static void main(String[] args) {
        Person person = new Person(new Man("zhangsan"));
        Man man = new Man("lisi");// new 强引用 man -> new Man() 是强引用
        Person person1 = new Person(man);
        System.gc();//垃圾回收


        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println(person.get()); //null 被回收
        System.out.println(person1.get()); //Man{name='lisi'} 没被回收

        ThreadLocalWeakTest threadLocalWeakTest = new ThreadLocalWeakTest();
        for (int i = 0; i < 5; i++) {
            threadLocalWeakTest.threadLocal.set(i);
        }


    }
}

class Person extends WeakReference<Man>{

    public Person(Man referent) {
        super(referent);
    }
}

class Man{
    private String name;

    public Man(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return "Man{" +
                "name='" + name + '\'' +
                '}';
    }

    /**
     * 垃圾回收时会执行该方法
     * @throws Throwable 异常
     */
    @Override
    protected void finalize() throws Throwable {
        System.out.println("执行finalize:"+this);
    }
}
