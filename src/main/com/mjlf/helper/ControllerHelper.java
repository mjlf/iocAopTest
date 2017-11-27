package com.mjlf.helper;


import com.mjlf.annotate.Controller;
import com.mjlf.annotate.RequestMapping;
import com.mjlf.bean.Handler;
import com.mjlf.bean.Request;

import java.lang.reflect.Method;
import java.util.*;

public final class ControllerHelper {
    public final static Map<Request, Handler> REQUEST_MAP = new HashMap<Request, Handler>();

    public static void loadRequetMapping(){
        Set<Class<?>> controllerClass = BeanHelper.BEAN_MAP.keySet();
        if(controllerClass != null){
            controllerClass.stream().filter((cls)->{
                if(cls.isAnnotationPresent(Controller.class)){
                    return true;
                }
                return false;
            }).forEach((cls)->{
                List<Method> methodList = Arrays.asList(cls.getMethods());
                methodList.stream().filter((method) -> {
                    if(method.isAnnotationPresent(RequestMapping.class)){
                        return true;
                    }
                    return false;
                }).forEach((method) -> {
                    String requestMethod = method.getAnnotation(RequestMapping.class).method();
                    String requestPath = method.getAnnotation(RequestMapping.class).path();

                    Request request = new Request(requestMethod, requestPath);
                    Handler handler = new Handler(cls, method);

                    REQUEST_MAP.put(request, handler);
                });
            });
        }
    }

    public static Handler getHandler(Request request){
        if(request != null){
            return REQUEST_MAP.get(request);
        }
        return null;
    }
}
