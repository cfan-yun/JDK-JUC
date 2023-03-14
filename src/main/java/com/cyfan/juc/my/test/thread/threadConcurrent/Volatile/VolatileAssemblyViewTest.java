package com.cyfan.juc.my.test.thread.threadConcurrent.Volatile;

/**
 * 查看volatile汇编指令层面查看
 *
 * 需要几个JVM参数
 *
 * --HSDIS工具使用
 * hotspot 使用内嵌jit编译，jit又分c1编译器和c2编译器
 *       c1--client
 *       c2--server  尽最大的可能进行编译优化
 *  冒号后面“+”代表启用，“-”代表禁用
 * -server                              //使用c2编译器优化编译
 * -XX:+UnlockDiagnosticVMOptions       //解锁诊断功能，打印汇编代码，会认为你需要做诊断
 * -XX:+PrintAssembly                   //打印汇编代码
 * -XX:-Inline                          //内联
 * -XX:-TieredCompilation               //只开启c2,禁用中间编译层(123层)
 * -XX:+TieredCompilation  -XX:TieredStopAtLevel=1  //只开启c1
 *
 * --jitwitch工具使用
 * -XX:+LogCompilation
 * -XX:LogFile=$ProjectFileDir$/assemblyLog/jit.log //生成文件位置
 *
 * 结论：字段上面如果加上了volatile修饰，那么底层的汇编代码会加上lock前缀指令。lock指令的作用是锁cacheline，刷storebuffer,刷cache
 *
 * 深度解析volatile 的两大重要特性
 * 1、可见性
 *      每次读volatile变量时，读到的总是最新值（即所有cpu，最后一次写入操作），就是任何一个线程的最新写入
 * 2.禁止编译、运行时指令重排
 *      维护happens-before：
 *          对volatile变量的写入不能重排到写入的操作之前，从而保证别的线程能够看到最新的写入的值（保证写入时，真正的从storebuffer 写入到了cache）
 *          对volatile变量的读操作，不能排到后续的读操作之后（保证读时，invalidate queue 中的消息处理完了）
 *   禁止重排并不时禁止所有的重排，只有volatile写入不能向前排，读取不能向后排。除了这两种以为，其他重排是可以允许的。
 *
 *
 *
 */
public class VolatileAssemblyViewTest {
    private static int cyan1;//加了volatile后相当于加了 lock前缀指令 lock addl $0x0,(%rsp)
    private volatile static int cyan2;
    private static int cyan3;
    private volatile static int cyan4;
    private static int cyan5;
    private volatile static int cyan6;

    public static void main(String[] args) {
        for (int i = 0; i < 1000; i++) {
            increment(i);
        }

    }

    private static void increment(int i) {
        cyan1 = i + 1;
        cyan2 = i + 2;
        cyan3 = i + 3;

        long start = System.nanoTime();
        long end;
        do {
            end = System.nanoTime();
        } while (start + 1000000 >= end);

        cyan4 = i + 4;
        cyan5 = i + 5;
        cyan6 = i + 6;

    }


    private static void mySleep(long time) {
        long start = System.nanoTime();
        long end;
        do {
            end = System.nanoTime();
        } while (start + time >= end);
    }

}
