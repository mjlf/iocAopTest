package com.mjlf.helper;

import com.mjlf.annotate.*;

import java.io.File;
import java.io.FileFilter;
import java.lang.annotation.Annotation;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

public final class ClassHelper {
    public static final Set<Class<?>> CLASS_SET = new HashSet<Class<?>>();

    /**
     * 加载指定包中的所有类， 并将结果存放到CLASS_SET中
     *
     * @param startClass
     * @return
     */
    public static String findbasePackage(Class<?> startClass) {
        String beanPackagePath = null;
        if (startClass != null) {
            if (startClass.isAnnotationPresent(BasePackage.class)) {
                beanPackagePath = startClass.getAnnotation(BasePackage.class).value();
            }
            beanPackagePath = startClass.getPackage().getName();
        }
        return beanPackagePath;
    }

    /**
     * 获取应用包下某种父类的所有子类
     *
     * @param superClass
     * @return
     */
    public static Set<Class<?>> getCLassSetBySuper(Class<?> superClass) {
        Set<Class<?>> classSet = CLASS_SET.stream()
                .filter((cls) -> {
                    if (superClass.isAssignableFrom(cls) && !superClass.equals(cls)) {
                        return true;
                    }
                    return false;
                }).collect(Collectors.toSet());
        return classSet;
    }

    /**
     * 获取应用包下代用某种注解的所有类
     * @param annotationClass
     * @return
     */
    public static Set<Class<?>> getClassSetByAnnotation(Class<? extends Annotation> annotationClass) {
        Set<Class<?>> classSet = CLASS_SET.stream()
                .filter((cls) -> {
                    if (cls.isAnnotationPresent(annotationClass)) {
                        return true;
                    }
                    return false;
                }).collect(Collectors.toSet());
        return classSet;
    }

    public static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }


    public static void loadClassFromBasePackage(String basePackage) {
        if (basePackage != null) {
            ClassLoader classLoader = getClassLoader();
            try {
                Enumeration<URL> urls = classLoader.getResources(basePackage.replace(".", "/"));
                while (urls.hasMoreElements()) {
                    URL url = urls.nextElement();
                    String protocol = url.getProtocol();
                    if (protocol.equals("file")) {
                        String packagePath = url.getPath().replaceAll("%20", " ");
                        addClass(basePackage, packagePath);
                    } else {
                        JarURLConnection jarURLConnection = (JarURLConnection) url.getContent();
                        if (jarURLConnection != null) {
                            JarFile jarFile = jarURLConnection.getJarFile();
                            if (jarFile != null) {
                                Enumeration<JarEntry> jarEntryEnumeration = jarFile.entries();
                                while (jarEntryEnumeration.hasMoreElements()) {
                                    JarEntry jarEntry = jarEntryEnumeration.nextElement();
                                    String jarEntryName = jarEntry.getName();
                                    if (jarEntryName.endsWith(".class")) {
                                        String className = jarEntryName.substring(0, jarEntryName.lastIndexOf("."))
                                                .replaceAll("/", ".");
                                        doAddClass(className);
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void addClass(String basePackage, String packagePath) {
        List<File> files = Arrays.asList(new File(packagePath).listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return (file.isFile() && file.getName().endsWith(".class") || file.isDirectory());
            }
        }));
        files.stream().forEach((file) -> {
            String fileName = file.getName();
            if (file.isFile()) {
                String className = fileName.substring(0, fileName.lastIndexOf("."));
                if (basePackage != null && basePackage.trim().length() > 0) {
                    className = basePackage + "." + className;
                }
                doAddClass(className);
            } else if (file.isDirectory()) {
                String subPackagePath = fileName;
                if (packagePath != null && packagePath.trim().length() > 0) {
                    subPackagePath = packagePath + "/" + subPackagePath;
                }
                String subPackageName = fileName;
                if (basePackage != null && basePackage.trim().length() > 0) {
                    subPackageName = basePackage + "." + subPackageName;
                }
                addClass(subPackageName, subPackagePath);
            }
        });
    }

    public static void doAddClass(String className) {
        Class<?> clazz = loadClass(className, false);
        CLASS_SET.add(clazz);
    }

    public static Class<?> loadClass(String className, boolean isInitialized) {
        ClassLoader classLoader = getClassLoader();
        Class<?> cls = null;
        try {
            cls = classLoader.loadClass(className);
            cls.forName(className, isInitialized, classLoader);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return cls;
    }

    /**
     * 获取带有server注解的类
     *
     * @return
     */
    public static Set<Class<?>> getAnnotateServer() {
        Set<Class<?>> serverSet = CLASS_SET.stream().filter((cls) -> {
            if (cls.isAnnotationPresent(Server.class)) {
                return true;
            }
            return false;
        }).collect(Collectors.toSet());
        return serverSet;
    }

    /**
     * 获取带有Controller注解的类
     *
     * @return
     */
    public static Set<Class<?>> getAnnotateController() {
        Set<Class<?>> controllerSet = CLASS_SET.stream().filter((cls) -> {
            if (cls.isAnnotationPresent(Controller.class)) {
                return true;
            }
            return false;
        }).collect(Collectors.toSet());
        return controllerSet;
    }

    public static Set<Class<?>> getAnnotateCommpant() {
        Set<Class<?>> commpantSet = CLASS_SET.stream().filter((cls) -> {
            if (cls.isAnnotationPresent(Commpant.class)) {
                return true;
            }
            return false;
        }).collect(Collectors.toSet());
        return commpantSet;
    }

    public static Set<Class<?>> getAnnotateAspect() {
        Set<Class<?>> commpantSet = CLASS_SET.stream().filter((cls) -> {
            if (cls.isAnnotationPresent(Aspect.class)) {
                return true;
            }
            return false;
        }).collect(Collectors.toSet());
        return commpantSet;
    }

    /**
     * 获取指定注解类并对其进行实例化
     * @return
     */
    public static Set<Class<?>> getAllBeanClass() {
        Set<Class<?>> classSet = new HashSet<>();
        classSet.addAll(getAnnotateServer());
        classSet.addAll(getAnnotateController());
        classSet.addAll(getAnnotateCommpant());
        classSet.addAll(getAnnotateAspect());
        return classSet;
    }
}
