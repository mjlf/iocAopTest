package com.mjlf.bean;

import java.lang.reflect.Method;

public class Handler {
    private Class<?> controllerClass;
    private Method method;

    public Handler() {
    }

    public Handler(Class<?> controllerClass, Method method) {
        this.controllerClass = controllerClass;
        this.method = method;
    }

    public Class<?> getControllerClass() {
        return controllerClass;
    }

    public void setControllerClass(Class<?> controllerClass) {
        this.controllerClass = controllerClass;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Handler handler = (Handler) o;

        if (controllerClass != null ? !controllerClass.equals(handler.controllerClass) : handler.controllerClass != null)
            return false;
        return method != null ? method.equals(handler.method) : handler.method == null;
    }

    @Override
    public int hashCode() {
        int result = controllerClass != null ? controllerClass.hashCode() : 0;
        result = 31 * result + (method != null ? method.hashCode() : 0);
        return result;
    }
}
