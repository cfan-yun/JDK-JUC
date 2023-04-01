package com.cyfan.my.test.threadPool;

import com.cyfan.my.test.threadPool.task.MyTask;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 * 线程池提交是使用的execute()方法，但其内部线程任务的处理是相当复杂的，
 * 涉及ThreadPoolExecutor、Worker、Thread三个类的5-6个方法
 * 1.ThreadPoolExecutor类比
 *  -execute/submit方法
 *      1.状态和线程数量用32位二进制数表示，高3位为线程状态，低29位为线程数量
 *  -addWorker主要就是创建线程和任务，然后执行
 *      线程和任务是捆绑在一起的（worker用来表示线程和任务的执行体）
 *      做两件事情：1.线程数+1；2.worker加入HashSet,并启动线程
 * 2.Worker类
 *   -runWorker()
 *      线程池真正执行任务的入口,worker对象实现了Runnable接口
 *   -getTask()
 *      从队列中获取一个任务执行
 *   -processWorkerExit
 *      如果任务队列不为空，从haseSet中移除一个线程，然后增加一个线程
 *      结论：调用shutDown并不会减少线程，直到队列为空
 *  其他方法：
 *      -shutDown  调用了shutdown方法，线程池就处于SHUTDOWN状态，此时线程池不会接受新的任务，他会等待所有任务执行完成（包括队列里面的任务）
 *      -shutDownNow 用了shutdownNow方法，线程池就处于STOP状态，此时线程池不会接受新的任务，并且会中断正在执行的任务，并且会清空队列，并返回剩余的任务
 *
 *
 * 可扩展点：
 *  经验：protected 修饰并且是空实现，通常就是扩展点
 *  terminated     此案成池关闭后执行
 *  afterExecute   每一个任务执行完之后来执行这个方法
 *  beforeExecute  任务执行完之后前来执行这个方法
 *
 *  需求：在多个任务执行完成的时候，可以暂停线程池（比如暂停线程池），暂停后必须可以支持恢复线程池，
 *  在每个任务执行后需要获取执行结果，
 *  在最后所有任务执行完成之后，必须通知管理员
 *
 *
 * 面试题；如何合理的估算线程池的大小
 *   1000个并发请求，10台机器，每台机器4核，请设计线程池（核心线程，最大线程数，任务队列容量）
 *   首先看当前系统是CPU密集型，还是IO密集型。
 *   cpu密集型：
 *      尽量减少上下文切换（书本上答案N+1 = 4+1 = 5）队列容量是100个
 *   IO密集型：
 *      cpu会有空闲，可增加线程数处理（2N+1 = 2*4 + 1 = 9）, 队列容量 1000
 *
 *   面试可以说：
 *   实际情况：设置完核心线程数，最大线程，任务队列之后，一样需要进行系统压测，根据压测结果得到一个合理的值或范围。
 *      根据系统流量状况做出调整，那么调整的前提是必须针对线程池进行监控
 *  总结
 *   1.cpu密集型
 *   2.IO密集型
 *   3.据压测结果得到一个合理的值或范围
 *   4.支持线程池的动态配置
 *      难点：动态配置：核心线程数，最大线程数，队列容量
 *      核心线程数，最大线程数，都可以设置，队列容量是不能动态修改的。
 *      ？？？？？怎么修改队列容量？？？？？
 *
 *
 *   支持的最大任务是多少？？
 *   cup密集：qps = 100+5 =  105
 *   io密集型：qps = 100+9 = 109
 *   常见中间件，比如tomcat里面最大任务
 *      默认Bio通道：
 *          最大线程数：  <attribute name="maxThreads" required="false">  200
 *          队列长度：  <attribute name="acceptCount" required="false">  100
 *          qps = 200+100 =  300
 *
 */
public class ThreadPoolSourceRead {

    public static void main(String[] args) {


        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(3, 10,
                10, TimeUnit.SECONDS, new LinkedBlockingQueue<>(2));
        for (int i = 0; i < 15; i++) {
            MyTask myTask = new MyTask(i);
            threadPoolExecutor.execute(myTask);
        }

    }
}
