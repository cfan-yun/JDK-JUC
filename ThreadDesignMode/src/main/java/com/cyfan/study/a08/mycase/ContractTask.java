package com.cyfan.study.a08.mycase;

public class ContractTask implements Runnable{

    private int serialNo;
    private String contractName;


    public ContractTask(int serialNo,String contractName){
        this.serialNo = serialNo;
        this.contractName =  contractName;
    }


    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName()+" 执行 "+this.toString());
    }

    @Override
    public String toString() {
        return "ContractTask{" +
                "serialNo=" + serialNo +
                ", contractName='" + contractName + '\'' +
                '}';
    }
}
