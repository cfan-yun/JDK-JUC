package com.cyfan.study.a04.mycase;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 电子屏
 */
public class Screen {

    private String docPath;
    private String docName;
    private List<String> content = new ArrayList<>();

    private boolean changed =  false;

    private FileWriter fileWriter;
    public Screen(String docPath,String docName) throws IOException {
        this.docPath = docPath;
        this.docName = docName;

        FileWriter fileWriter = new FileWriter(new File(docPath, docName));
        this.fileWriter = fileWriter;
    }


    public static Screen create(String docPath,String docName) throws IOException {
        Screen screen = new Screen(docPath, docName);
        //自动刷新线程
        AutoFlashThread autoFlashThread = new AutoFlashThread(screen);
        autoFlashThread.setName("autoFlashThread");
        autoFlashThread.start();
        return screen;

    }


    //编辑录入
    public void edit(String text){
        synchronized (this){
            this.content.add(text);
            this.changed = true;
        }
    }


    //保存入库（保存到文件）+ 显示,由两个线程调用(自动刷新和手动刷新两个线程)
    public void  save() throws IOException {
        //多线程操作
        synchronized (this){
            //如果没有更新，那么直接返回，无需任何操作
            if (!changed){
                return;
            }
            //如果由更新，才入库刷新
            for (String name: content) {
                this.fileWriter.write(name);
                this.fileWriter.write("\r\n");
                System.out.println(Thread.currentThread().getName()+","+name);
            }
            this.fileWriter.flush();
            changed = false;//状态更新，更新未已刷新过，无变化
        }
    }

}
