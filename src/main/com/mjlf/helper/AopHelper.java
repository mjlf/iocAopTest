package com.mjlf.helper;

import com.mjlf.annotate.After;
import com.mjlf.annotate.Aspect;
import com.mjlf.annotate.Before;
import com.mjlf.bean.ProxyMethod;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class AopHelper {
    private static Map<String, ProxyMethod> PROXY_METHOD = new HashMap<String, ProxyMethod>();

    public static void setProxyMethod() {
        ClassHelper.CLASS_SET.stream().forEach(cls -> {
            if (cls.isAnnotationPresent(Aspect.class)) {
                List<Method> methods = Arrays.asList(cls.getMethods());
                if (methods != null) {
                    methods.stream().filter(method -> {
                        if (method.isAnnotationPresent(Before.class)
                                || method.isAnnotationPresent(After.class)) {
                            return true;
                        }
                        return false;
                    }).forEach(method -> {
                        if (method.isAnnotationPresent(Before.class)) {
                            Before before = method.getAnnotation(Before.class);
                            String name = before.value();
                            System.out.println(BeanHelper.BEAN_MAP.get(cls));
                            ProxyMethod proxyMethod = new ProxyMethod(name, method, BeanHelper.BEAN_MAP.get(cls));
                            PROXY_METHOD.put(name, proxyMethod);
                        } else if (method.isAnnotationPresent(After.class)) {
                            After after = method.getAnnotation(After.class);
                            String name = after.value();
                            ProxyMethod proxyMethod = new ProxyMethod(name, method, BeanHelper.BEAN_MAP.get(cls));
                            PROXY_METHOD.put(name, proxyMethod);
                        }
                    });
                }
            }
        });
    }

    public static ProxyMethod getProxyMethodByName(String name) {
        return PROXY_METHOD.get(name);
    }
}
