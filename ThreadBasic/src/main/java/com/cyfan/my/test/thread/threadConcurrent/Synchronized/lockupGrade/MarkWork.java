package com.cyfan.my.test.thread.threadConcurrent.Synchronized.lockupGrade;

/**
 * 对象头，锁所在的对象头
 */


/**
 * |------------------------------------------------------------------------------|--------------------|
 * |                                  Mark Word (64 bits)                         |       State        |
 * |------------------------------------------------------------------------------|--------------------|
 * | unused:25 | identity_hashcode:31 | unused:1 | age:4 | biased_lock:1 | lock:2 |       Normal       |
 * |------------------------------------------------------------------------------|--------------------|
 * | thread:54 |       epoch:2        | unused:1 | age:4 | biased_lock:1 | lock:2 |       Biased       |
 * |------------------------------------------------------------------------------|--------------------|
 * |                       ptr_to_lock_record:62                         | lock:2 | Lightweight Locked |
 * |------------------------------------------------------------------------------|--------------------|
 * |                     ptr_to_heavyweight_monitor:62                   | lock:2 | Heavyweight Locked |
 * |------------------------------------------------------------------------------|--------------------|
 * |                                                                     | lock:2 |    Marked for GC   |
 * |------------------------------------------------------------------------------|--------------------|
 *
 *
 *
 *
 *|-------------------------------------------------------|--------------------|
 * |                  Mark Word (32 bits)                  |       State        |
 * |-------------------------------------------------------|--------------------|
 * | identity_hashcode:25 | age:4 | biased_lock:1 | lock:2 |       Normal       |
 * |-------------------------------------------------------|--------------------|
 * |  thread:23 | epoch:2 | age:4 | biased_lock:1 | lock:2 |       Biased       |
 * |-------------------------------------------------------|--------------------|
 * |               ptr_to_lock_record:30          | lock:2 | Lightweight Locked |
 * |-------------------------------------------------------|--------------------|
 * |               ptr_to_heavyweight_monitor:30  | lock:2 | Heavyweight Locked |
 * |-------------------------------------------------------|--------------------|
 * |                                              | lock:2 |    Marked for GC   |
 * |-------------------------------------------------------|--------------------|
 *
 * |--------------------------------------------------------------------------------|
 * |                                 Mark Word (32 bits)                            |
 * |--------------------------------------------------------------------------------|
 * | 锁状态	    |           25bit		  |4bit	        |1bit	     |      2bit    |
 * | 	        |   23bit	        |2bit |             | 是否偏向锁 |    锁标志位  |
 * |------------|-------------------|-----|-------------|------------|--------------|
 * | 无锁状态	|   对象的hashCode   	  |分代年龄	    |    0	     |      01      |
 * |------------|-------------------|-----|-------------|------------|--------------|
 * | 偏向锁	    |   线程ID	        |epoch|分代年龄	    |    1	     |      01      |
 * |------------|----------------------------------------------------|--------------|
 * | 轻量级锁	|            指向栈中锁记录的指针	                 |      00      |
 * |------------|----------------------------------------------------|--------------|
 * | 重量级锁	|           指向互斥量（重量级锁的指针）             | 	    10      |
 * |------------|----------------------------------------------------|--------------|
 * | GC标志	    |                           空				         |      11      |
 * |--------------------------------------------------------------------------------|
 *
 * 指向栈中锁记录的指针 即MarkWord中指向线程栈帧中lockRecord的指针
 *  指向互斥量（重量级锁的指针） 即markWord中指向ObjectMonitor的指针
 *  并发字段
 *   偏向锁：线程ID
 *   轻量级锁： 指向栈中锁记录的指针lockRecord
 *   重量级锁：重量级锁的指针ObjectMonitor重的Owner字段
 */
public class MarkWork implements Cloneable{

    private String flag = "01";//无锁或者是偏向锁状态
    private String biasedLock = "1"; //默认偏向锁，是否偏向锁
    private String epoch;
    private String age; // 分代年龄
    private volatile long threadID = -1L; //线程ID
    private volatile LockRecord ptrLockRecord;//指向轻量级锁的指针
    private ObjectMonitor ptrMonitor;//指向重量级锁的指针
    private volatile  String inflateStatus; //锁膨胀的状态

    public String getInflateStatus() {
        return inflateStatus;
    }

    public void setInflateStatus(String inflateStatus) {
        this.inflateStatus = inflateStatus;
    }

    public String getBiasedLock() {
        return biasedLock;
    }

    public void setBiasedLock(String biasedLock) {
        this.biasedLock = biasedLock;
    }

    public String getEpoch() {
        return epoch;
    }

    public void setEpoch(String epoch) {
        this.epoch = epoch;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public long getThreadID() {
        return threadID;
    }

    public void setThreadID(long threadID) {
        this.threadID = threadID;
    }

    public String getFlag() {
        return flag;
    }

    public LockRecord getPtrLockRecord() {
        return ptrLockRecord;
    }

    public void setPtrLockRecord(LockRecord ptrLockRecord) {
        this.ptrLockRecord = ptrLockRecord;
    }

    public ObjectMonitor getPtrMonitor() {
        return ptrMonitor;
    }

    public void setPtrMonitor(ObjectMonitor ptrMonitor) {
        this.ptrMonitor = ptrMonitor;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }


    @Override
    protected Object clone(){
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
