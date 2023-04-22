package com.cyfan.study.a01.atomic.b02.basic;

import com.cyfan.study.utils.UnsafeUtils;
import sun.misc.Unsafe;

/**
 * 通用原子类
 */
public class MyAtomicVersion<V> implements java.io.Serializable {
    private static final long serialVersionUID = 6214790243416807050L;

    // setup to use Unsafe.compareAndSwapInt for updates
    private static final Unsafe unsafe = UnsafeUtils.getUnsafe();
    private static final long valueOffset;

    private static final long valueVersionOffset;

    private  static final class ValueVersion<T>{//value添加版本号
        private volatile T value;

        private volatile int version;

        private ValueVersion(T value, int version){
            this.value = value;
            this.version= version;
        }
    }

    static {
        try {
            valueOffset = unsafe.objectFieldOffset
                    (ValueVersion.class.getDeclaredField("value"));
            valueVersionOffset = unsafe.objectFieldOffset
                    (MyAtomicVersion.class.getDeclaredField("valueVersion"));
        } catch (Exception ex) { throw new Error(ex); }
    }

//    private volatile int value;
//
//    private volatile int version;

    private volatile ValueVersion<V> valueVersion;

    /**
     * Creates a new AtomicInteger with the given initial value.
     *
     * @param initialValue the initial value
     */
    public MyAtomicVersion(V initialValue, int initialVersion) {
//        value = initialValue;
        this.valueVersion = new ValueVersion(initialValue, initialVersion);
    }



    /**
     * Creates a new AtomicInteger with initial value {@code 0}.
     */
    public MyAtomicVersion(V initialValue) {
        this.valueVersion = new ValueVersion(initialValue, 0);
    }

    /**
     * Gets the current value.
     *
     * @return the current value
     */
    public final ValueVersion get() {
        return this.valueVersion;
    }

    public final V getValue() {
        return this.valueVersion.value;
    }
    public final int getVersion() {
        return this.valueVersion.version;
    }

    /**
     * Sets to the given value.
     *
     * @param newValue the new value
     */
    public final void set(V  newValue, int newVersion) {
       if (newValue != this.valueVersion.value || newVersion != this.valueVersion.version){
           this.valueVersion = new ValueVersion(newValue, newVersion);
       }
    }


    public final void setValue(V  newValue) {
        this.valueVersion.value = newValue;
    }
    /**
     * Eventually sets to the given value.
     *
     * @param newValue the new value
     * @since 1.6
     */
    public final void lazySet(V newValue) {
        unsafe.putOrderedObject(this.valueVersion, valueOffset, newValue);
    }
    public final void lazySet(V newValue, int newVersion) {
        unsafe.putOrderedObject(this, valueVersionOffset, new ValueVersion(newValue, newVersion));
    }


    /**
     * Atomically sets to the given value and returns the old value.
     *
     * @param newValue the new value
     * @return the previous value
     */
    public final Object getAndSet(V newValue) {
        return unsafe.getAndSetObject(this.valueVersion, valueOffset, newValue);
    }

    /**
     * Atomically sets the value to the given updated value
     * if the current value {@code ==} the expected value.
     *
     * @param expect the expected value
     * @param update the new value
     * @return {@code true} if successful. False return indicates that
     * the actual value was not equal to the expected value.
     */
    public final boolean compareAndSet(V expect, V update) {
        ValueVersion current = this.valueVersion;//当前值
        return expect == current.value &&     // 预期值等于当前值 并且
                ((update == current.value)  //当前值等于新值，即要修改的结果与原来一样，那么可以不改，直接返回
                        || unsafe.compareAndSwapObject(this, valueVersionOffset, current, new ValueVersion(update, -1)));
    }

    public final boolean compareAndSet(V expectValue, V newValue, int exceptVersion, int newVersion) {
        ValueVersion current = this.valueVersion;//当前值
        return expectValue == current.value &&     // 预期值等于当前值 并且
                exceptVersion == current.version && // 预期版本等于当前版本 并且
                ((newValue == current.value && newVersion == current.version)  //当前值等于新值，即要修改的结果与原来一样，那么可以不改，直接返回
                        || unsafe.compareAndSwapObject(this, valueVersionOffset, current, new ValueVersion(newValue, newVersion)));
    }

    /**
     * Atomically sets the value to the given updated value
     * if the current value {@code ==} the expected value.
     *
     * <p><a href="package-summary.html#weakCompareAndSet">May fail
     * spuriously and does not provide ordering guarantees</a>, so is
     * only rarely an appropriate alternative to {@code compareAndSet}.
     *
     * @param expect the expected value
     * @param update the new value
     * @return {@code true} if successful
     */
    public final boolean weakCompareAndSet(V expect, V update) {
        ValueVersion current = this.valueVersion;//当前值
        return expect == current.value &&     // 预期值等于当前值 并且
                ((update == current.value)  //当前值等于新值，即要修改的结果与原来一样，那么可以不改，直接返回
                        || unsafe.compareAndSwapObject(this, valueVersionOffset, current, new ValueVersion(update, -1)));
    }

    /**
     * Atomically increments by one the current value.
     *
     * @return the previous value
     */
    public final int getAndIncrement() {//i++ return i;
        return unsafe.getAndAddInt(this.valueVersion, valueOffset, 1);
    }

    /**
     * Atomically decrements by one the current value.
     *
     * @return the previous value
     */
    public final int getAndDecrement() {// i-- retun i;
        return unsafe.getAndAddInt(this.valueVersion, valueOffset, -1);
    }

    /**
     * Atomically adds the given value to the current value.
     *
     * @param delta the value to add
     * @return the previous value
     */
    public final int getAndAdd(int delta) {
        return unsafe.getAndAddInt(this.valueVersion, valueOffset, delta);
    }

    /**
     * Atomically increments by one the current value.
     *
     * @return the updated value
     */
    public final int incrementAndGet() {// ++i return i + 1;
        return unsafe.getAndAddInt(this.valueVersion, valueOffset, 1) + 1;
    }


    /**
     * Atomically decrements by one the current value.
     *
     * @return the updated value
     */
    public final int decrementAndGet() {// --i return i -1;
        return unsafe.getAndAddInt(this.valueVersion, valueOffset, -1) - 1;
    }

    /**
     * Atomically adds the given value to the current value.
     *
     * @param delta the value to add
     * @return the updated value
     */
    public final int addAndGet(int delta) {
        return unsafe.getAndAddInt(this.valueVersion, valueOffset, delta) + delta;
    }

    /**
     * Atomically updates the current value with the results of
     * applying the given function, returning the previous value. The
     * function should be side-effect-free, since it may be re-applied
     * when attempted updates fail due to contention among threads.
     *
     * @param updateFunction a side-effect-free function
     * @return the previous value
     * @since 1.8
     */
//    public final int getAndUpdate(IntUnaryOperator updateFunction) {
//        int prev, next;
//        do {
//            prev = getValue();
//            next = updateFunction.applyAsInt(prev);
//        } while (!compareAndSet(prev, next));
//        return prev;
//    }

    /**
     * Atomically updates the current value with the results of
     * applying the given function, returning the updated value. The
     * function should be side-effect-free, since it may be re-applied
     * when attempted updates fail due to contention among threads.
     *
     * @param updateFunction a side-effect-free function
     * @return the updated value
     * @since 1.8
     */
//    public final int updateAndGet(IntUnaryOperator updateFunction) {
//        int prev, next;
//        do {
//            prev = getValue();
//            next = updateFunction.applyAsInt(prev);
//        } while (!compareAndSet(prev, next));
//        return next;
//    }

    /**
     * Atomically updates the current value with the results of
     * applying the given function to the current and given values,
     * returning the previous value. The function should be
     * side-effect-free, since it may be re-applied when attempted
     * updates fail due to contention among threads.  The function
     * is applied with the current value as its first argument,
     * and the given update as the second argument.
     *
     * @param x the update value
     * @param accumulatorFunction a side-effect-free function of two arguments
     * @return the previous value
     * @since 1.8
     */
//    public final int getAndAccumulate(int x,
//                                      IntBinaryOperator accumulatorFunction) {
//        int prev, next;
//        do {
//            prev = getValue();
//            next = accumulatorFunction.applyAsInt(prev, x);
//        } while (!compareAndSet(prev, next));
//        return prev;
//    }

    /**
     * Atomically updates the current value with the results of
     * applying the given function to the current and given values,
     * returning the updated value. The function should be
     * side-effect-free, since it may be re-applied when attempted
     * updates fail due to contention among threads.  The function
     * is applied with the current value as its first argument,
     * and the given update as the second argument.
     *
     * @param x the update value
     * @param accumulatorFunction a side-effect-free function of two arguments
     * @return the updated value
     * @since 1.8
     */
//    public final int accumulateAndGet(int x,
//                                      IntBinaryOperator accumulatorFunction) {
//        int prev, next;
//        do {
//            prev = getValue();
//            next = accumulatorFunction.applyAsInt(prev, x);
//        } while (!compareAndSet(prev, next));
//        return next;
//    }

    /**
     * Returns the String representation of the current value.
     * @return the String representation of the current value
     */
    public String toString() {
        return Integer.toString((Integer) getValue());
    }

    /**
     * Returns the value of this {@code AtomicInteger} as an {@code int}.
     */
    public int intValue() {
        return (Integer) getValue();
    }

    /**
     * Returns the value of this {@code AtomicInteger} as a {@code long}
     * after a widening primitive conversion.
     * @jls 5.1.2 Widening Primitive Conversions
     */
    public long longValue() {
        return (Long)getValue();
    }

    /**
     * Returns the value of this {@code AtomicInteger} as a {@code float}
     * after a widening primitive conversion.
     * @jls 5.1.2 Widening Primitive Conversions
     */
    public float floatValue() {
        return (Float)getValue();
    }

    /**
     * Returns the value of this {@code AtomicInteger} as a {@code double}
     * after a widening primitive conversion.
     * @jls 5.1.2 Widening Primitive Conversions
     */
    public double doubleValue() {
        return (Double)getValue();
    }

}
