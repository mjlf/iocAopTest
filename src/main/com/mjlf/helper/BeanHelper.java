package com.mjlf.helper;

import com.mjlf.annotate.Autowired;
import com.mjlf.annotate.Commpant;
import com.mjlf.annotate.NeedInterceptMethod;
import com.mjlf.annotate.Server;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 用户管理系统中需要管理的bean
 */
public final class BeanHelper {
    public final static Map<Class<?>, Object> BEAN_MAP = new HashMap<Class<?>, Object>();

    public static void setBean(Class<?> cls, Object object) {
        BEAN_MAP.put(cls, object);
    }

    /**
     * 实例话bean
     *
     * @param classSet
     */
    public static void instanceBean(Set<Class<?>> classSet) {
        classSet.stream().forEach((cls) -> {
            if (cls != null) {
                try {
                    Object obj = null;
                    List<Method> methods = Arrays.asList(cls.getMethods());
                    boolean needAspect = false;
                    for(Method method : methods){
                        if(method.isAnnotationPresent(NeedInterceptMethod.class)){
                            needAspect = true;
                            break;
                        }
                    }
                    if (needAspect) {
                        obj = ProxyManager.createProxy(cls);
                    } else {
                        obj = cls.newInstance();
                    }
                    BEAN_MAP.put(cls, obj);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });

        setField();
    }

    /**
     * 为BEAN_MAP 中的类设置属性
     */
    public static void setField() {
        Set<Class<?>> keySet = BEAN_MAP.keySet();
        if (keySet != null && keySet.size() > 0) {

            //注入实体类
            keySet.stream().forEach((cls) -> {
                List<Field> fields = Arrays.asList(cls.getDeclaredFields());
                Object object = BEAN_MAP.get(cls);
                fields.stream().filter((field) -> {
                    if (field.isAnnotationPresent(Autowired.class)) {
                        return true;
                    }
                    return false;
                }).forEach((field) -> {
                    Class<?> fieldClass = field.getType();
                    //只能加载指定注解的对象
                    if (fieldClass.isAnnotationPresent(Commpant.class)
                            || fieldClass.isAnnotationPresent(Server.class)) {
                        if (BEAN_MAP.containsKey(fieldClass)) {
                            Object fieldObject = BEAN_MAP.get(fieldClass);
                            try {
                                field.setAccessible(true);
                                field.set(object, fieldObject);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            });
        }
    }

    public Object getBean(Class<?> clazz) {
        if (BEAN_MAP.containsKey(clazz)) {
            return BEAN_MAP.get(clazz);
        }
        return null;
    }
}
