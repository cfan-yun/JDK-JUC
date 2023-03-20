package com.cyfan.juc.my.test.thread.threadCommunication.threadLoal;


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
 *          此例为场景1
 *
 */
public class ThreadLocalTest {

    private final String flag = "0";

    private  final  ThreadLocal<String> flagThreadLocal =  new ThreadLocal<>();
    public static void main(String[] args) {
        ThreadLocalTest threadLocalTest = new ThreadLocalTest();
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            new Thread(()->{
                threadLocalTest.test1();
                threadLocalTest.test2(finalI);
                threadLocalTest.test3();
            }).start();
        }

    }

    public void test1(){
        flagThreadLocal.set(flag);
        System.out.println(Thread.currentThread().getName()+",test1, flagThreadLocal = "+flagThreadLocal.get());
    }

    public void test2(int id ){
        if(id == 0){
            flagThreadLocal.set("00");
        }else if (id == 1){
            flagThreadLocal.set("11");
        }else{
            flagThreadLocal.set("xx");
        }
    }

    public void test3(){
        System.out.println(Thread.currentThread().getName()+",test3, flagThreadLocal = "+flagThreadLocal.get());
    }
}
