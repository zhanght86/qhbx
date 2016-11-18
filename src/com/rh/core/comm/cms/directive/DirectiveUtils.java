package com.rh.core.comm.cms.directive;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.rh.core.base.Bean;

import freemarker.core.Environment;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateNumberModel;
import freemarker.template.TemplateScalarModel;

/**
 * freemarker 自定义宏帮助类
 * @author liwei
 * 
 */
public class DirectiveUtils {

    /**
     * 添加指定参数到环境变量中
     * 
     * @param env - 环境变量
     * @param params - 参数
     * @return - 添加后的参数集合
     * @throws TemplateException - freemarker template exception
     */
    public static Map<String, TemplateModel> addParamsToVariable(Environment env, Map<String, TemplateModel> params)
            throws TemplateException {
        Map<String, TemplateModel> origMap = new HashMap<String, TemplateModel>();
        if (params.size() <= 0) {
            return origMap;
        }
        Set<Entry<String, TemplateModel>> entrySet = params.entrySet();

        for (@SuppressWarnings("rawtypes")
        Map.Entry entry : entrySet) {
            String key = (String) entry.getKey();
            TemplateModel value = env.getVariable(key);
            if (value != null) {
                origMap.put(key, value);
            }
            env.setVariable(key, (TemplateModel) entry.getValue());
        }
        return origMap;
    }

    /**
     * 根据指定参数中获取数据
     * @param name - key
     * @param params - 模版参数
     * @return - 参数值（字符串）
     * @throws TemplateException - freemarker template exception
     */
    public static String getString(String name, Map<String, TemplateModel> params)
            throws TemplateException {
        TemplateModel model = (TemplateModel) params.get(name);
        if (model == null) {
            return null;
        }
        if ((model instanceof TemplateScalarModel)) {
            return ((TemplateScalarModel) model).getAsString();
        }
        if ((model instanceof TemplateNumberModel)) {
            return ((TemplateNumberModel) model).getAsNumber().toString();
        }
        throw new RuntimeException(name);
    }
    
    /**
     * 
     * @param params 参数
     * @return 转换Bean
     * @throws TemplateException 
     */
    public static Bean getBean(Map<String, TemplateModel> params)
            throws TemplateException {
        Bean bean = new Bean();
        
        Set<String> keySet = params.keySet();
        for (String key:keySet) {
            bean.put(key, getString(key, params));
        }
        return bean;
    }
    

    /**
     * 根据指定参数中获取数据
     * @param name - key
     * @param params - 模版参数
     * @return 参数值 (Long)
     * @throws TemplateException - freemarker template exception
     */
    public static Long getLong(String name, Map<String, TemplateModel> params)
            throws TemplateException {
        TemplateModel model = (TemplateModel) params.get(name);
        if (model == null) {
            return null;
        }
        if ((model instanceof TemplateScalarModel)) {
            String s = ((TemplateScalarModel) model).getAsString();
            if (StringUtils.isBlank(s)) {
                return null;
            }
            try {
                return Long.valueOf(Long.parseLong(s));
            } catch (NumberFormatException e) {
                throw new RuntimeException(name);
            }
        }
        if ((model instanceof TemplateNumberModel)) {
            return Long.valueOf(((TemplateNumberModel) model).getAsNumber().longValue());
        }
        throw new RuntimeException(name);
    }

    /**
     * 根据指定参数中获取数据
     * @param name - key
     * @param params - 模版参数
     * @return 参数值 (Int)
     * @throws TemplateException - freemarker template exception
     */
    public static Integer getInt(String name, Map<String, TemplateModel> params)
            throws TemplateException {
        TemplateModel model = (TemplateModel) params.get(name);
        if (model == null) {
            return 0;
        }
        if ((model instanceof TemplateScalarModel)) {
            String s = ((TemplateScalarModel) model).getAsString();
            if (StringUtils.isBlank(s)) {
                return 0;
            }
            try {
                return Integer.valueOf(Integer.parseInt(s));
            } catch (NumberFormatException e) {
                throw new RuntimeException(name);
            }
        }
        if ((model instanceof TemplateNumberModel)) {
            return Integer.valueOf(((TemplateNumberModel) model).getAsNumber().intValue());
        }
        throw new RuntimeException(name);
    }

    /**
     * 从环境变量中移除指定参数
     * 
     * @param env - 环境变量
     * @param params - 参数
     * @param origMap - ?
     * @throws TemplateException - freemarker template exception
     */
    public static void removeParamsFromVariable(Environment env, Map<String, TemplateModel> params,
            Map<String, TemplateModel> origMap) throws TemplateException {
        if (params.size() <= 0) {
            return;
        }
        for (String key : params.keySet()) {
            env.setVariable(key, (TemplateModel) origMap.get(key));
        }
    }

}
