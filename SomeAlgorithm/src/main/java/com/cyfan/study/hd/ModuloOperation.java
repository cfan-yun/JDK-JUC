package com.cyfan.study.hd;

/**
 * 取模运算的两种方式
 *
 * * 1. num % mod
 * *       5 % 4 = 1
 * * 2. num & (mod -1)
 * *       5 0101
 * *       4 0100
 * *   (4-1) 0011
 * *   (5&3) 0001
 */
public class ModuloOperation {

    private static int COUNT =  10000;
    private static int MOD =  4;
    public static void main(String[] args) {
        //使用常规数学计算
        testGeneral();
        //使用逻辑位运算
        testByte();


    }

    private static void testByte() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < COUNT; i++) {
           int result =  i & (MOD-1);
           System.out.print(result + ",");
        }
        System.out.println();
        System.out.println(System.currentTimeMillis() - start);
    }

    private static void testGeneral() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < COUNT; i++) {
            int result = i % MOD;
            System.out.print(result + ",");

        }
        System.out.println();
        System.out.println(System.currentTimeMillis() - start);
    }
}
