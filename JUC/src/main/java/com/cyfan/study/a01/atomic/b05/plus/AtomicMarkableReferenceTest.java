package com.cyfan.study.a01.atomic.b05.plus;

import java.util.concurrent.atomic.AtomicMarkableReference;

public class AtomicMarkableReferenceTest {
    

    public static void main(String[] args) {
        Customer customer1 = new Customer("1", "zhang_san");
        Customer customer2 = new Customer("2", "li_si");
        Customer customer3 = new Customer("2", "wang_er");
        testResoleABA(customer1, customer2, customer3);
    }


    private static void testResoleABA(Customer customer1, Customer customer2, Customer customer3) {
        AtomicMarkableReference<Customer> myAtomicMarkable = new AtomicMarkableReference<>(customer1, true);
        new Thread(() ->{
            try {
                boolean marked = myAtomicMarkable.isMarked();
                System.out.println(Thread.currentThread().getName() +", marked = " + marked);

                Thread.sleep(1000);//让t1先跑完完成ABA
                boolean b = myAtomicMarkable.compareAndSet(customer1, customer2,marked, !marked );
                marked =  myAtomicMarkable.isMarked();
                System.out.println(Thread.currentThread().getName() + ",result1 = " + b + ", marked = "  + marked);
                b = myAtomicMarkable.compareAndSet(customer2, customer1, marked, !marked);
                marked =  myAtomicMarkable.isMarked();
                System.out.println(Thread.currentThread().getName() + ",result2= " + b + ", marked = "  + marked);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, "t1").start();

        new Thread(() ->{
            try {
                boolean marked = myAtomicMarkable.isMarked();
                System.out.println(Thread.currentThread().getName() +", marked = " + marked);

                Thread.sleep(2000);//让t1先跑完完成ABA

                boolean b = myAtomicMarkable.compareAndSet(customer1, customer3, marked, !marked);
                marked =  myAtomicMarkable.isMarked();
                System.out.println(Thread.currentThread().getName() + ",result3 = " + b + ", value = " + myAtomicMarkable.getReference() + ", marked = " + marked);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, "t2").start();

    }
}
