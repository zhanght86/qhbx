/*
 * Copyright (c) 2011 Ruaho All rights reserved.
 */
package com.rh.core.util.plugin;

import java.util.List;

/**
 * 插件管理接口类
 * @param <T> 实现此接口的类
 * @author Jerry Li
 * @version $Id$
 */
public interface PluginMgr<T> {
	
	/**
	 * 获取可用的插件
	 * @return	第一个可用的插件
	 */
	T get();
	
	/**
	 * 获取全部可用的插件
	 * @return	全部插件的list
	 */
	List<T> gets();

}
