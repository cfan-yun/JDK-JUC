package com.cyfan.juc.my.test.thread.threadConcurrent.Volatile;

import fr.ujm.tse.lt2c.satin.cache.size.*;

/**
 * cache 多路关联测试ways是
 */
public class CacheWaysTest {

    public static void main(String[] args) {
        CacheInfo instance = CacheInfo.getInstance();
        CacheLevelInfo cacheInformation = instance.getCacheInformation(CacheLevel.L1, CacheType.DATA_CACHE);
        System.out.println("L1 Information Sets:"+cacheInformation.getCacheSets()+" 行/列");
        System.out.println("L1 Information Ways:"+cacheInformation.getCacheWaysOfAssociativity()+ " 列/行");
        System.out.println("L1 Information Cache Size:"+cacheInformation.getCacheSize() + " 字节");
        System.out.println("L1 Information CacheLine size:"+ cacheInformation.getCacheSize()/(cacheInformation.getCacheSets()*cacheInformation.getCacheWaysOfAssociativity()) + " 字节");

        int total = instance.getCacheInformation(CacheLevel.L1,CacheType.INSTRUCTION_CACHE).getCacheSize()
                +instance.getCacheInformation(CacheLevel.L1,CacheType.DATA_CACHE).getCacheSize();
                //+instance.getCacheInformation(CacheLevel.L1,CacheType.UNIFIED_CACHE).getCacheSize();
                //+instance.getCacheInformation(CacheLevel.L1,CacheType.UNKNOWN_CACHE).getCacheSize();
    }
}
