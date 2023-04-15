package com.cyfan.study.a11.mycase;

public class Client {

    protected  void saveUserInfo(UserInfo userInfo ,int i){
        User user = new User();
        user.setId(i+"");
        user.setName("zhangsan"+i);
        user.setAddress("zhangzhuang"+i);


        userInfo.putUser(user);
        System.out.println("get i = " + i);
        User user1 = userInfo.getUser(i+"");

        System.out.println("key =  "+i +",---- "+user1.toString());
    }
}
