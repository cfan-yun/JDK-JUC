package com.cyfan.my.test.thread;

public class DataTransport {


    //main线程是由虚拟机在C++层面创建的
    public static void main(String[] args) throws InterruptedException {

        //单线程执行数据迁移
        dataTransportSingle();
        System.out.println("====================================");
        //多线程执行数据迁移
        dataTransportMore();
      // Thread.sleep(Integer.MAX_VALUE);

    }


    /**
     * 单线程执行数据迁移
     * @throws InterruptedException
     */
    public static  void dataTransportSingle() throws InterruptedException {
        //单线程串行执行，需要修改为生产消费者模型，由多线程执行，增加速度，单线程DB1 -> CSV 必须完成之后，才能执行CSV -> DB2，效率比较低
        //从数据库1-> CSV文件
        writeCSVFromDB1();
        //从CSV文件到数据库2
        writeDB2FromCSV();
    }


    /**
     * 多线程执行数据迁移
     */
    private static void dataTransportMore() {
        WriteCSVThread writeCSVThread = new WriteCSVThread();
        writeCSVThread.setName("write-csv");
        writeCSVThread.start();

        WriteDBThread writeDBThread = new WriteDBThread();
        writeDBThread.setName("write-db");
        writeDBThread.start();


    }

    static class  WriteCSVThread extends Thread{
        @Override
        public void run() {
            try {
                System.out.println(">>>>>>>>>>>>>>>>>thread："+Thread.currentThread().getName()+ " start.....");
                DataTransport.writeCSVFromDB1();
                System.out.println(">>>>>>>>>>>>>>>>>thread："+Thread.currentThread().getName()+ " end.....");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class WriteDBThread extends  Thread{
        @Override
        public void run() {
            try {
                System.out.println(">>>>>>>>>>>>>>>>>thread："+Thread.currentThread().getName()+ " start.....");
                DataTransport.writeDB2FromCSV();
                System.out.println(">>>>>>>>>>>>>>>>>thread："+Thread.currentThread().getName()+ " end.....");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }




    private static void writeCSVFromDB1() throws InterruptedException {
        System.out.println(">>>>>>>>>>>>>>开始执行 writeCSVFromDB1>>>>>>>>>>>>>");
        Thread.sleep(1000);
        System.out.println(">>>>>>>>>>>>>>writeCSVFromDB1 执行结束 DB1 -> CSV文件 完成 >>>>>>>>>>>>>");
    }

    private static void writeDB2FromCSV() throws InterruptedException {
        System.out.println(">>>>>>>>>>>>>>开始执行 writeDB2FromCSV>>>>>>>>>>>>>");
        Thread.sleep(2000);
        System.out.println(">>>>>>>>>>>>>>writeDB2FromCSV 执行结束 CSV -> DB2 完成 >>>>>>>>>>>>>");
    }


}
