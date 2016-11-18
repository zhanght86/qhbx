/*
 * Copyright (c) 2011 Ruaho All rights reserved.
 */
package com.rh.core.base.start;

import com.rh.core.comm.CacheMgr;
import com.rh.core.serv.ServMgr;

/**
 * 缓存装载类。
 * 
 * @author Jerry Li
 * @version $Id$
 */
public class CacheLoader extends ServMgr {
	
	/**
	 * 启动时自动装载缓存：
	 * 装载字典设定的所有缓存；
	 * 装载服务定义中类型为全部装载的缓存。
	 */
	public void start() {
        System.out.println("CACHE is OK!");
	}
	
	/**
	 * 关闭时时清除缓存
	 */
	public void stop() {
	    CacheMgr.destroyCache();
	}
}