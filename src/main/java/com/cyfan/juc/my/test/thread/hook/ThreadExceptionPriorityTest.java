package com.cyfan.juc.my.test.thread.hook;

/**
 * 异常处理优先级测试
 *  1. Thread类中getUncaughtExceptionHandler方法中被重写，返回的UncaughtExceptionHandler优先级最高； 1
 *  2. t1.setUncaughtExceptionHandler 设置的UncaughtExceptionHandler，优先级其次； 2
 *  3. 抛出异常的线程的父线程组的uncaughtExcption，优先级其次；3
 *  4. Thread.setUncaughtExceptionHandler 设置的UncaughtExceptionHandler，优先级其次；4
 *  5. 控制台打印异常信息优先级最低。5
 *
 */
public class ThreadExceptionPriorityTest {

    public static void main(String[] args) {

        //创建一个父线程组,重写其uncaughtException方法。 其优先级为 4
        ThreadGroup myThreadGroupParent = new ThreadGroup("myThreadGroupParent"){
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                System.out.println("Priority = 4 (Override myThreadGroupParent.uncaughtException), threadName="+Thread.currentThread().getName()+",Exception:"+e.toString());
            }
        };

        //创建一个线程组，设置其父线程组为myThreadGroupParent，重写myThreadGroup的uncaughtException方法。 其优先级为 3
        ThreadGroup myThreadGroup = new ThreadGroup(myThreadGroupParent,"myThreadGroup"){
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                System.out.println("Priority = 3 (Override myThreadGroup.uncaughtException), threadName="+Thread.currentThread().getName()+",Exception:"+e.toString());
            }
        };

        //创建一个线程，其所属的线程组为myThreadGroup，线程名为myThread
        Thread myThread =  new Thread(myThreadGroup,()->{
            System.out.println(Thread.currentThread().getName() + "  is running !");
            try {
                int i = 1/0;//此段代码抛出异常,被try-Catch住了，UncaughtExceptionHandler捕获不到
            } catch (Exception e) {
                System.out.println("异常被catch住了！UncaughtExceptionHandler捕获不到!"+ e.toString());
                //e.printStackTrace();
                //throw e; //抛出异常之后，UncaughtExceptionHandler能捕获到
            }

            int[] j = new int[1];
            int value = j[2];//数字下标越界异常，UncaughtExceptionHandler能捕获到

        }, "myThread"){//重写myThread对象的getUncaughtExceptionHandler方法，此处优先级最高，最先处理异常，优先级为 1
            @Override
            public UncaughtExceptionHandler getUncaughtExceptionHandler() {
                return (t,e)->{
                    System.out.println("Priority = 1 (Override myThread.getUncaughtExceptionHandler()),  threadName="+Thread.currentThread().getName()+",Exception:"+e.toString());
                };
            }
        };

        //设置线程的异常处理逻辑，优先级为2；
        myThread.setUncaughtExceptionHandler((t,e)->{
            System.out.println("Priority = 2 (myThread.setUncaughtExceptionHandler), threadName="+Thread.currentThread().getName()+",Exception:"+e.toString());
        });

        //设置线程的异全局的异常处理逻辑，优先级为5；
        Thread.setDefaultUncaughtExceptionHandler((t,e)->{
            System.out.println("Priority = 5(static Thread.setDefaultUncaughtExceptionHandler), threadName="+Thread.currentThread().getName()+",Exception:"+e.toString());
        });

        //啥都不设置，优先级为最低 6， 控制台打印异常

        myThread.start();



    }
}
