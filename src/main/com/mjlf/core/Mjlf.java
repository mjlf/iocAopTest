package com.mjlf.core;

import com.mjlf.helper.AopHelper;
import com.mjlf.helper.BeanHelper;
import com.mjlf.helper.ClassHelper;
import com.mjlf.helper.ControllerHelper;

public class Mjlf {

    public static void run(Class<?> cls){
        //加载类
        ClassHelper.loadClassFromBasePackage(ClassHelper.findbasePackage(cls));
        //实例化
        BeanHelper.instanceBean(ClassHelper.getAllBeanClass());
        //加载controller
        ControllerHelper.loadRequetMapping();
        //加载AOP
        AopHelper.setProxyMethod();
    }
}
