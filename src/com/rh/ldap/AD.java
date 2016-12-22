package com.rh.ldap;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author chujie
 *
 */

public class AD {
	
	private String user;
	private String url;
	private String pasword;
	/** log */
    private static Log log = LogFactory.getLog(AD.class);

	/**
	 * 
	 * @param url AD服务器地址
	 * @param user AD管理员账号
	 * @param password AD管理员密码
	 */
	public AD(String url, String user, String password) {
		this.url = url;
		this.user = user;
		this.pasword = password;
	}

	/**
	 * get connection
	 * @return LdapContext
	 */
	public LdapContext getConn() {
		Hashtable<Object, Object> env = new Hashtable<Object, Object>();
		//JNDI Context工厂类
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		//认证类型
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		//管理员账号
		env.put(Context.SECURITY_PRINCIPAL, this.user);
		//管理员密码
		env.put(Context.SECURITY_CREDENTIALS, this.pasword);
		env.put(Context.REFERRAL, "follow");
		//LDAP 服务地址
		env.put(Context.PROVIDER_URL, this.url);
		env.put(Context.SECURITY_PROTOCOL, "ssl");
		env.put("java.naming.ldap.factory.socket", "com.rh.ldap.DummySSLSocketFactory");
		LdapContext ctx = null;
		try {
			ctx = new InitialLdapContext(env, null);
			log.info("-------ldap 认证成功----------");
		} catch (NamingException e) {
			throw new RuntimeException("AD：认证失败！", e);
		}
		return ctx;
	}

	/**
	 * close connection
	 * @param ctx LdapContext
	 */
	public void colseConn(LdapContext ctx) {
		try {
			ctx.close();
		} catch (NamingException e) {
			throw new RuntimeException("AD：认证失败", e);
		}
	}

	/**
	 * add
	 * 
	 * @param userDN 用户DN
	 * @param attrs attributes
	 * 
	 */
	public void create(String userDN, BasicAttributes attrs) {
		LdapContext ctx = this.getConn();
		try {
			ctx.createSubcontext(userDN, attrs);
		} catch (NamingException e) {
			throw new RuntimeException("AD：创建用户或者部门失败！", e);
		} finally {
			this.colseConn(ctx);
		}
	}
	
	
	/**
	 * modify 
	 * 
	 * @param userDN 用户DN
	 * @param mods an ordered sequence
	 */
	public void modify(String userDN, ModificationItem[] mods) {
		LdapContext ctx = this.getConn();
		try {
			ctx.modifyAttributes(userDN, mods);
		} catch (NamingException e) {
			throw new RuntimeException("AD：用户修改失败！", e);
		} finally {
			this.colseConn(ctx);
		}
	}

	/**
	 * 用于修改 ad attrabute user_name user_dept 
	 * @param oldName 
	 * @param newName 
	 */
	public void rename(String oldName, String newName) {
		LdapContext ctx = this.getConn();
		try {
			ctx.rename(oldName, newName);
		} catch (NamingException e) {
			throw new RuntimeException("AD：修改用户名称或者部门名称失败！", e);
		} finally {
			this.colseConn(ctx);
		}
	}
	
	/**
	 * delete
	 * 
	 * @param userDN 用户DN
	 */
	public void delete(String userDN) {
		LdapContext ctx = this.getConn();
		try {
			ctx.destroySubcontext(userDN);
		} catch (NamingException e) {
			throw new RuntimeException("AD：删除用户或者部门失败！", e);
		} finally {
			this.colseConn(ctx);
		}
	}
	
	
	/**
	 * 
	 * @param base object 例  OU=前海再保险,DC=qianhaire,DC=local
	 * @param filter the filter expression to use for the search; may not be null
	 * @param searchCtls the search controls
	 * @return searchResult
	 */
	public NamingEnumeration<SearchResult> search(String base, String filter, SearchControls searchCtls) {
		LdapContext ctx = this.getConn();
		NamingEnumeration<SearchResult> searchResult = null;
		try {
			 searchResult = ctx.search(base, filter, searchCtls);
		} catch (NamingException e) {
			throw new RuntimeException("AD：查询用户失败！", e);
		} finally {
			this.colseConn(ctx);
		}
		return searchResult;
	}
	
}
