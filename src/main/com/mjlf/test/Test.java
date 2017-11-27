package com.mjlf.test;

import com.mjlf.helper.ProxyManager;

public class Test {

    @org.junit.Test
    public void test(){
        CGlibProxy cGlibProxy = new CGlibProxy();
//        Student student = (Student) cGlibProxy.getProxy(Student.class);
        Student student = (Student) ProxyManager.createProxy(Student.class);
        student.study();
        student.eat();
    }
}
