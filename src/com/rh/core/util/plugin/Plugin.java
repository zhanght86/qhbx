/*
 * Copyright (c) 2011 Ruaho All rights reserved.
 */
package com.rh.core.util.plugin;

/**
 * 插件需要实现的接口类
 * 
 * @author Jerry Li
 * @version $Id$
 */
public interface Plugin {
	/**
	 * 插件是否能工作
	 * @return 能否工作
	 */
	boolean canWork();
}
