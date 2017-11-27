package com.mjlf.server;

import com.mjlf.annotate.Autowired;
import com.mjlf.annotate.Server;

@Server
public class OrderServer {

    @Autowired
    private UserServer userServer;

    public int getCountByUName(String uName){
        if(userServer.isLogin(uName)){
            return 1000;
        }
        return 0;
    }
}
