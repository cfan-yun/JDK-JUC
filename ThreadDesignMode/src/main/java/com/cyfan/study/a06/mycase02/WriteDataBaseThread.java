package com.cyfan.study.a06.mycase02;

public class WriteDataBaseThread<K,V> extends Thread{



    private final K key;
    private final MyDataBase<K,V> myDataBase;
    private final V value;

    public WriteDataBaseThread(MyDataBase<K,V> dataBase, K key, V value){
        this.key =  key;
        this.value = value;
        this.myDataBase = dataBase;

    }


    @Override
    public void run() {
        while (true){
            try {
                this.myDataBase.set(this.key,this.value);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
