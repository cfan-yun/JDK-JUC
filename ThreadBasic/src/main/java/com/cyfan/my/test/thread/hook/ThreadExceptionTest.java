package com.cyfan.my.test.thread.hook;

public class ThreadExceptionTest {

    public static void main(String[] args) throws InterruptedException {
        AcceptThreadException acceptThreadException = new AcceptThreadException();
        /******这两句代码在只剩下DestroyVM一个用户线程时才执行***/
        //上报异常，邮件通知相关人员
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            System.out.println(">>>>>>>>>>>> notify somebody!"+acceptThreadException.getE().toString());
        }));

        //要释放资源，网络，数据库连接等资源
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            System.out.println(">>>>>>>>>>>> release some resources!");
        }));
        /******这两句代码在只剩下DestroyVM一个用户线程时才执行***/


       Thread t1 =  new Thread(()->{
            for (int i = 0; i < 2; i++) {
                System.out.println(Thread.currentThread().getName()+" is running !");
            }
            int i = 1/0; //此处抛出异常
        }, "userThread1");

        Thread t2 =  new Thread(()->{
            for (int i = 0; i < 2; i++) {
                System.out.println(Thread.currentThread().getName()+" is running !");
            }
            int i = 1/0; //此处抛出异常
        }, "userThread2");

        //获取线程的异常信息，非try-catch捕获过的异常，最好放在线程启动之前设置，否则可能会不生效
        t1.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                System.out.println(t.getName()+" is exception! message>>>>>>>"+e.getMessage());
                acceptThreadException.setT(t);
                acceptThreadException.setE(e);
            }
        });

        //默认的异常处理方法（所有线程如果没有设置异常处理逻辑，则调用该方法）,如果线程自己的setUncaughtExceptionHandler，那么用线程自己的
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                System.out.println(t.getName()+" is exception! message:"+e.getMessage());
                acceptThreadException.setT(t);
                acceptThreadException.setE(e);
            }
        });

       t1.start();
       t2.start();


    }

    /**
     * 该类用于接受线程异常信息
     */
    static class AcceptThreadException{
        private Thread t;
        private Throwable e;

        public AcceptThreadException() {

        }

        public AcceptThreadException(Thread t, Throwable e) {
            this.t = t;
            this.e = e;
        }

        public Thread getT() {
            return t;
        }

        public void setT(Thread t) {
            this.t = t;
        }

        public Throwable getE() {
            return e;
        }

        public void setE(Throwable e) {
            this.e = e;
        }
    }
}
