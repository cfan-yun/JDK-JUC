package com.cyfan.my.test.jmh;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

/**
 *
 * TLAB = Thread local allocate buffer 线程本地分配缓存区
 *  它占用的是Eden区并且基本上大小只是Eden的1%
 *
 *
 * 基准测试 jmh 工具使用
 *
 * 类似Junit
 *      JMH 粒度更细
 *      做性能测试比如要精确到微妙的时候
 *
 *      执行之前每次都需要先maven 插件 clean compile一下
 *
 */
@BenchmarkMode(Mode.AverageTime) //测平均时间（一次多少时间），还是吞吐量（一秒多少次）
@OutputTimeUnit(TimeUnit.NANOSECONDS)  //输出时间什么级别
//@State(Scope.Group) //Group 线程组共享一份  thread 每个线程一份  Benchmark 所有线程共享一份
public class TlabTest {

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder().include(TlabTest.class.getSimpleName())
                .forks(1)                           //进程数，几个进程跑
                .warmupIterations(5)                //预热几次，测试时，前几次数据可能不准，所以一般要预先跑几次
                .measurementIterations(5)           //执行测试，执行5次
                .threads(4)                         //4个线程跑
                .build();
        new Runner(options).run();         //执行
    }

    @Benchmark
    @Group("group")
    public void testA(MyClass myClass){
        myClass.values[0].myValue++;
    }
    @Benchmark
    @Group("group")
    public void testB(MyClass myClass){
        myClass.values[0].myValue++;
    }

   public static class MyClassValue{
        volatile int myValue;
    }

    /**
     * 1.share组共享MyCount  --Scope.Group
     * 2.每个线程创建一个MyCount--Scope.Thread
     *
     */
    //@State(Scope.Group)
    @State(Scope.Thread)
    public static  class MyClass{
        MyClassValue[] values = new MyClassValue[2];

        public MyClass(){
            values[0] = new MyClassValue();
            values[1] = new MyClassValue();
        }
    }
}
