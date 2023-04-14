package com.cyfan.study.a09.mycase03;

public class SpiderServer {

    public static void main(String[] args) {

        long start = System.currentTimeMillis();
        // 8784 8536  7443 7991
        IContent content1 = Spider.grabByObj("https://www.baidu.com/");
        IContent content2 = Spider.grabByObj("https://www.taobao.com/");
        IContent content3 = Spider.grabByObj("https://www.jd.com/");

        //6461 6000 5974
//        IContent content1 = Spider.asyncGrabByObj("https://www.baidu.com/");
//        IContent content2 = Spider.asyncGrabByObj("https://www.taobao.com/");
//        IContent content3 = Spider.asyncGrabByObj("https://www.jd.com/");
        Spider.save2File("baidu.html",  content1);
        Spider.save2File("taobao.html",  content2);
        Spider.save2File("jd.html",  content3);

        System.out.println("spend time = " + (System.currentTimeMillis() -  start));
    }
}
