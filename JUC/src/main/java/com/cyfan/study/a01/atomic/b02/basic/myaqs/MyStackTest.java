package com.cyfan.study.a01.atomic.b02.basic.myaqs;

public class MyStackTest {

    //两个线程入栈，一个线程出栈
    //最终结果  栈中元素个数 = "t3,栈顶为空！" 打印的个数
    //"t3,栈顶为空！" + 出栈个数 =  总入栈个数
    //入栈个数 = 60
    //保证打印不乱序只能加上synchronized ，但是注释掉synchronized关键字，出入栈是没问题的
    public static void main(String[] args) {
        MyStack myStack = new MyStack();
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 30; i++) {
                MyStack.Node node = new MyStack.Node(i);
                myStack.push(node);
            }
        }, "t1");
        Thread t2 = new Thread(() -> {
            for (int i = 30; i < 60; i++) {
                MyStack.Node node = new MyStack.Node(i);
                myStack.push(node);
            }
        }, "t2");
        Thread t3 = new Thread(() -> {
            for (int i = 0; i < 60; i++) {
                myStack.pop();
            }
        }, "t3");

        t1.start();
        t2.start();
        t3.start();

        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("打印栈最终信息：");
        myStack.printStack();
    }
}
