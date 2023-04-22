package com.cyfan.study.a01.atomic.b02.basic;

import java.util.concurrent.atomic.AtomicReference;

public class AtomicReferenceTest {

    private static  volatile  MemberShip memberShip =  new MemberShip("1", "zhang_san", 50);

    private  static  volatile AtomicReference<MemberShip> memberShipAtomicReference =
            new AtomicReference<>(new MemberShip("1", "zhang_san", 50));

    public static void main(String[] args) {
//        testNonThreadSafe();
        testThreadSafe();

    }

    private static void testNonThreadSafe() {
        for (int i = 0; i < 20; i++) {
            new Thread(()->{
                try {
//                    synchronized (AtomicReferenceTest.class){
                        MemberShip memberShipTemp= memberShip;
                        MemberShip memberShipNew = new MemberShip(memberShipTemp.getId(), memberShipTemp.getName(), memberShipTemp.getPoint()+ 100);
                        System.out.println("memberShipNew = " + memberShipNew);
                        memberShip = memberShipNew;
                        Thread.sleep(500);
//                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }
    }

    private static void testThreadSafe() {
        for (int i = 0; i < 20; i++) {
            new Thread(()->{
                try {
                    //while (true){
                        MemberShip memberShipTemp= memberShipAtomicReference.get();
                        MemberShip memberShipNew = new MemberShip(memberShipTemp.getId(), memberShipTemp.getName(), memberShipTemp.getPoint()+ 100);
                        //synchronized (AtomicReferenceTest.class){ //解决打印错乱
                            if (memberShipAtomicReference.compareAndSet(memberShipTemp, memberShipNew)){// 外层不使用while(true) 的话，有可能修改失败，打印记录就不满20条了
                                System.out.println(memberShipAtomicReference.get());//此处打印是乱的是正常的 因为和if中的语句执行是非原子性的
                                //break;//跳出循环
                            }
                        //}

                        Thread.sleep(500);
                    //}

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }
    }
}
