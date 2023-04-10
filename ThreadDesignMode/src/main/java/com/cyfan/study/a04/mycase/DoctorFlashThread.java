package com.cyfan.study.a04.mycase;

import java.io.IOException;
import java.util.Scanner;

/**
 * 医生手动刷新线程
 */
public class DoctorFlashThread extends Thread{

    private String docPath;
    private String docName;

    private final Scanner scanner = new Scanner(System.in);

    public DoctorFlashThread(String docPath,String docName){
       this.docName = docName;
       this.docPath = docPath;
    }

    @Override
    public void run() {
        int times =  0;
        try {
            //1.医生线程创建电子屏，电子屏创建自动刷新线程
            Screen screen = Screen.create(docPath, docName);
            while (true){
                String text = scanner.next();
                screen.edit(text);

                if (times == 5){
                    //触发入库刷新动作
                    screen.save();
                    times = 0;
                }
                times++;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
