package com.cyfan.study.a09.mycase;

import java.util.Arrays;

/**
 * 返回真实数据
 */
public class RealProduct implements Product {

    private final String content;

    public RealProduct(int count, char c) {
        System.out.println("RealProduct start--->count = "+count +", char = "+c);
        char[] chars = new char[count];
        Arrays.fill(chars, c);//字符数组赋值
        //模拟业务逻辑执行
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        this.content = new String(chars);
        System.out.println("RealProduct end--->count = "+count +", char = "+c);
    }

    @Override
    public String getContent() {
        return this.content;
    }
}
