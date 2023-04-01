package com.cyfan.my.test.thread.threadCommunication.threadLoal;


import java.util.HashMap;

/**
 *  ThreadLocal原理
 *  1.使用场景
 *      1)共享变量
 *      2)在某些方法中里，计算的中间结果需要共享给其他方法
 *          String m1(){
 *              String ss = m2();//此变量未被其他地方使用
 *              //很多逻辑
 *              return ss;
 *          }
 *
 *          String m3(){
 *              String s3 =  m1(); //其实ss值在最开始就计算好了，m1不返回也可以，此时就可以用ThreadLocal将ss值存起来。
 *          }
 *
 *  此例为场景2
 *
 */
public class ThreadLocalTest1 {

    private final ThreadLocal<String> threadLocal =  new ThreadLocal<>();
    public static void main(String[] args) {
        ThreadLocalTest1 threadLocalTest = new ThreadLocalTest1();
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            new Thread(()->{
                threadLocalTest.test1(finalI);
                threadLocalTest.test2();
                threadLocalTest.test3();
            }).start();
        }

    }

    public void test1(int id){
        String data = getDataFromOracle(id);
        threadLocal.set(data);
    }

    private String getDataFromOracle(int id) {
        HashMap<Object, Object> map = new HashMap<>();
        map.put(1, "apple");
        map.put(2, "banana");
        map.put(3,"orange");
        return (String) map.get(id) ;

    }

    public void test2(){
        String value = threadLocal.get();
        System.out.println(Thread.currentThread().getName()+",test3, threadLocal Value  = "+threadLocal.get());
        String mysqlValue = getDataFromMysql(value);
        threadLocal.set(mysqlValue);
    }

    private String getDataFromMysql(String key) {
        HashMap<Object, Object> map = new HashMap<>();
        map.put("orange", "橘子");
        map.put("apple", "苹果");
        map.put("banana", "香蕉");
        return (String)map.get(key);
    }

    public void test3(){
        System.out.println(Thread.currentThread().getName()+",test3, threadLocal Value  = "+threadLocal.get());
    }
}
