package com.cyfan.study.a01.atomic.b01.unsafe.object;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class CasObject {

    protected volatile int number; // cas操作其自增



    public int increment(int x , Unsafe unsafe){
        long offsetNumber = 0;
        try {
            do {
                Field numberField = CasObject.class.getDeclaredField("number");//反射获取字段
                offsetNumber = unsafe.objectFieldOffset(numberField);   //获取字段相对CasObject的偏移量
            }while (!unsafe.compareAndSwapInt(this, offsetNumber, x-1, x));//cas没修改成功继续循环执行
            return unsafe.getInt(this, offsetNumber); //获取当前对象中的number字段值，并进行返回
            //return this.number;
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

    }

}
