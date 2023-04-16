package com.cyfan.study.a12.mycase;

import com.cyfan.study.a12.mycase.client.PrinterClient;

public class Test {

    public static void main(String[] args) {
        PrinterClient printerClient = new PrinterClient();
        printerClient.print("zhangsan");
        printerClient.print("lisi");
        printerClient.copy("wanger");
    }
}
