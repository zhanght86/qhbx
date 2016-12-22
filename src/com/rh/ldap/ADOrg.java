package com.rh.ldap;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.rh.core.base.Bean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.util.DateUtils;

/**
 * 
 * @author chujie
 *
 */
public class ADOrg {
	
	/**
	 * 
	 * @return List
	 * @throws Exception 
	 */
	public List<Bean> getAll() throws Exception {
		List<Bean> list = new ArrayList<Bean>();
		AD ad = new ADMgr().getAD();
/*		list = ad.search("OU=前海再保险,DC=qianhaire,DC=local", "objectClass=organizationalUnit", new String[] {
				"objectGUID", "name", "distinguishedName", "whenCreated", "whenChanged" });
*/		return list;
	}
	
	

	public String formate(String timeStr) throws ParseException {
		Collection<String> DEFAULT_DATE_FORMATS = new ArrayList<String>();
		DEFAULT_DATE_FORMATS.add("yyyyMMddHHmmss");
		Date dt = DateUtils.parseDate(timeStr, DEFAULT_DATE_FORMATS);
		return DateUtils.formatDatetime(dt);
	}

	/**
	 * 获取、同步AD数据
	 * 
	 * @throws Exception
	 *             例外
	 */
	public void synTest() throws Exception {
		try {
			// IOUtils.write(JsonUtils.toJson(new ADOrg().getAll()), new FileOutputStream("d:/org.txt"));

			List<Bean> list = null;//new ADOrg().getAll();
			String name = "";
			String id = "";
			String createDate = "";
			String changeDate = "";
			String dName = "";
			String deptCode = "";
			List<Bean> beansList = new ArrayList<Bean>();
			for (Bean org : list) {

				System.out.println(org);
				Bean ouBean = new Bean();
				id = org.getStr("objectGUID");
				name = org.getStr("name");
				createDate = org.getStr("whenCreated");
				changeDate = org.getStr("whenChanged");
				dName = org.getStr("distinguishedName");
				ouBean.set("OBJECTGUID", id.substring(1, id.length() - 1));
				ouBean.set("NAME", org.getStr("name"));
				ouBean.set("DISTINGUISHEDNAME", dName);
				ouBean.set("WHENCREATED", formate(createDate));
				ouBean.set("WHENCHANGED", formate(changeDate));

	
					String pCode = "";

					int index = dName.indexOf(",");
					String pDname = dName.substring(index + 1, dName.length());

					System.out.println(pDname);
					// 根据 AD的 distinguishedName去查找组织机构编码，若能找到则 添加到 第三表中 ，若查不到，则 在组织机构表中添加一条
					ParamBean paramBean = new ParamBean();
					paramBean.set("DISTINGUISHEDNAME", pDname);
					Bean adPbean = ServDao.find("PT_AD_OU", paramBean);
					Bean orgBean = new Bean();
					if (adPbean != null) {
						pCode = adPbean.getStr("DEPTCODE");
						paramBean = new ParamBean();
						paramBean.set("DEPT_PCODE", pCode);
						paramBean.set("DEPT_NAME",name);
						orgBean = ServDao.find("SY_ORG_DEPT", paramBean);
						// TODO 如果 没有 找到父或者没有 找到 该机构 如何处理
						if (orgBean != null) {
							deptCode = orgBean.getStr("DEPT_CODE");
						}else{
							deptCode = "";
						}
					}else{
						deptCode = "";
					}
				ouBean.set("DEPTCODE", deptCode);
				ServDao.save("PT_AD_OU", ouBean);
				//beansList.add(ouBean);
			}
			//ServDao.creates("PT_AD_OU", beansList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
