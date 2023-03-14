package com.cyfan.juc.my.test.thread.threadConcurrent.Volatile;

/**
 * 缓存命中测试
 *  CUP 多级Cache，Cache由CacheLine组成。
 *      CacheLine 的局部性
 *          时间局部性：比如一个cacheLine中数组，下标为1的数据被访问到了，那么大概率最近还会被访问到
 *          空间局部性：下表为1的数据被访问到了，那么大概率后面的2，3，4大概率最近会被访问
 *  主存中数据被读取到cache中，是以一个cacheLine为单位被一次性读取一堆的。
 */
public class CacheHitTest {
    static  int count = 2048;
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        int[][] a = new int[count][count];
        for (int i = 0; i < count; i++) {
            for (int j = 0; j < count; j++) {
                a[i][j] = 1;//按行赋值  这个比较快，因为缓存命中率高
                //a[j][i] = 1;//按列赋值 这个比较慢，因为缓存命中率低了
            }
        }
        System.out.println(System.currentTimeMillis()-start);
    }
}
