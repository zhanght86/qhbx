package com.rh.ldap;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.ldap.LdapContext;

import org.junit.Test;

import com.rh.core.base.Bean;
import com.rh.core.base.TipException;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.util.DateUtils;

/**
 * 
 *	操作AD中账号的类
 */
public class ADUser {

	/**
	 * 实例化 AD
	 */
    
	private AD ad = new ADMgr().getAD();
	
	/**
	 * 
	 * @return list
	 * @throws Exception 
	 */
	@Test
	public void getAll() throws Exception {
		List<Bean> list = new ArrayList<Bean>();
/*		list = ad.search("OU=前海再保险,DC=qianhaire,DC=local", "objectClass=User", new String[] { "objectGUID",
				"sAMAccountName", "name", "distinguishedName", "mail", "title", "whenCreated", "whenChanged" });
*/	/*	list = ad.search("DC=qianhaire,DC=local",
				"(&(objectClass=top)(objectClass=organizationalPerson)(sAMAccountName=zhangs))", new String[] {
						"sAMAccountName", "name", "distinguishedName", "mail"});
		System.out.println(list);*/
		addUser();
	}

	/**
	 * 同步AD中用户数据
	 * @param list 
	 */
	public void synUser(List<Bean> list) {
		try {
			// IOUtils.write(JsonUtils.toJson(new ADOrg().getAll()), new FileOutputStream("d:/org.txt"));
			String name = "";
			String loginName = "";
			String id = "";
			String createDate = "";
			String changeDate = "";
			String dName = "";
			String userCode = "";
			String deptCode = "";
			String cmpyCode = "zhbx";
			List<Bean> beansList = new ArrayList<Bean>();
			for (Bean user : list) {

				System.out.println(user);
				Bean cnBean = new Bean();
				id = user.getStr("objectGUID");
				name = user.getStr("name");
				loginName = user.getStr("sAMAccountName");
				createDate = user.getStr("whenCreated");
				changeDate = user.getStr("whenChanged");
				dName = user.getStr("distinguishedName");
				cnBean.set("OBJECTGUID", id.substring(1, id.length() - 1));
				cnBean.set("SAMACCOUNTNAME", user.getStr("sAMAccountName"));
				cnBean.set("NAME", name);
				cnBean.set("DISTINGUISHEDNAME", dName);
				cnBean.set("MAIL", user.getStr("mail"));
				cnBean.set("TITLE", user.getStr("title"));
				cnBean.set("WHENCREATED", formate(createDate));
				cnBean.set("WHENCHANGED", formate(changeDate));

				int index = dName.indexOf(",");
				String pDname = dName.substring(index + 1, dName.length());

				System.out.println(pDname);

				// 根据 AD的 distinguishedName去查找部门编码，
				ParamBean paramBean = new ParamBean();
				paramBean.set("DISTINGUISHEDNAME", pDname);
				Bean adPbean = ServDao.find("PT_AD_OU", paramBean);
				if (adPbean != null) {
					cnBean.set("ADDEPTCODE", adPbean.getStr("DEPTCODE") + "*" + adPbean.getStr("OBJECTGUID"));
				}
				
				paramBean = new ParamBean();
				paramBean.set("CMPY_CODE", cmpyCode);
				paramBean.set("USER_LOGIN_NAME", loginName);
				Bean userBean = ServDao.find("SY_ORG_USER", paramBean);

				if (userBean != null) {
					userCode = userBean.getStr("USER_CODE");
					deptCode = userBean.getStr("DEPT_CODE");
				} else {
					userCode = "";
					deptCode = "";
				}
				cnBean.set("DEPTCODE", deptCode);
				cnBean.set("USERCODE", userCode);
				ServDao.save("PT_AD_USER", cnBean);
				// beansList.add(cnBean);
			}

			// ServDao.creates("PT_AD_USER", beansList);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 添加AD节点.
	 * 
	 * @param userDN 用户DN
	 * @param attrs attributes
	 * @return boolean 添加是否成功.
	 * @throws Exception 
	 * 
	 */
	public boolean createAdNode(String userDN, BasicAttributes attrs) throws Exception {
		LdapContext ctx = null;
		try {
			ctx = ad.getConn();
			ctx.createSubcontext(userDN, attrs);
		} catch (NamingException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			ad.colseConn(ctx);
		}
		return false;
	}


	/**
	 * 添加用户
	 * @param paramBean 用户Bean
	 * @return userCN
	 * @throws NamingException
	 * "(&(objectClass=top)(objectClass=organizationalPerson)(sAMAccountName="zhangs"))"; 
	 */
	@Test
	public void addUser() {
		String userCN = "CN=测试,OU=前海再保险,OU=测试,DC=qianhairen,DC=local";
				//getUserCn(paramBean);
		if (userCN.length() > 0) {
			BasicAttribute objclassSet = new BasicAttribute("objectclass");
			objclassSet.add("top"); 
			objclassSet.add("person"); 
			objclassSet.add("organizationalperson"); 
			objclassSet.add("user");
			
			BasicAttributes attrs = new BasicAttributes();
	        attrs.put(objclassSet);
	        attrs.put("sAMAccountName", "dglin");
	        attrs.put("cn", "测试");
	        attrs.put("displayName", "测试");
	        attrs.put("sn", "测");
	        attrs.put("givenName", "三");
	        attrs.put("userPrincipalName", "test@qianhaire.local");
	        try {
	        	//过滤条件
	        	String filter = "(&(objectClass=top)(objectClass=organizationalPerson)(sAMAccountName=zhangsan))";
	        	//查询属性
	        	String[] searchCtls = new String[] {"sAMAccountName", "name", "distinguishedName", "mail"};
	        	createAdNode(userCN, attrs);
				//List<Bean> searchresult = ad.search("DC=qianhaire,DC=local", filter , searchCtls);
	        	/*		if (searchresult.size() < 1) {
				} else {
					System.out.println("该用户已经存在");
				}*/
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

	// 修改用户
	public void modify(ParamBean paramBean) {
		String userCN = getUserCn(paramBean);
		if (userCN.length() > 0) {
			System.out.println("userCN===" + userCN);
			ModificationItem[] mods = new ModificationItem[1];
			/* 修改属性 */
			Attribute attr0 = new BasicAttribute("name", "lxh213");
			mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attr0);
			/* 修改属性 */
			try {
				modifyUser(userCN, mods);
			} catch (NamingException e) {
				e.printStackTrace();
				throw new TipException("修改用户失败" + e);
			}
		}
	}

	/**
	 * 修改
	 * 
	 * @return
	 * @throws NamingException
	 */
	public boolean modifyUser(String userCN, ModificationItem[] mods) throws NamingException {
		LdapContext ctx = null;
		try {
			ctx = ad.getConn();
			System.out.println("updating...\n");
			ctx.modifyAttributes(userCN, mods);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error: " + e.getMessage());
			return false;
		} finally {
			ad.colseConn(ctx);
		}
	}

	/**
	 * 根据参数bean获取 用户 CN
	 * 
	 * @param paramBean
	 *            参数bean
	 * @return 用户cn
	 */
	public String getUserCn(Bean paramBean) {
		String userCN = "";
		ParamBean ouBean = new ParamBean();
		ouBean.set("DEPTCODE", paramBean.getStr("DEPT_CODE"));
		List<Bean> list = ServDao.finds("PT_AD_OU", ouBean);
		if (list.size() > 0) {
			userCN = "CN=" + paramBean.getStr("USER_NAME") +","+ list.get(0).getStr("DISTINGUISHEDNAME");
		}
		return userCN;
	}

	/**
	 * 删除用户.
	 * 
	 * @param userDN
	 *            String 用户DN
	 * @return
	 * @throws NamingException
	 */
	public boolean delUser(String userDN) throws NamingException {
		LdapContext ctx = null;
		try {
			ctx = ad.getConn();
			ctx.destroySubcontext(userDN);
			return true;
		} catch (NamingException e) {
			System.err.println("Problem changing password: " + e);
		} catch (Exception e) {
			System.err.println("Problem: " + e);
		} finally {
			ad.colseConn(ctx);
		}
		return false;
	}

	public String formate(String timeStr) throws ParseException {
		Collection<String> DEFAULT_DATE_FORMATS = new ArrayList<String>();
		DEFAULT_DATE_FORMATS.add("yyyyMMddHHmmss");
		Date dt = DateUtils.parseDate(timeStr, DEFAULT_DATE_FORMATS);
		return DateUtils.formatDatetime(dt);
	}
}
