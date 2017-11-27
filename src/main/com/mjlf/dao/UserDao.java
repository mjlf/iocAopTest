package com.mjlf.dao;

import com.mjlf.entity.User;

public interface UserDao {

    default boolean slave(User user){
        return true;
    }

    default boolean login(String uName, String uPass){
        return true;
    }

    default boolean isLogin(String uName){
        if(uName.endsWith("mjlf")){
            return true;
        }
        return false;
    }
}
