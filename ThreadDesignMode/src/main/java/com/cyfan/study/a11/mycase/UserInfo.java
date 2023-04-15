package com.cyfan.study.a11.mycase;

import java.util.HashMap;
import java.util.Map;

public class UserInfo {

    private static final ThreadLocal<UserSpecialInfo>  userSpecialInfoThreadLocal = new ThreadLocal<>();

    public User putUser(User user){
        return  this.getUserSpecialInfo().putUser(user);
    }

    public User getUser(String ID){
        return  this.getUserSpecialInfo().getUser(ID);
    }

    public UserSpecialInfo getUserSpecialInfo(){

        UserSpecialInfo userSpecialInfo = userSpecialInfoThreadLocal.get();
        if (userSpecialInfo == null){
            userSpecialInfo = new UserSpecialInfo();
            userSpecialInfoThreadLocal.set(userSpecialInfo);
        }
        System.out.println(userSpecialInfo);
        return userSpecialInfo;
    }

}
