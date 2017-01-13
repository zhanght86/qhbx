package com.rh.ldap;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.LdapContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.org.UserBean;
import com.rh.core.org.mgr.UserMgr;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServMgr;
import com.rh.core.serv.dict.DictMgr;
import com.rh.core.util.EncryptUtils;

/**
 * 
 *	操作AD中账号的类
 */
public class ADMgr {
	
	/** log */
	private static Log log = LogFactory.getLog(ADMgr.class);

	/**JNDI Context工厂类*/
	public static final String INITIAL_CONTEXT_FACTORY = "com.sun.jndi.ldap.LdapCtxFactory";
	
	/**认证类型*/
	public static final String SECURITY_AUTHENTICATION = "simple";
	
	public static final String REFERRAL = "follow";
	
	public static final String SECURITY_PROTOCOL = "ssl";
	
	public static final String BASE_OU = "DC=qianhaire,DC=local";
	   public static final String DOMAIN  = "qianhaire.local";
	/**
	 * ldap配置信息
	 */
	private String LDAP_URL = "ldap://10.0.50.8";//Context.getSyConf("LDAP_URL", "ldap://10.0.50.8");
	private String LDAP_ADMINISTRATOR ="adminoa@qianhaire.local"; //Context.getSyConf("LDAP_ADMINISTRATOR", "adminsadmin").concat("@qianhaire.local");
	private String ldapAdminPsd = "4ed71005301d78129348fc0d1dc1f624";//Context.getSyConf("LDAP_ADMINISTRATOR_PASSWORD", "20160325@qhr");//加密后的密码4ed71005301d78129348fc0d1dc1f624
	private String LDAP_PASSWORD = EncryptUtils.decrypt(new String(ldapAdminPsd.getBytes()),	 EncryptUtils.DES);
//	private String LDAP_URL =  Context.getSyConf("LDAP_URL", "ldap://10.0.50.8");
//    private String LDAP_ADMINISTRATOR =Context.getSyConf("LDAP_ADMINISTRATOR", "adminsadmin").concat("@qianhaire.local");
//   private String ldapAdminPsd = Context.getSyConf("LDAP_ADMINISTRATOR_PASSWORD", "4f1e2efa627e1aa36f65536863cb23f9");//加密后的密码
//   private String LDAP_PASSWORD = EncryptUtils.decrypt(new String(ldapAdminPsd.getBytes()),    EncryptUtils.DES);
	/**
	 * 实例化 AD
	 */
	private  AD ad = new AD(LDAP_URL, LDAP_ADMINISTRATOR, LDAP_PASSWORD);
    
	/**
	 * 添加AD账号.
	 * 
	 * @param paramBean 用户Bean
	 * @return boolean 添加是否成功.
	 * 
	 */
	public OutBean createAccount(ParamBean paramBean) {
		OutBean outBean = new OutBean();
		hasAccount(paramBean);
		String sn = paramBean.getStr("USER_NAME").substring(0, 1);
		String givenName = paramBean.getStr("USER_NAME").substring(1);
		String mail = paramBean.getStr("USER_EMAIL");
		String deptCode = paramBean.getStr("DEPT_CODE");
		Bean deptBean = ServDao.find("SY_ORG_DEPT", deptCode);
		String department = DictMgr.getName("SY_ORG_DEPT_ALL", deptBean.getStr("TDEPT_CODE"));
		String company = DictMgr.getName("SY_ORG_DEPT_ALL", deptBean.getStr("ODEPT_CODE"));		
		log.debug("-------ldap创建用户---公司----=="+company+"---");
		log.debug("-------ldap创建用户----部门-----=="+department+"---");
		BasicAttribute objclassSet = new BasicAttribute("objectclass");
		objclassSet.add("top"); 
		objclassSet.add("person"); 
		objclassSet.add("organizationalperson"); 
		objclassSet.add("user");
		BasicAttributes attrs = new BasicAttributes();
        attrs.put(objclassSet);
        attrs.put("sAMAccountName", paramBean.getStr("USER_LOGIN_NAME"));
        attrs.put("cn", paramBean.getStr("USER_NAME"));
        attrs.put("sn", sn);
        attrs.put("givenName", givenName);
        attrs.put("displayName", paramBean.getStr("USER_NAME"));
        attrs.put("company", company);
        attrs.put("department", department);
		log.debug("-------ldap创建用户---cn----=="+company+"---");
		log.debug("-------ldap创建用户----sn-----=="+department+"---");	
		log.debug("-------ldap创建用户----givenName-----=="+department+"---");
		Bean cmpyBean = ServDao.find("SY_ORG_DEPT", deptBean.getStr("ODEPT_CODE"));
		log.debug("-------ldap创建用户----displayName-----=="+paramBean.getStr("USER_NAME")+"---");
		log.debug("-------ldap创建用户----userPrincipalName-----=="+cmpyBean.getStr("DEPT_SRC_TYPE1")+"---");			
		if (!cmpyBean.getStr("DEPT_SRC_TYPE1").equals("")) {
        	attrs.put("userPrincipalName", paramBean.getStr("USER_LOGIN_NAME") + cmpyBean.getStr("DEPT_SRC_TYPE1"));
        }
        if (!mail.equals("")) {
        	attrs.put("mail", mail);
        }
		try {
			log.info("-------AD:创建账号-----" + paramBean.getStr("USER_AD_OU") + "-----");
			ad.create(paramBean.getStr("USER_AD_OU"), attrs);
		} catch (Exception e) {
			throw new RuntimeException("AD域创建账号失败！", e);
		}
		return outBean.setOk("AD：数据添加成功！");
	}

	/**
	 * 根据登陆名查询用户
	 * @param paramBean 
	 * @return outBean 没有则返回 null
	 */
	public void hasAccount(ParamBean paramBean) {
		if (Context.getSyConf("AD_FIND_USER", 2) == 1) {
			String name = paramBean.getStr("USER_LOGIN_NAME");
			String filter = "(&(objectCategory=person)(objectClass=organizationalPerson)(sAMAccountName=" + name + "))";
			String[] reqAtts = new String[] {"sAMAccountName", "cn", "name"};
			SearchControls searchCtls = new SearchControls();
			searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
			searchCtls.setReturningAttributes(reqAtts); // 设置返回属性集
			NamingEnumeration<SearchResult> serarchResult = ad.search(BASE_OU, filter, searchCtls);
			try {
				if (serarchResult.hasMore()) {
					SearchResult result = serarchResult.next();
					NamingEnumeration<? extends Attribute> attrs = result.getAttributes().getAll();
					String attrsJson = "{";
					for (int i = 0; attrs.hasMore(); i++) {
						if (i == 0) {
							attrsJson += attrs.next();
						} else {
							attrsJson += "," + attrs.next();
						}
					}
					attrsJson += "}";
					throw new RuntimeException("AD：该用户 " + attrsJson+ "已经存在");
				}
			} catch (NamingException e) {
				log.info("AD：查找没有该用户！");
			}
		}
	}
	
	/**
	 * 获取用户CN
	 * @param userName  must contain (userName)
	 * @param deptCode  must contain (DEPT_CODE)
	 * @return userCN
	 */
	
	public String getUserCN(String userName, String deptCode) {
		ParamBean param = new ParamBean(ServMgr.SY_ORG_DEPT, ServMgr.ACT_FINDS);
		param.setId(deptCode);
		Bean deptBean = ServMgr.act(param).getDataList().get(0);
		String userCN = "CN=" + userName + "," + deptBean.getStr("DEPT_MEMO");
		return userCN;
	}

	
	/**
	 * 修改ad用户密码并设置永不过期
	 * @param paramBean 
	 * @return outBean
	 */
	public OutBean modifyPsd(ParamBean paramBean) {
		OutBean outBean = new OutBean();
		String pwd = paramBean.getStr("USER_PASSWORD");
		ModificationItem[] mods = new ModificationItem[2];
        // Replace the "unicdodePwd" attribute with a new value
        // Password must be both Unicode and a quoted string
		String newQuotedPassword = "\"" + pwd + "\"";
        byte[] newUnicodePassword = null;
		try {
			newUnicodePassword = newQuotedPassword.getBytes("UTF-16LE");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("AD：密码修改异常!", e);
		}
		mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
				new BasicAttribute("unicodePwd", newUnicodePassword));
		mods[1] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
				new BasicAttribute("userAccountControl", Integer.toString(66048)));
		ad.modify(paramBean.getStr("USER_AD_OU"), mods);
		return outBean.setOk();
	}
	
	/**
	 * 修改用户
	 * @param paramBean 
	 * @return OutBean
	 */
	public OutBean modify(ParamBean paramBean) {
		OutBean outBean = new OutBean();
		//先判断是否修改登录名 如果修改则查找是否重复
		if (!paramBean.getStr("USER_LOGIN_NAME").equals("")) {
			hasAccount(paramBean);
		}
		//接着改部门，姓名 后续属性
		Bean oldBean = paramBean.getSaveOldData();
		String deptCode = paramBean.getStr("DEPT_CODE");
		if (!(deptCode.equals("") && paramBean.getStr("USER_NAME").equals(""))) {
				String oldName = oldBean.getStr("USER_AD_OU");
				ad.rename(oldName, paramBean.getStr("USER_AD_OU"));
		}

		if (!paramBean.getStr("USER_PASSWORD").equals("")) {
			modifyPsd(paramBean);
		}
		
		//恢复用户
		if (!paramBean.getStr("S_FLAG").equals("")) {
			RestoreUser(paramBean);
		}
		
		List<Attribute> list = new ArrayList<Attribute>();
		
		if (!deptCode.equals("")) {
	        Bean deptBean = ServDao.find(ServMgr.SY_ORG_DEPT, paramBean.getStr("DEPT_CODE"));
	        String department = ServDao.find(ServMgr.SY_ORG_DEPT, deptBean.getStr("TDEPT_CODE")).getStr("DEPT_NAME");
	        String company = ServDao.find(ServMgr.SY_ORG_DEPT, deptBean.getStr("ODEPT_CODE")).getStr("DEPT_NAME");
			Attribute attr = new BasicAttribute("department", department);
			list.add(attr);
			Attribute attr1 = new BasicAttribute("company", company);
			list.add(attr1);
		}
		
		if (!paramBean.getStr("USER_LOGIN_NAME").equals("")) {
			Attribute attr = new BasicAttribute("sAMAccountName", paramBean.getStr("USER_LOGIN_NAME"));
			list.add(attr);
			
			if(deptCode.equals("")) {
				deptCode = oldBean.getStr("DEPT_CODE");
			}
			Bean deptBean = ServDao.find("SY_ORG_DEPT", deptCode);
			Bean cmpyBean = ServDao.find("SY_ORG_DEPT", deptBean.getStr("ODEPT_CODE"));
			if (!cmpyBean.getStr("DEPT_SRC_TYPE1").equals("")) {
				Attribute attr1 = new BasicAttribute("userPrincipalName", paramBean.getStr("USER_LOGIN_NAME") + cmpyBean.getStr("DEPT_SRC_TYPE1"));
				list.add(attr1);
			}			
		}
		
		if (!paramBean.getStr("USER_NAME").equals("")) {
			String sn = paramBean.getStr("USER_NAME").substring(0, 1);
			String givenName = paramBean.getStr("USER_NAME").substring(1);
			Attribute attr = new BasicAttribute("sn", sn);
			Attribute attr1 = new BasicAttribute("givenName", givenName);
			Attribute attr2 = new BasicAttribute("displayName", paramBean.getStr("USER_NAME"));
			list.add(attr);
			list.add(attr1);
			list.add(attr2);
		}
		
		if (!paramBean.getStr("USER_EMAIL").equals("")) {
			Attribute attr = new BasicAttribute("mail", paramBean.getStr("USER_EMAIL"));
			list.add(attr);
		}
		
		/* 修改属性 */
		if (list.size() > 0) {
			ModificationItem[] mods = new ModificationItem[list.size()];
			for (int i = 0; i < list.size(); i++) {
				mods[i] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, list.get(i));
			}
			log.info("-------AD:修改账号-----" + paramBean.getStr("USER_AD_OU") + "-----");
			ad.modify(paramBean.getStr("USER_AD_OU"), mods);
		}
		outBean.setOk("AD：数据修改成功！");
		return outBean;
	}

	/**
	 * 修改用户
	 * @param paramBean 
	 * @return OutBean
	 */
	public OutBean modifyAttr(ParamBean paramBean) {
		ModificationItem[] mods = new ModificationItem[2];
        Bean deptBean = ServDao.find(ServMgr.SY_ORG_DEPT, paramBean.getStr("DEPT_CODE"));
        String department = ServDao.find(ServMgr.SY_ORG_DEPT, deptBean.getStr("TDEPT_CODE")).getStr("DEPT_NAME");
        String company = ServDao.find(ServMgr.SY_ORG_DEPT, deptBean.getStr("ODEPT_CODE")).getStr("DEPT_NAME");
		mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("company", company));
		mods[1] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("department", department));
		ad.modify(paramBean.getStr("USER_AD_OU"), mods);
		return new OutBean().setOk();
	}
	

	/**
	 * 删除用户.(慎用)
	 * 
	 * @param paramBean 
	 * @return outBean
	 */
	public OutBean delUser(ParamBean paramBean) {
		OutBean outBean = new OutBean();
		log.info("-------AD:删除账号-----" + paramBean.getStr("USER_AD_OU") + "-----");
		ad.delete(paramBean.getStr("USER_AD_OU"));
		return outBean.setOk("AD：删除用户成功！");
	}

	/**
	 * 禁用用户.
	 * 
	 * @param paramBean 
	 * @return outBean
	 */
	public OutBean falseDelUser(ParamBean paramBean) {
		OutBean outBean = new OutBean();
		ModificationItem[] mods = new ModificationItem[1];
		mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
				new BasicAttribute("userAccountControl", Integer.toString(514)));
		log.info("-------AD:禁用账号-----" + paramBean.getStr("USER_AD_OU") + "-----");
		ad.modify(paramBean.getStr("USER_AD_OU"), mods);
		return outBean.setOk("AD：禁用用户成功！");
	}

	
	/**
	 * 禁用用户.
	 * 
	 * @param paramBean 
	 * @return outBean
	 */
	public OutBean RestoreUser(ParamBean paramBean) {
		OutBean outBean = new OutBean();
		Bean bean = ServDao.find(paramBean.getServId(), paramBean.getId());
		ModificationItem[] mods = new ModificationItem[1];
		mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
				new BasicAttribute("userAccountControl", Integer.toString(66048)));
		log.info("-------AD:恢复账号-----" + bean.getStr("USER_AD_OU") + "-----");
		ad.modify(bean.getStr("USER_AD_OU"), mods);
		return outBean.setOk("AD：恢复用户成功！");
	}
	
	
	/**
	 * 添加AD组织机构.
	 * 
	 * @param paramBean 用户Bean
	 * @return boolean 添加是否成功.
	 * 
	 */
	public OutBean createDept(ParamBean paramBean) {
		OutBean outBean = new OutBean();
		String ou = paramBean.getStr("DEPT_NAME");
		
		BasicAttribute objclassSet = new BasicAttribute("objectclass");
		objclassSet.add("top"); 
		objclassSet.add("organizationalUnit"); 
		
		BasicAttributes attrs = new BasicAttributes();
        attrs.put(objclassSet);
        attrs.put("ou", ou);
        log.info("-------AD:添加部门-----" + paramBean.getStr("DEPT_MEMO") + "-----");
		ad.create(paramBean.getStr("DEPT_MEMO"), attrs);
		return outBean.setOk("AD：添加部门成功！");
	}

	/**
	 * 添加AD组织机构.
	 * 
	 * @param paramBean 用户Bean
	 * @return boolean 添加是否成功.
	 * 
	 */
	public OutBean createCmpy(ParamBean paramBean) {
		OutBean outBean = new OutBean();
		String ou = paramBean.getStr("CMPY_NAME");
		
		BasicAttribute objclassSet = new BasicAttribute("objectclass");
		objclassSet.add("top"); 
		objclassSet.add("organizationalUnit"); 
		
		BasicAttributes attrs = new BasicAttributes();
        attrs.put(objclassSet);
        attrs.put("ou", ou);
        log.info("-------AD:创建公司-----" + paramBean.getStr("CMPY_DESC") + "-----");
		ad.create(paramBean.getStr("CMPY_DESC"), attrs);
		return outBean.setOk("AD：创建公司成功！");
	}
	
	/**
	 * 修改组织机构
	 * @param paramBean 
	 * @return OutBean
	 */
	public OutBean modifyDept(ParamBean paramBean) {
		OutBean outBean = new OutBean();
		Bean oldBean = paramBean.getSaveOldData();
		if ((!paramBean.getStr("DEPT_NAME").equals("")) || (!paramBean.getStr("DEPT_PCODE").equals(""))) {
				String oldName = oldBean.getStr("DEPT_MEMO");
				log.info("-------AD:修改部门-----" + paramBean.getStr("DEPT_MEMO") + "-----");
				ad.rename(oldName, paramBean.getStr("DEPT_MEMO"));
		}

		return outBean.setOk("AD：修改部门成功！");
	}
	
	/**
	 * 删除部门.
	 * 
	 * @param paramBean 
	 * @return outBean
	 */
	public OutBean delDept(ParamBean paramBean) {
		OutBean outBean = new OutBean();
		String deptCn = paramBean.getDeleteDatas().get(0).getStr("DEPT_MEMO");
		if (!deptCn.equals("")) {
			log.info("-------AD:删除部门-----" + deptCn + "-----");
			ad.delete(deptCn);
		} else {
			throw new RuntimeException("AD：deptCn is null");
		}
		return outBean.setOk("AD：删除部门成功！");
	}
	
	/**
	 * 修改公司（目前只支持修改公司名称）
	 * @param paramBean 
	 * @return OutBean
	 */
	public OutBean modifyCmpy(ParamBean paramBean) {
		OutBean outBean = new OutBean();
		Bean oldBean = paramBean.getSaveOldData();
		if (!paramBean.getStr("CMPY_NAME").equals("")) {
				String oldName = oldBean.getStr("CMPY_DESC");
				log.info("-------AD:修改公司-----" + paramBean.getStr("CMPY_DESC") + "-----");
				ad.rename(oldName, paramBean.getStr("CMPY_DESC"));
		}

		return outBean.setOk("AD：修改公司成功！");
	}
	
	
	/**
	 * 删除公司.
	 * 
	 * @param paramBean 
	 * @return outBean
	 */
	public OutBean delCmpy(ParamBean paramBean) {
		OutBean outBean = new OutBean();
		String cmpyCn = paramBean.getDeleteDatas().get(0).getStr("CMPY_DESC");
		if (!cmpyCn.equals("")) {
			log.info("-------AD:删除公司-----" + cmpyCn + "-----");
			ad.delete(cmpyCn);
		} else {
			throw new RuntimeException("AD：cmpyCn is null");
		}
		return outBean.setOk("AD：删除公司成功！");
	}
	
	public AD getAD(){
	    return ad;
	}
	
	public static void main(String[] args){
        LdapContext ctx = null;
        ADMgr admgr = new ADMgr();
        try {
            ctx = admgr.ad.getConn();
            AD ad= admgr.ad;
            NamingEnumeration<SearchResult>  list = null;
//                  list = ad.search("OU=前海再保险,DC=qianhaire,DC=local", "objectClass=User", new String[] { "objectGUID",
//                            "sAMAccountName", "name", "distinguishedName", "mail", "title", "whenCreated", "whenChanged" });
//                SearchControls sdf = new SearchControls();
//                sdf.setReturningAttributes(new String[] {
//                       "name"});
//                      list = ad.search("DC=qianhaire,DC=local",
//                            "objectClass=User",sdf );
//                    System.out.println(list);
//                    while(list.hasMoreElements()){
//                        SearchResult sr = list.nextElement();
//                        System.out.println(sr.getAttributes());
//                    }
                    
                    SearchControls searchCtls = new SearchControls();  
                    searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);  
                    String addTime =  "20161229000000.0Z";
                    String searchFilter = "(&(objectCategory=person)(objectClass=user)(name=*)(whenChanged>="+
                            addTime
                            +"))";  
                    String searchBase = "OU=前海再保险,DC=qianhaire,DC=local";  
                    String returnedAtts[] = {"memberOf","whenChanged","whenCreated","name","title","mail",
                          "sAMAccountName", "name", "distinguishedName", "telephoneNumber", "homePhone",  
                           "mobile"};  
                    searchCtls.setReturningAttributes(returnedAtts);  
                    NamingEnumeration<SearchResult> answer = ctx.search(searchBase, searchFilter,searchCtls);  
                    List<Bean> aBeanList = new ArrayList<Bean>();
                    while (answer.hasMoreElements()) {  
                        SearchResult sr = (SearchResult) answer.next();  
                        System.out.println( "username ："+sr.getName()+",attributes:"+sr.getAttributes()+",samaccountname:"+sr.getAttributes().get("samaccountname").getID());  
                        String loginName = "",name ="",mail="",officePhone="",mobile="",telephonenumber="";
                        if(sr.getAttributes().get("samaccountname").toString().contains(":")){
                                loginName = sr.getAttributes().get("samaccountname").toString().substring(sr.getAttributes().get("samaccountname").toString().indexOf(":")+1);
                                name = sr.getName().substring(sr.getName().indexOf("CN="),sr.getName().indexOf(","));
                                if(sr.getAttributes().get("telephonenumber").toString().length()>0){
                                    telephonenumber = sr.getAttributes().get("telephonenumber").toString().substring(sr.getAttributes().get("telephonenumber").toString().indexOf(":"));
                                }
                                if(sr.getAttributes().get("mobile").toString().length()>0){
                                    mobile = sr.getAttributes().get("mobile").toString().substring(sr.getAttributes().get("mobile").toString().indexOf(":"));
                                }
                                if(sr.getAttributes().get("mail").toString().length()>0){
                                    mail = sr.getAttributes().get("mail").toString().substring(sr.getAttributes().get("mail").toString().indexOf(":"));
                                }
                                Bean uBean = new Bean();
                                List<Bean>  userBeanList = ServDao.finds("SY_ORG_USER", " and USER_LOGIN_NAME = "+loginName+"'");
                                if(userBeanList.size()==0){
                                    uBean.set("USER_NAME", name);
                                    uBean.set("USER_OFFICE_PHONE", telephonenumber);
                                    uBean.set("USER_MOBILE", mobile);
                                    uBean.set("USER_MAIL", mail);
                                    aBeanList.add(uBean);
                                    log.info("同步域用户："+sr.getAttributes()+"准备添加成功。");
                                }else if(userBeanList.size()==1){
                                    uBean = userBeanList.get(0);
                                    uBean.set("USER_NAME", name);
                                    uBean.set("USER_OFFICE_PHONE", telephonenumber);
                                    uBean.set("USER_MOBILE", mobile);
                                    uBean.set("USER_MAIL", mail);
                                    ServDao.update("SY_ORG_USER", uBean);
                                    log.info("同步域用户成功："+sr.getAttributes()+"修改成功。");
                                }else{
                                    //记录失败信息
                                    log.error("同步域用户信息失败【"+loginName+"】在OA系统中不唯一");
                                }
                        }else{//记录失败的信息
                            //记录失败信息
                            log.error("获取域用户信息登录名失败，【"+ "username ："+sr.getName()+",attributes:"+sr.getAttributes()+",samaccountname:"+sr.getAttributes().get("samaccountname").getID()+"】在域系统中没有获取到");
                        }
                       
                    } 
                    if(aBeanList.size()>0){
                    ServDao.creates("SY_ORG_USER", aBeanList);
                    log.info("同步域用户成功："+aBeanList.size()+"个用户添加成功。");
                    }
        } catch (Exception e) {
            log.error("同步域用户失败：", e);
        } finally {
            admgr.ad.colseConn(ctx);
        }
	}
}
