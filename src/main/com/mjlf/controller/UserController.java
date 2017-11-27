package com.mjlf.controller;

import com.mjlf.annotate.Autowired;
import com.mjlf.annotate.Controller;
import com.mjlf.annotate.NeedInterceptMethod;
import com.mjlf.annotate.RequestMapping;
import com.mjlf.server.OrderServer;
import com.mjlf.server.UserServer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class UserController {

    @Autowired
    private UserServer userServer;
    @Autowired
    private OrderServer orderServer;

    @NeedInterceptMethod(function = {"printParams", "end"})
    @RequestMapping(method = "POST", path = "/mjlf/login")
    public void login(HttpServletRequest request, HttpServletResponse response){
        String name = request.getParameter("name");
        String pass = request.getParameter("pass");

        if(userServer.isLogin(name)){
            int count = orderServer.getCountByUName(name);
            try {
                System.out.println(count);
                response.getWriter().write(count + "");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            try {
                response.getWriter().write("don't login");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
