package com.mjlf.helper;

import com.mjlf.annotate.After;
import com.mjlf.annotate.Before;
import com.mjlf.annotate.NeedInterceptMethod;
import com.mjlf.bean.ProxyMethod;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public final class ProxyManager {
    public static <T> T createProxy(final Class<?> targetClass) {
        return (T) Enhancer.create(targetClass, new MethodInterceptor() {
            @Override
            public Object intercept(Object targetObject, Method targetMethod, Object[] params,
                                    MethodProxy methodProxy) throws Throwable {
                if (targetMethod.isAnnotationPresent(NeedInterceptMethod.class)) {
                    NeedInterceptMethod needInterceptMethod = targetMethod.getAnnotation(NeedInterceptMethod.class);
                    List<String> functions = Arrays.asList(needInterceptMethod.function());
                    //执行
                    functions.stream().forEach(functionName->{
                        ProxyMethod proxyMethod = AopHelper.getProxyMethodByName(functionName);
                        Method method = proxyMethod.getMethod();
                        if(method.isAnnotationPresent(Before.class)){
                            try {
                                method.invoke(proxyMethod.getObject() ,params);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
                Object result = methodProxy.invokeSuper(targetObject, params);
                if (targetMethod.isAnnotationPresent(NeedInterceptMethod.class)) {
                    NeedInterceptMethod needInterceptMethod = targetMethod.getAnnotation(NeedInterceptMethod.class);
                    List<String> functions = Arrays.asList(needInterceptMethod.function());
                    //执行
                    functions.stream().forEach(functionName->{
                        ProxyMethod proxyMethod = AopHelper.getProxyMethodByName(functionName);
                        Method method = proxyMethod.getMethod();
                        if(method.isAnnotationPresent(After.class)){
                            try {
                                method.invoke(proxyMethod.getObject() ,params);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
                return result;
            }
        });
    }
}
