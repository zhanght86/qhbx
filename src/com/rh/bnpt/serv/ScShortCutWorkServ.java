package com.rh.bnpt.serv;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.comm.InfoServ;
import com.rh.core.org.UserBean;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.util.JsonUtils;
import com.rh.core.util.RequestUtils;

/**
 * 个人快捷工作配置处理类
 * 
 * @author ZJW
 * 
 */
public class ScShortCutWorkServ extends CommonServ {
	/**
	 * 获取快捷菜单主菜单
	 * 
	 * @param paramBean
	 *            参数
	 * @return OutBean
	 */
	public OutBean getMainMenu(ParamBean paramBean) {
		OutBean outBean = new OutBean();
		if (paramBean.isNotEmpty("PID")) {
			InfoServ info = new InfoServ();
			OutBean menuBean = info.menu(paramBean); // 从菜单权限获取的菜单，注会先从缓存中取
			List<Bean> menuBeans = menuBean.getList("TOPMENU"); // 菜单编码的BEANS
			// 个人菜单权限列表
			ArrayList<Bean> personMenus = new ArrayList<Bean>();
			// 个人菜单权限列表填充数据
			Bean tempBean;
			//先手动添加一个"OA首页的菜单"
			Bean oaHomeBean = ServDao.find("SY_COMM_MENU", "OA_HOME__zhbx");
			tempBean = new Bean();
			tempBean.set("MENU_ID", "OA_HOME__zhbx");
            tempBean.set("MENU_NAME", oaHomeBean.getStr("MENU_NAME"));
            tempBean.set("MENU_TYPE", oaHomeBean.getStr("MENU_TYPE"));
            tempBean.set("DS_MENU_FLAG", oaHomeBean.getStr("DS_MENU_FLAG"));
            try {
                tempBean.set("MENU_INFO", URLEncoder.encode("/sy/comm/page/page.jsp","utf-8"));
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            tempBean.set("DS_ICON", oaHomeBean.getStr("DS_ICON"));
            personMenus.add(tempBean);
			for (Bean bean : menuBeans) {
				tempBean = new Bean();
				tempBean.set("MENU_ID", bean.getStr("ID"));
				tempBean.set("MENU_NAME", bean.getStr("NAME"));
				tempBean.set("MENU_TYPE", bean.getInt("TYPE"));
				tempBean.set("DS_MENU_FLAG", bean.getInt("MENU"));
				try {
                    tempBean.set("MENU_INFO", URLEncoder.encode(bean.getStr("INFO"),"utf-8"));
                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
				tempBean.set("DS_ICON", bean.getStr("DSICON"));
				personMenus.add(tempBean);
			}
			 
			outBean.setData(personMenus);
		}
		return outBean;
	}
	/**
	 * 获取集成系统下虚拟菜单,会先取个人设置信息，再取权限菜单，然后整合
	 * 
	 * @param paramBean
	 *            参数
	 * @return OutBean
	 */
	public OutBean getShortCutWork(ParamBean paramBean) {
		UserBean userBean = Context.getUserBean();
		OutBean outBean = new OutBean();
		if (paramBean.isNotEmpty("PID")) {
			String select = "STCUT_ID,MENU_ID,MENU_TYPE,STCUT_MENU_ID,MENU_PID,MENU_NAME,"
					+ "MENU_INFO,DS_ICON,S_USER,STCUT_ISUSE,STCUT_ORDER,S_PUBLIC";
			paramBean.setSelect(select);
			paramBean.setWhere(paramBean.getWhere() != null ? paramBean.getWhere() + " and CODE_PATH like '%"
					+ paramBean.getStr("PID") + "%' and MENU_INFO is not null and S_USER='" + userBean.getId() + "'" : paramBean.getWhere());
			paramBean.setOrder("S_PUBLIC ASC,STCUT_ORDER");
			// 获取快捷菜单表中自己拥有的菜单列表
			List<Bean> lists = ServDao.finds(paramBean.getServId(), paramBean);
			String pid = paramBean.getStr("PID");
			String serv = paramBean.getServId(); // 快捷菜单的服务ID
			paramBean.clear();
			paramBean.set("PID", pid);
			InfoServ info = new InfoServ();
			OutBean menuBean = info.menu(paramBean); // 从菜单权限获取的菜单，注会先从缓存中取
			List<Bean> menuBeans = menuBean.getList("TOPMENU"); // 菜单编码的BEANS
			// 个人快捷菜单列表，只存ID
			ArrayList<String> personShortCuts = new ArrayList<String>();
			// 个人菜单权限列表
			ArrayList<String> personMenus = new ArrayList<String>();
			// 存储整合后的菜单集合
			ArrayList<Bean> personShortCutList = new ArrayList<Bean>();

			// 向个人快捷菜单列表中填充数据
			for (Bean bean : lists) {
				personShortCuts.add(bean.getStr("STCUT_MENU_ID"));
			}
			// 个人菜单权限列表填充数据
			for (Bean bean : menuBeans) {
			    if(bean.get("CHILD")!=null){
			        List<Bean> childBeans = bean.getList("CHILD");
			        for (Bean b : childBeans) {
			            personMenus.add(b.getStr("ID"));
                    }
			    }
				personMenus.add(bean.getStr("ID"));
			}

			Bean tmpb = new Bean();
			// 整合菜单数据过程，此时整合后的列表仅为个人快捷菜单的集合
			for (Bean bean : lists) {
				// 若菜单权限集合中不存在个人列表集合中的数据，则从个人菜单服务中删除该条数据
				if (!personMenus.contains(bean.getStr("MENU_ID"))) {
					tmpb.setId(bean.getId());
					ServDao.delete(serv, tmpb);
				} else if(bean.getStr("MENU_INFO").length()<=0){
				    tmpb.setId(bean.getId());
                    ServDao.delete(serv, tmpb);
				} else{ // 否则加入整合集合中
				    if("1".equals(bean.getStr("MENU_TYPE"))){
				        if(bean.getStr("MENU_INFO").indexOf(".list.do")>0){
				        //如果已经有list.do后缀，则不再单独添加    
				        }else{
				            bean.set("MENU_INFO", bean.getStr("MENU_INFO")+".list.do");
				        }
				    }
					personShortCutList.add(bean);
				}
			}
			Bean aclMenu = null;
			int order = 100;

			//取得管理员设置的公共菜单 此处用于，在管理员设置公共菜单后，某些用户尚未对快捷菜单进行过任何操作包括鼠标滑动查看
			ParamBean pubMenuParam = new ParamBean();
			pubMenuParam.setSelect("distinct STCUT_MENU_ID,STCUT_ORDER");
			pubMenuParam.setWhere(" and CODE_PATH like '%"
							+ pid
							+ "%' and S_PUBLIC='1'"
							+ " and S_USER in(select USER_CODE from SY_ORG_ROLE_USER_V "
							+ " where ROLE_CODE = 'RADMIN' and CMPY_CODE = '"
							+ userBean.getCmpyCode() + "')");
			pubMenuParam.setOrder("STCUT_ORDER ASC");
			List<Bean> pubMenus = ServDao.finds(serv, pubMenuParam);
			//List<String> pubMeanStrs = new ArrayList<String>();
			Map<String, Bean> pubMaps = new HashMap<String, Bean>();
			for (Bean bean : pubMenus) {
				//pubMeanStrs.add(bean.getStr("STCUT_MENU_ID"));
				pubMaps.put(bean.getStr("STCUT_MENU_ID"), bean);
			}

			// 将菜单权限集合加入到整合集合中
			for (Bean bean : menuBeans) {
			    //只将叶子节点加入整合集合中
			    List<Bean> childBeans = bean.getList("CHILD");
			    for (Bean childBean : childBeans) {
			        if (!personShortCuts.contains(childBean.get("ID"))) {
    	                if(childBean.getInt("TYPE")==1||childBean.getInt("TYPE")==2){
    
    	                    aclMenu = new Bean();
    	                    
    	                    aclMenu.set("MENU_ID", childBean.getStr("ID"));
    	                    aclMenu.set("MENU_TYPE", childBean.getInt("TYPE"));
    	                    aclMenu.set("STCUT_MENU_ID", childBean.getStr("ID"));
    	                    aclMenu.set("MENU_PID", childBean.getStr("PID"));
    	                    aclMenu.set("MENU_NAME", childBean.getStr("NAME"));
    	                    try {
    	                        if(childBean.getInt("TYPE")==1&&childBean.getStr("INFO").indexOf(".list.do")<0){
    	                            aclMenu.set("MENU_INFO", URLEncoder.encode(childBean.getStr("INFO")+".list.do","utf-8"));
    	                        }else{
    	                            aclMenu.set("MENU_INFO", URLEncoder.encode(childBean.getStr("INFO"),"utf-8"));
    	                        }
    	                    } catch (UnsupportedEncodingException e) {
    	                        e.printStackTrace();
    	                    }
    	                    aclMenu.set("DS_ICON", childBean.getStr("DSICON"));
    	                    aclMenu.set("S_USER", userBean.getCode());
    	                    if (pubMaps.containsKey(childBean.get("ID"))) {
    	                        aclMenu.set("STCUT_ISUSE", "1");
    	                        aclMenu.set("S_PUBLIC", "1");
    	                        aclMenu.set("STCUT_ORDER", pubMaps.get(childBean.get("ID")).get("STCUT_ORDER"));
    	                    } else {
    	                        aclMenu.set("STCUT_ISUSE", "2");
    	                        aclMenu.set("S_PUBLIC", "");
    	                        aclMenu.set("STCUT_ORDER", order);
    	                    }
    	                    tmpb = ServDao.save(serv, aclMenu);
    	                    aclMenu.set("STCUT_ID", tmpb.getId());
    	                    personShortCutList.add(aclMenu);
    	                    order = order + 10;
    	                }
	                }
	            }
				if (!personShortCuts.contains(bean.get("ID"))) {
				    if(bean.getStr("INFO").length()>0){
				        aclMenu = new Bean();
				        aclMenu.set("MENU_ID", bean.getStr("ID"));
				        
				        aclMenu.set("STCUT_MENU_ID", bean.getStr("ID"));
				        aclMenu.set("MENU_PID", bean.getStr("PID"));
				        aclMenu.set("MENU_NAME", bean.getStr("NAME"));
				        try {
				            aclMenu.set("MENU_INFO", URLEncoder.encode(bean.getStr("INFO"),"utf-8"));
				        } catch (UnsupportedEncodingException e) {
				            e.printStackTrace();
				        }
				        aclMenu.set("DS_ICON", bean.getStr("DSICON"));
				        aclMenu.set("S_USER", userBean.getCode());
				        if (pubMaps.containsKey(bean.get("ID"))) {
				            aclMenu.set("STCUT_ISUSE", "1");
				            aclMenu.set("S_PUBLIC", "1");
				            aclMenu.set("STCUT_ORDER", pubMaps.get(bean.get("ID")).get("STCUT_ORDER"));
				        } else {
				            aclMenu.set("STCUT_ISUSE", "2");
				            aclMenu.set("S_PUBLIC", "");
				            aclMenu.set("STCUT_ORDER", order);
				        }
				        tmpb = ServDao.save(serv, aclMenu);
				        aclMenu.set("STCUT_ID", tmpb.getId());
				        personShortCutList.add(aclMenu);
				        order = order + 10;
				    }
				}
			}
			outBean.setData(personShortCutList);
		}
		return outBean;
	}

	/**
	 * 个人设置保存处理
	 * 
	 * @param paramBean
	 *            参数 主要传递菜单MENU_ID，STCUT_ORDER、STCUT_ISUSER
	 */
	public void savePersonCutWorkBySys(ParamBean paramBean) {
		String submenuData = paramBean.getStr("submenuData");
		List<Bean> list = JsonUtils.toBeanList(submenuData);
		if (!list.isEmpty()) {
			Bean whereBean = new Bean();
			for (Bean bean : list) {
				bean.set("_PK_", bean.getStr("STCUT_ID"));
				bean.set("STCUT_MENU_ID", bean.getStr("MENU_ID"));
				bean.set("STCUT_ORDER", bean.getStr("STCUT_ORDER"));
				ServDao.save(paramBean.getServId(), bean);
				if (paramBean.isNotEmpty("isAdminOpt")) {
					Bean setBean = new Bean();
					if ("1".equals(bean.getStr("S_PUBLIC"))) {
						setBean.set("S_PUBLIC", "1");
						setBean.set("STCUT_ISUSE", bean.getStr("STCUT_ISUSE"));
						setBean.set("STCUT_ORDER", bean.getStr("STCUT_ORDER"));
						whereBean.set("_WHERE_", " and STCUT_MENU_ID='" + bean.getStr("MENU_ID") + "'");
						ServDao.updates(paramBean.getServId(), setBean, whereBean);
					} else {
						setBean.set("S_PUBLIC", "");
						setBean.set("STCUT_ISUSE", bean.getStr("STCUT_ISUSE"));
						setBean.set("STCUT_ORDER", bean.getStr("STCUT_ORDER"));
						whereBean.set("_WHERE_", " and STCUT_MENU_ID='" + bean.getStr("MENU_ID") + "'");
						ServDao.updates(paramBean.getServId(), setBean, whereBean);
					}
				}
			}
		}
	}
}
