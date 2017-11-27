package com.mjlf.test;

import com.mjlf.annotate.NeedInterceptMethod;

public class Student {
    @NeedInterceptMethod(function = {"pri"})
    public void study(){
        System.out.println("I am studing");
    }

    public void eat(){
        System.out.println("I am eatting");
    }
}
