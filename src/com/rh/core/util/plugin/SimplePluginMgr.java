/*
 * Copyright (c) 2011 Ruaho All rights reserved.
 */
package com.rh.core.util.plugin;

import java.util.ArrayList;
import java.util.List;

import com.rh.core.util.Lang;

/**
 * 插件管理简单实现类
 * 主要为了满足根据引入包在运行期动态选择插件进行执行的需求。
 * 要求插件首先要实现业务要求的共同的接口或继承自共同的父类，同时要求插件必须实现本包的Plugin接口。
 * 
 * @param <T> 实现Plugin接口的类
 * @author Jerry Li
 * @version $Id$
 */
public class SimplePluginMgr<T> implements PluginMgr<T> {
    
	/** 存放插件list */
	private List<Plugin> plugList = new ArrayList<Plugin>();
	
	
	/**
	 * 构建体方法，传入多个plugin的类名，装载按照传入的优先级进行判断
	 * @param classNames	plugin类名
	 */
	@SuppressWarnings("unchecked")
	public SimplePluginMgr(String... classNames) {
		if (classNames != null) {
			for (String name : classNames) {
				loadPlugin((Class<? extends T>) Lang.loadClass(name));
			}
		}
	}
	
	/**
	 * 构建体方法，传入多个plugin的类，装载按照传入的优先级进行判断
	 * @param classes	plugin类
	 */
	public SimplePluginMgr(Class<? extends T>... classes) {
		if (classes != null) {
			for (Class<? extends T> plugClass : classes) {
				loadPlugin(plugClass);
			}
		}
	}
	
	/**
	 * 获取第一个可用的plugin
	 * @return 可用的plugin
	 */
	@SuppressWarnings("unchecked")
	public T get() {
		for (Plugin plug : plugList) {
			if (plug.canWork()) {
				return (T) plug;
			}
		}
		throw new RuntimeException("No Plugins!");
	}
	
	/**
	 * 获取可用的插件列表
	 * @return 可用plugin列表
	 */
	@SuppressWarnings("unchecked")
	public List<T> gets() {
		List<T> list = new ArrayList<T>();
		for (Plugin plugClass : plugList) {
			if (plugClass.canWork()) {
				list.add((T) plugClass);
			}
		}
		return list;
	}
	
	/**
	 * 装载插件
	 * @param pluginClass  插件类
	 */
	protected void loadPlugin(Class<? extends T> pluginClass) {
		try {
			plugList.add((Plugin) pluginClass.newInstance());
		} catch (Throwable e) {
			throw new RuntimeException(pluginClass.getName(), e);
		}
	}
 
}
