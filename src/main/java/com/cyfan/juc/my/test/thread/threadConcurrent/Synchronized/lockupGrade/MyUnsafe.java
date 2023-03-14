package com.cyfan.juc.my.test.thread.threadConcurrent.Synchronized.lockupGrade;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * 获取Unsafe对象，操作内存
 */
public class MyUnsafe {

    public static Unsafe getUnsafe() {
        Field field = null;
        try {

            field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            return (Unsafe) field.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}
