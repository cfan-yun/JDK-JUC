package com.cyfan.study.a01.atomic.b02.basic;


public class MyAtomicVersionTest {


    public static void main(String[] args) {
        Customer customer1 = new Customer("1", "zhangsan");
        Customer customer2 = new Customer("2", "lisi");
        Customer customer3 = new Customer("2", "wanger");
        //忽略版本号
        MyAtomicVersion<Customer> myAtomicVersion = new MyAtomicVersion<>(customer1);
        //testNoVersion(customer1, customer2, customer3, myAtomicVersion);

        //解决通用型ABA问题
        //testABA(customer1, customer2, customer3, myAtomicVersion);
        testResoleABA(customer1, customer2, customer3);
    }

    private static void testNoVersion(Customer customer1, Customer customer2, Customer customer3, MyAtomicVersion<Customer> myAtomicVersion) {
        boolean b1 = myAtomicVersion.compareAndSet(customer1, customer2);
        Customer value1 = myAtomicVersion.getValue();
        System.out.println("b1 = " + b1 + ", "+"valu1e = " + value1);


        boolean b2 = myAtomicVersion.compareAndSet(customer1, customer3);
        Customer value2 = myAtomicVersion.getValue();
        System.out.println("b2= " + b2 + ", "+"value2 = " + value2);
    }

    private static void testABA(Customer customer1, Customer customer2, Customer customer3, MyAtomicVersion<Customer> myAtomicVersion) {
        new Thread(() ->{
            boolean b = myAtomicVersion.compareAndSet(customer1, customer2);
            System.out.println(Thread.currentThread().getName() + ",result1 = " + b);
            b = myAtomicVersion.compareAndSet(customer2, customer1);
            System.out.println(Thread.currentThread().getName() + ",result2 = " + b);
        }, "t1").start();

        new Thread(() ->{
            try {
                Thread.sleep(1000);//让t1先跑完完成ABA
                boolean b = myAtomicVersion.compareAndSet(customer1, customer3);
                System.out.println(Thread.currentThread().getName() + ",result3 = " + b + ", value = " + myAtomicVersion.getValue());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, "t2").start();
    }

    private static void testResoleABA(Customer customer1, Customer customer2, Customer customer3) {
        MyAtomicVersion<Customer> myAtomicVersion = new MyAtomicVersion<Customer>(customer1, 0);
        new Thread(() ->{
            try {
                int version = myAtomicVersion.getVersion();
                System.out.println(Thread.currentThread().getName() +", version = " + version);

                Thread.sleep(1000);//让t1先跑完完成ABA
                boolean b = myAtomicVersion.compareAndSet(customer1, customer2,version, version + 1 );
                version =  myAtomicVersion.getVersion();
                System.out.println(Thread.currentThread().getName() + ",result1 = " + b + ", version = "  + version);
                b = myAtomicVersion.compareAndSet(customer2, customer1, version, version + 1);
                version =  myAtomicVersion.getVersion();
                System.out.println(Thread.currentThread().getName() + ",result2= " + b + ", version = "  + version);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, "t1").start();

        new Thread(() ->{
            try {
                int version = myAtomicVersion.getVersion();
                System.out.println(Thread.currentThread().getName() +", version = " + version);

                Thread.sleep(2000);//让t1先跑完完成ABA

                boolean b = myAtomicVersion.compareAndSet(customer1, customer3, version, version + 1);
                version =  myAtomicVersion.getVersion();
                System.out.println(Thread.currentThread().getName() + ",result3 = " + b + ", value = " + myAtomicVersion.getValue() + ", version = " + version);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, "t2").start();

    }
}
