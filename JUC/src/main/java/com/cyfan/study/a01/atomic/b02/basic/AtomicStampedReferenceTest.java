package com.cyfan.study.a01.atomic.b02.basic;


import java.util.concurrent.atomic.AtomicStampedReference;

public class AtomicStampedReferenceTest {
    

    public static void main(String[] args) {
        Customer customer1 = new Customer("1", "zhang_san");
        Customer customer2 = new Customer("2", "li_si");
        Customer customer3 = new Customer("2", "wang_er");
        testResoleABA(customer1, customer2, customer3);
    }

    private static void testResoleABA(Customer customer1, Customer customer2, Customer customer3) {
        AtomicStampedReference<Customer> myAtomicVersion = new AtomicStampedReference<>(customer1, 0);
        new Thread(() ->{
            try {
                int version = myAtomicVersion.getStamp();
                System.out.println(Thread.currentThread().getName() +", version = " + version);

                Thread.sleep(1000);//让t1先跑完完成ABA
                boolean b = myAtomicVersion.compareAndSet(customer1, customer2,version, version + 1 );
                version =  myAtomicVersion.getStamp();
                System.out.println(Thread.currentThread().getName() + ",result1 = " + b + ", version = "  + version);
                b = myAtomicVersion.compareAndSet(customer2, customer1, version, version + 1);
                version =  myAtomicVersion.getStamp();
                System.out.println(Thread.currentThread().getName() + ",result2= " + b + ", version = "  + version);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, "t1").start();

        new Thread(() ->{
            try {
                int version = myAtomicVersion.getStamp();
                System.out.println(Thread.currentThread().getName() +", version = " + version);

                Thread.sleep(2000);//让t1先跑完完成ABA

                boolean b = myAtomicVersion.compareAndSet(customer1, customer3, version, version + 1);
                version =  myAtomicVersion.getStamp();
                System.out.println(Thread.currentThread().getName() + ",result3 = " + b + ", value = " + myAtomicVersion.getReference() + ", version = " + version);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, "t2").start();

    }
}
