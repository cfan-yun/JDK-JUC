package com.cyfan.study.a06.mycase02;

public class ReadDataBaseThread<K,V> extends Thread {

    private final K key;
    private final MyDataBase<K,V> myDataBase;

    public ReadDataBaseThread(MyDataBase<K,V> dataBase, K key){
        this.key =  key;
        this.myDataBase = dataBase;
    }


    @Override
    public void run() {
        while (true){

            try {
                this.myDataBase.get(this.key);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
