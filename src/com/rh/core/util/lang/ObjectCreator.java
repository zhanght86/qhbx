package com.rh.core.util.lang;


/**
 * 实例化指定名称的类。
 * 
 * @author yangjinyun
 */
public class ObjectCreator {

    /**
     * 

     * 实例化指定名称的类，并返回实例对象。如果指定的类名不存在，或者不是指定接口的实现类，则返回null。
     * @param <T> 返回指定类型类。
     * 
     * @param interfaceCls 指定接口，或抽象类
     * @param clsName 需要被初始化的类的名称。
     * @return 初始化类的实例对象
     */
    public static <T> T create(Class<T> interfaceCls, String clsName) {
        T obj = null;

        @SuppressWarnings("unchecked")
        Class<T> cls = findClass(clsName);

        if (cls != null && interfaceCls.isAssignableFrom(cls)) {
            // 如果是接口的实现类
            try {
                obj = (T) cls.newInstance();
            } catch (Exception e) {
                obj = null;
                throw new RuntimeException(clsName + "不是 " + interfaceCls.getName()
                        + " 接口的实现类.");
            }
        } else {
            throw new RuntimeException(clsName + "不是 " + interfaceCls.getName()
                    + " 接口的实现类.");
        }

        return obj;
    }

    /**
     * @param strClsName 需要被初始化的类的名称。
     * @return 如果能根据指定名称找到类，则返回类对象，否则返回null。
     */
    @SuppressWarnings("rawtypes")
    private static Class findClass(String strClsName) {
        Class cls = null;

        try {
            cls = Class.forName(strClsName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("ClassNotFoundException:" + e.getMessage());
        }

        return cls;
    }
}
