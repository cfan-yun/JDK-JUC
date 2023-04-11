package com.cyfan.study.a02;

/**
 * 模式1：Immutable 模式 共享资源只读不写
 *  核心代码： private final Object obj:
 *            //构造函数初始化/或其他位置初始化 obj =  new Object();
 *
 * 1.特点：
 *        不可变模式，重点：没有写操作（或者在初始化时，写一次），只有读操作。成员变量使用private final 修饰。
 * Immutable模式中存在着确保实例状态不发生改变的类，immutable类。
 * 在访问这些实例时，并不需要执行耗时的互斥处理，因此若能巧妙的利用该模式，定能提高程序效率。
 *
 *2.JDK哪些类使用这个设计模式
 *  String类Immutable ,StringBuffer类mutable
 *  CopyAndWriteArrayList类Immutable，ArrayList类mutable。
 *      CopyAndWriteArrayList可变部分拷贝出来
 *
 */
public class Main {

}
