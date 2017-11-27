package com.mjlf.core;

import com.mjlf.AppStart;
import com.mjlf.bean.Handler;
import com.mjlf.bean.Request;
import com.mjlf.helper.BeanHelper;
import com.mjlf.helper.ControllerHelper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Enumeration;

public class DoService extends HttpServlet {

    @Override
    public void init() throws ServletException {
        super.init();
        Mjlf.run(AppStart.class);
    }

    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try{
            String requstMethod = req.getMethod().toUpperCase();
            String requestPath = req.getRequestURI();

            Request request = new Request(requstMethod, requestPath);
            Handler handler = ControllerHelper.getHandler(request);

            if(handler != null){
                Method method = handler.getMethod();
                Class<?> cls = handler.getControllerClass();
                Object obj = BeanHelper.BEAN_MAP.get(cls);

                method.invoke(obj, req, res);
            }else {
                System.out.println("don't have the path");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
