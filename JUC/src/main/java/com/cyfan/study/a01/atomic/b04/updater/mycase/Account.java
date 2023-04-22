package com.cyfan.study.a01.atomic.b04.updater.mycase;

public class Account {
    protected volatile int money; //这里一定要加volatile当多线程操作时

    public Account(int money) {
        this.money = money;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    @Override
    public String toString() {
        return "Account{" +
                "money=" + money +
                '}';
    }
}
