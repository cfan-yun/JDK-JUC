package com.cyfan.study.a11.mycase;

import java.util.HashMap;
import java.util.Map;

public class UserSpecialInfo {



    private Map<String, User> map;
    public UserSpecialInfo(){
        this.map =  new HashMap<>();
    }

    public User putUser(User user){
        return  this.map.put(user.getId(), user);
    }

    public User getUser(String ID){
        return this.map.get(ID);
    }



}
