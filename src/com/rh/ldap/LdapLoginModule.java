package com.rh.ldap;

import java.util.Hashtable;
import java.util.List;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rh.core.base.Bean;
import com.rh.core.base.TipException;
import com.rh.core.org.UserBean;
import com.rh.core.org.auth.login.AbstractLoginModule;
import com.rh.core.org.mgr.UserMgr;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServMgr;
import com.rh.core.util.EncryptUtils;
import com.rh.core.util.RequestUtils;
import com.rh.core.util.encoder.Base64;

/**
 * @author ayf
 * @version v1.0 创建时间： 类说明 用户登录 ldap验证类
 */
public class LdapLoginModule extends AbstractLoginModule {
	private static final String PARAM_LOGINNAME = "loginName";
	private static final String PARAM_PASSWORD = "password";
	// Ldap地址;
	private static final String LDAP_URL = com.rh.core.base.Context.getSyConf("LDAP_URL", "ldap://10.0.50.8");
	/** log */
    private static Log log = LogFactory.getLog(LdapLoginModule.class);
	
	@Override
	public UserBean authenticate(Bean paramBean) {
		Hashtable<String, String> env = null;
		LdapContext ctx = null;
		String loginName = paramBean.getStr("id");
		if(paramBean.getStr("id").equals("")){
			loginName = paramBean.getStr(PARAM_LOGINNAME);
		}
		log.debug("-------登录"+loginName+"用户名----------");
		String password = new String(Base64.decode(paramBean.getStr(PARAM_PASSWORD).getBytes()));
		UserBean userBean = null;

		if (loginName.length() <= 0 || password.length() <= 0) {
			throw new RuntimeException("认证参数错误，用户名：" + loginName + ";密码长度：" + password.length());
		}

		ParamBean midBean = new ParamBean();
		midBean.set("USER_LOGIN_NAME", loginName);
		List<Bean> datas = ServDao.finds("SY_ORG_USER", midBean);
		if (datas != null && datas.size() > 0) {
			Bean bean = datas.get(0);
			log.debug("-------登录"+bean+"开始----------");
			env = new Hashtable<String, String>();
			env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
			env.put(Context.PROVIDER_URL, LDAP_URL);
			env.put(Context.SECURITY_PRINCIPAL, loginName + "@"+ADMgr.DOMAIN); // 用户的dn
			env.put(Context.SECURITY_CREDENTIALS, password); // 密码
			env.put(Context.SECURITY_AUTHENTICATION, "simple");
			//特殊ip可以无秘密登陆
			userBean = UserMgr.getUser(bean.getStr("USER_CODE"));
            String adminIP = com.rh.core.base.Context.getSyConf("sy_nopwd_login_ips", "");
            boolean isTest = com.rh.core.base.Context.getSyConf("sy_istest_devlepment", false);
            String ipAddress = RequestUtils.getIpAddr(com.rh.core.base.Context.getRequest());
            boolean isNoPwdLogin = ipAddress.length()>0 && adminIP.contains(ipAddress);
            if(isNoPwdLogin  || isTest ){
            }else{
    			try {
    				ctx = new InitialLdapContext(env, null);
    				ctx.close();
    			} catch (NamingException e) {
    				e.printStackTrace();
    				throw new TipException("登陆失败：用户名或密码错误！");
    			}
    			// 登陆成功则把域用户密码同步到系统
    			password = EncryptUtils.encrypt(password,
    					com.rh.core.base.Context.getSyConf("SY_USER_PASSWORD_ENCRYPT", EncryptUtils.DES));
    			userBean.set("USER_PASSWORD", password);
    			ServDao.save(ServMgr.SY_ORG_USER, userBean);
            }
			return userBean;
		} else {
			throw new RuntimeException("系统中无此用户，请与系统管理员联系！");
		}
	}
}
