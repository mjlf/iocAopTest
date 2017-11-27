package com.mjlf.bean;

import java.lang.reflect.Method;

public class ProxyMethod {

    private String name;
    private Method method;
    private Object object;

    public ProxyMethod() {
    }

    public ProxyMethod(String name, Method method, Object object) {
        this.name = name;
        this.method = method;
        this.object = object;
    }

    public String getName() {
        return name;
    }

    public Method getMethod() {
        return method;
    }

    public Object getObject() {
        return object;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProxyMethod that = (ProxyMethod) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (method != null ? !method.equals(that.method) : that.method != null) return false;
        return object != null ? object.equals(that.object) : that.object == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (method != null ? method.hashCode() : 0);
        result = 31 * result + (object != null ? object.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ProxyMethod{" +
                "name='" + name + '\'' +
                ", method=" + method +
                ", object=" + object +
                '}';
    }
}
