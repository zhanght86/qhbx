/*
 * Copyright (c) 2011 Ruaho All rights reserved.
 */
package com.rh.core.base.start;

import java.sql.Connection;
import java.sql.DatabaseMetaData;

import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.sql.DataSource;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.base.Context.DB_TYPE;
import com.rh.core.base.Context.DS;



/**
 * 数据源管理类，处理数据源的初始化和关闭。
 * 
 * @author Jerry Li
 * @version $Id$
 */
public class DsMgr {
	
	/**
	 * 初始化数据库连接池，支持多数据源。
	 */
	public void start() {
		try {
    		//获取数据源，支持多数据源
		    String jndiPrefix = Context.app(Context.SYS_PARAM_JNDI_PREFIX, "");
		    javax.naming.Context env;
		    if (jndiPrefix.length() > 0) {
		        env = (javax.naming.Context) new InitialContext().lookup(jndiPrefix);
		        jndiPrefix += "/";
		    } else {
		        env = (javax.naming.Context) new InitialContext();
		    }
    		String dsPrefix = Context.app(Context.SYS_PARAM_DATASOURCE_PREFIX, "jdbc");
    		NamingEnumeration<NameClassPair> namEnumList = env.list(dsPrefix);
    		String prefix = dsPrefix + "/";
    		int i = 0;
    		while (namEnumList.hasMore()) {
    		    NameClassPair bnd = namEnumList.next();
    		    String name = prefix + bnd.getName();
        		try {
        		    Bean dsBean = new Bean();
                    dsBean.set(DS.NAME, name);
                    dsBean.set(DS.FULL_NAME, jndiPrefix + name);
                    DataSource ds = (DataSource) env.lookup(name);
                    Connection conn = ds.getConnection();
                    DatabaseMetaData dbmd = conn.getMetaData();
                    String url = dbmd.getURL();
                    String userName = dbmd.getUserName();
                    if (url.indexOf("oracle") >= 0) {
                        dsBean.set(DS.DB_TYPE, DB_TYPE.ORACLE);
                    } else if (url.indexOf("sqlserver") >= 0) {
                        dsBean.set(DS.DB_TYPE, DB_TYPE.MSSQL);
                    } else if (url.indexOf("mysql") >= 0) {
                        dsBean.set(DS.DB_TYPE, DB_TYPE.MYSQL);
                    } else if (url.indexOf("db2") >= 0) {
                        dsBean.set(DS.DB_TYPE, DB_TYPE.DB2);
                    } else {
                        dsBean.set(DS.DB_TYPE, DB_TYPE.OTHER);
                    }
                    dbmd = null;
                    conn.close();
                    dsBean.set(DS.DS, ds);
                    dsBean.set(DS.USER_NAME, userName);
                    Context.setDSBean(name, dsBean); //放入应用级变量
                    if (i == 0) { //设置第一个为缺省数据源
                        Context.setDSBean("", dsBean); //将缺省数据源放入应用级变量
                    }
                    i++;
                    System.out.println("dsName(JNDI):" + name + " Url=" + url + "(" + userName + ") is OK!");
    		    } catch (Exception e) {
    		        System.out.println("dsName(JNDI):" + name + " ERROR! " + e.getMessage());
    		    }
    		}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 关闭连接池
	 */
	public void stop() {
		
	}
}