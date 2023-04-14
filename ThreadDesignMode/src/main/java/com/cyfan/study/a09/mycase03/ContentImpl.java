package com.cyfan.study.a09.mycase03;

public class ContentImpl implements IContent {

    private  byte[] content;
    public ContentImpl(String urlStr){
        this.content =  Spider.grab(urlStr);
    }

    @Override
    public  byte[] getContent(){
        return this.content;
    }
}
