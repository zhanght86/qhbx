package com.rh.core.serv;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 服务监听
 * @author wanghg
 */
public class ServListener {
    private static final String INIT = "init";
    private static final String AFTER = "after";
    private static final String BEFORE = "before";
    /**
     * 监听类
     */
    private Class<?> cls;
    /**
     * 方法map
     */
    private Map<String, Method> methodMap = new HashMap<String, Method>();
    private String conf;
    private String serv;
    /**
     * 服务监听
     * @param serv 服务
     * @param lisCls 类名
     * @param conf 配置
     * @throws Exception 例外
     */
    public ServListener(String serv, String lisCls, String conf) throws Exception {
        this.cls = Class.forName(lisCls);
        this.serv = serv;
        this.conf = conf;
    }
    /**
     * 调用方法
     * @param at 时机
     * @param methodName 方法名称
     * @param classes 类
     * @param objects 对象
     */
    private void invoke(String at, String methodName, Class<?>[] classes, Object[] objects) {
        Method method = null;
        if (!methodMap.containsKey(at)) { //监听before和after
            try {
                Class<?>[] classes2 = new Class<?>[classes.length + 1];
                classes2[0] = String.class;
                System.arraycopy(classes, 0, classes2, 1, classes.length);
                method = this.cls.getMethod(at, classes2);
            } catch (Exception e) {
                method = null;
            }
            this.methodMap.put(at, method);
        } else {
            method = this.methodMap.get(at);
        }
        if (method != null) {
            try {
                Object[] objects2 = new Object[objects.length + 1];
                objects2[0] = methodName;
                System.arraycopy(objects, 0, objects2, 1, objects.length);
                method.invoke(newInstance(), objects2);
            } catch (Exception e) {
                throw new RuntimeException("监听执行错误:" + this.cls.getName() + "." + methodName, e);
            }
        } else {
            methodName = at + Character.toUpperCase(methodName.charAt(0)) + methodName.substring(1);
            if (!methodMap.containsKey(methodName)) {
                try {
                    method = this.cls.getMethod(methodName, classes);
                } catch (Exception e) {
                    method = null;
                }
                this.methodMap.put(methodName, method);
            } else {
                method = methodMap.get(methodName);
            }
            if (method != null) {
                try {
                    method.invoke(newInstance(), objects);
                } catch (Exception e) {
                    throw new RuntimeException("监听执行错误:" + this.cls.getName() + "." + methodName, e);
                }
            }
        }
    }
    /**
     * 实例化
     * @return 实例
     * @throws Exception 错误
     */
    private Object newInstance() throws Exception {
        Object instance = this.cls.newInstance();
        Method method = null;
        if (!methodMap.containsKey(INIT)) { //监听before和after
            try {
                method = this.cls.getMethod(INIT, String.class, String.class);
            } catch (Exception e) {
                method = null;
            }
            this.methodMap.put(INIT, method);
        } else {
            method = this.methodMap.get(INIT);
        }
        if (method != null) {
            method.invoke(instance, new Object[] {this.serv, this.conf});
        }
        return instance;
    }
    /**
     * before监听
     * @param act 操作
     * @param param 参数bean
     */
    public void before(String act, ParamBean param) {
        invoke(BEFORE, act, new Class[] {ParamBean.class}, 
                new Object[] {param});
    }
    /**
     * after监听
     * @param act 操作
     * @param param 参数
     * @param result 结果
     */
    public void after(String act, ParamBean param, OutBean result) {
        invoke(AFTER, act, new Class[] {ParamBean.class, OutBean.class}, 
                new Object[] {param, result});
    }
}
