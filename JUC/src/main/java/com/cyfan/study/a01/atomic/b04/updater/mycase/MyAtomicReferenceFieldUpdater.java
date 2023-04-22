package com.cyfan.study.a01.atomic.b04.updater.mycase;

import com.cyfan.study.utils.UnsafeUtils;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * 使得修改对象的属性时，属性线程安全
 * @param <T> 需要修改属性的对象
 */
public class MyAtomicReferenceFieldUpdater<T> {

    private static final Unsafe unsafe = UnsafeUtils.getUnsafe();

    private final  long offset;

    private final Class<T> tClass;


    public MyAtomicReferenceFieldUpdater(Class<T> tClass, String fieldName) {
        try {
            this.tClass = tClass;
            Field offsetField = tClass.getDeclaredField(fieldName);
            offset = unsafe.objectFieldOffset(offsetField);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }


    public final int addAndGet(T obj, int delta){
        return unsafe.getAndAddInt(obj, offset, delta) + delta;
    }

    public final int getAndAdd(T obj, int delta){
        return unsafe.getAndAddInt(obj, offset, delta); // obj:Account; offset: money
    }

}
