package com.mjlf.aop;

import com.mjlf.annotate.After;
import com.mjlf.annotate.Aspect;
import com.mjlf.annotate.Before;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

@Aspect
public class ControllerAop {

    /**
     * 有用表示前置打印出放请求参数
     */
    @Before(value = "printParams")
    public void before(HttpServletRequest request, HttpServletResponse response){
        if(request != null){
            Enumeration<String> names = request.getParameterNames();
            while (names.hasMoreElements()){
                String name = names.nextElement();
                System.out.println("name : " + name + " | value : " + request.getParameter(name));
            }
        }
    }

    @After(value = "end")
    public void after(HttpServletRequest request, HttpServletResponse response){
        System.out.println("end");
    }
}
