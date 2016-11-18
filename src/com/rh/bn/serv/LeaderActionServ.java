package com.rh.bn.serv;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.org.UserBean;
import com.rh.core.org.mgr.OrgMgr;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.PageBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.util.DateUtils;

/**
 * 领导活动日常查看的serv
 * @author AoDanni
 * */

public class LeaderActionServ extends CommonServ {
	private static final Log log = LogFactory.getLog(LeaderActionServ.class);
	/**领导活动查看用户服务  **/
	public static final String BN_LDR_QUERY_USER = "BN_LDR_QUERY_USER";

	/**领导活动查看关系服务 **/
	public static final String BN_LDR_QUERY_RELAT = "BN_LDR_QUERY_RELAT";

	/**领导活动记录服务 **/
	public static final String OA_LDR_ACTION = "OA_LDR_ACTION";

	/**用户角色关联服务 **/
	public static final String  SY_ORG_ROLE_USER = "SY_ORG_ROLE_USER";

	/**用户服务 **/
	public static final String  SY_ORG_USER = "SY_ORG_USER";

	/**
	 * 获取当前选择角色下的领导列表*
	 *  @param  paramBean 传入参数
	 * @return 传出参数
	 * */
	public OutBean getLeaderList(ParamBean paramBean) {
		OutBean outBean = new OutBean();
		UserBean userBean = Context.getUserBean();
		// 根据当前登录人 获取queryID
		List<Bean> querylist = ServDao.finds(BN_LDR_QUERY_USER,
				" and ROLE_CODE  in( select ROLE_CODE from SY_ORG_ROLE_USER " 
				+ "where  USER_CODE like '%" + userBean.getCode() + "%' )");
		// 判断是否只有一个唯一的queryID
		List<Bean> allUserLists = new ArrayList<Bean>();
		for  (int j = 0; j < querylist.size(); j++) {
			// 根据queryID获取可查看的角色 以及部门
			List<Bean> relatBeans = ServDao.finds(
					BN_LDR_QUERY_RELAT,
					" and QUERY_ID = '" + querylist.get(j).getStr("QUERY_ID")
					+ "' and ROLE_CODE='"
					+ paramBean.getStr("ROLE_CODE") + "'");
			if (relatBeans.size() == 1) {
				List<Bean> userLists = null;
				//当前角色下是否选择了部门
				if (relatBeans.get(0).getStr("DEPT_CODE") != "" && !"".equals(relatBeans.get(0).getStr("DEPT_CODE"))) {
					String[] depts = relatBeans.get(0).getStr("DEPT_CODE").split(",");
					// 根据角色查找用户
					for (int i = 0; i < depts.length; i++) {
						userLists = ServDao.finds(SY_ORG_USER,
								" and USER_CODE in ( select user_code from "
										+ SY_ORG_ROLE_USER
										+ " where  CMPY_CODE='zhbx' and ROLE_CODE ='"
										+ paramBean.getStr("ROLE_CODE")
										+ "' and DEPT_CODE ='" + depts[i]
												+ "')  ORDER BY USER_SORT ");
						if (userLists != null) {
							allUserLists.addAll(userLists);
						}
					}
				} else { //没选择部门则查询当前角色下所有人员
					userLists = ServDao.finds(SY_ORG_USER,
							" and USER_CODE in ( select user_code from "
									+ SY_ORG_ROLE_USER
									+ " where  CMPY_CODE='zhbx' and ROLE_CODE ='"
									+ paramBean.getStr("ROLE_CODE") + "') ORDER BY USER_SORT");
					if (userLists != null) {
						allUserLists.addAll(userLists);
					}
				}
				//去除allUserLists中的重复数据
				for (int n = 0; n < allUserLists.size() - 1; n++) {  
					for (int m = allUserLists.size() - 1; m > n; m--) {  
						if (allUserLists.get(m).equals(allUserLists.get(n))) {  
							allUserLists.remove(m);  
						}   
					}   
				}  
			}
		}
		//分页的代码
		PageBean page =  new PageBean();
		page.set("SHOWNUM", paramBean.getStr("SHOWNUM")); //从参数中获取需要取多少条记录，如果没有则取所有记录
		page.set("NOWPAGE", paramBean.getStr("NOWPAGE"));  //从参数中获取第几页，缺省为第1页
		int count = allUserLists.size();
		int showNum = Integer.parseInt(paramBean.getStr("SHOWNUM"));
		if (!page.contains("ALLNUM") || page.getAllNum() <= 0) {  //如果有总记录数就不再计算
			if ((page.getInt("NOWPAGE") == 1) && (count < showNum)) { //数据量少，无需计算分页
				page.set("PAGES", 1);
				page.set("ALLNUM", count);
			} else {
				int pages = 0;
				if (count > 0) {
					if (count % page.getInt("SHOWNUM") == 0) {
						pages = count / page.getInt("SHOWNUM");
					} else {
						pages = count / page.getInt("SHOWNUM") + 1;
					}
				}
				page.set("PAGES", pages);
				page.set("ALLNUM", count);
			}
		}
		outBean.setCount(page.getInt("ALLNUM")); //设置为总记录数
		outBean.setPage(page);
		List<Bean> showUserLists = new ArrayList<Bean>();
		for (int i  = 0; i < allUserLists.size(); i++) {
		    Bean userInfo = allUserLists.get(i);
		    if(!Context.getSyConf("BN_ORG_ZODEPT_CODE","0001B210000000000BU3").equals(userInfo.get("ODEPT_CODE"))){
                userInfo.set("ODEPT_NAME",OrgMgr.getDept(userInfo.getStr("ODEPT_CODE")).getStr("DEPT_NAME"));
		    }
			int rowNum = Integer.parseInt(allUserLists.get(i).getStr("_ROWNUM_")) + 1;
			if (rowNum >= showNum * (page.getInt("NOWPAGE") - 1) + 1 && rowNum <= showNum * page.getInt("NOWPAGE")) {
				showUserLists.add(allUserLists.get(i));
			}
		}
		outBean.setData(showUserLists);
		return outBean;
	}

	/**
	 * 获取领导的活动情况
	 * @param  paramBean 传入参数
	 * @return 传出参数
	 * */
	public OutBean getLeaderActons(ParamBean paramBean) {
		OutBean outBean = new OutBean();
		//本周开始时间
		String startDate = paramBean.getStr("BEGIN_DATE");
		//本周结束时间
		String endDate = paramBean.getStr("END_DATE");
		//拼写查询sql
		StringBuffer sb = new StringBuffer("");
		sb.append(" and instr(','||ldr_id||',','," + paramBean.getStr("LEADER_CODE") + ",')>0");
		sb.append(" and (");
		//活动时间开始于周一之前，结束于周日之前
		sb.append("(BEGIN_TIME <='" + startDate + " 00:00:00' and END_TIME >='" 
		        + startDate + " 00:00:00' and END_TIME <='" + endDate + " 23:59:59')");
		//活动时间开始于周一之前，结束于周日之后
		sb.append(" or (BEGIN_TIME <='" + startDate + " 00:00:00' and END_TIME >='" + endDate + " 23:59:59')");
		//活动时间开始于周一之后、周日之前，结束于周日之前
		sb.append(" or (BEGIN_TIME >='" + startDate + " 00:00:00' and END_TIME <='" + endDate + " 23:59:59')");
		//活动时间开始于周一之后、周日之前，结束于周日之后
		sb.append(" or (BEGIN_TIME >='" + startDate + " 00:00:00' and BEGIN_TIME <='" 
		        + endDate + " 23:59:59' and END_TIME >='" + endDate + " 23:59:59')");
		sb.append(") order by BEGIN_TIME");
		List<Bean> actionList = ServDao.finds(OA_LDR_ACTION, sb.toString());
		// 根据领导每项活动时间，处理前端渲染情况
		List<Bean> resultList = new ArrayList<Bean>();
		for (Bean action : actionList) {
			//活动开始时间
			String startTime = action.getStr("BEGIN_TIME") + ":00";
			//活动结束时间
			String endTime = action.getStr("END_TIME") + ":00";
			if (startTime.trim().length() <= 0 || endTime.trim().length() <= 0) {
				//异常数据，不处理
				log.error("领导活动：" + action.getStr("TITLE") + "，开始或结束时间不能为空。");
				continue;
			}
			if (DateUtils.isBefore(endTime, startTime)) { //开始时间晚于结束时间
				//异常数据，不处理
				log.error("领导活动：" + action.getStr("TITLE") + "，开始时间不能晚于结束时间。");
				continue;
			}
			if (DateUtils.isBefore(startTime, startDate + " 00:00:00")) { //活动开始时间早于周一，则取周一0点
				startTime = startDate + " 00:00:00";
			}
			if (DateUtils.isBefore(endDate + " 23:30:00", endTime)) { //活动时间晚于周日，则取周日24点
			    endTime = endDate + " 23:30:00";
			}
			long b = DateUtils.getDateFromString(startTime).getTime()  
			        - DateUtils.getDateFromString(startDate + " 00:00:00").getTime();
			long beginBlock = 1 + b / (30 * 60 * 1000);
			action.set("BEGIN_BLOCK", beginBlock);
			long e = DateUtils.getDateFromString(endDate + " 23:59:59").getTime()
			         - DateUtils.getDateFromString(endTime).getTime();
			long endBlock = 336 - e / (30  * 60 * 1000);
			action.set("END_BLOCK", endBlock);
			resultList.add(action);
		}
		outBean.setData(resultList);
		return outBean;
	}


	/**
	 * 保存领带活动查看的角色部门信息
	 * @param paramBean 参数bean
	 * @return 返回参数
	 */
	public Bean saveItem(ParamBean paramBean) {
		String[] idArr = paramBean.getStr("idArray").split(",");

		String[] nameArr = paramBean.getStr("nameArray").split(",");

		String queryId = paramBean.getStr("QUERY_ID");

		for (int i = 0; i < idArr.length; i++) {
			Bean bean = new Bean();
			bean.set("QUERY_ID", queryId).set("ROLE_CODE", idArr[i]).set("ROLE_NAME", nameArr[i]);
			ServDao.create(BN_LDR_QUERY_RELAT, bean);
		}
		return paramBean;
	}


	/**
	 * 保存角色关联的部门信息
	 * 
	 * @param paramBean 参数bean
	 * @return 参数bean
	 */
	public Bean saveRoleDept(Bean paramBean) {

		paramBean.setId(paramBean.getStr("RE_ID"));

		ServDao.update(BN_LDR_QUERY_RELAT, paramBean);

		return paramBean;
	}
	
	/**
     * 转换参与人员的用户ID为NAME
     * 
     * @param paramBean 参数参与人员ID
     * @return 参数bean
     */
	
	
	   public OutBean changeOtherUserToName(Bean paramBean) {
	       OutBean outBean = new OutBean();
	       String[] userIds = ((String) paramBean.get("_OTHERUSER_")).split(",");
	       String names = "";
	       
	       outBean.set("TITLE", paramBean.get("_TITLE_"));
	       outBean.set("ADDRESS", paramBean.get("_ADDRESS_"));
	       outBean.set("ACT_TYPE", paramBean.get("_ACT_TYPE_"));
	       outBean.set("CONTENT", paramBean.get("_CONTENT_"));
	       outBean.set("BEGIN_TIME", paramBean.get("_BEGIN_TIME_"));
	       outBean.set("END_TIME", paramBean.get("_END_TIME_"));
	       outBean.set("NUM",  paramBean.get("_NUM_"));
	       
	       if (userIds.length > 0) {
	           for (int i = 0; i < userIds.length; i++) {
	               List<Bean> namesBean =  ServDao.finds(SY_ORG_USER, " and USER_CODE = '" + userIds[i] + "'");
	               if (namesBean.size() > 0) {
	                   String name = (String) namesBean.get(0).get("USER_NAME");
    	               if (i == userIds.length - 1) {
    	                   names += name;
    	               } else {
    	                   names += name + ",";
    	               }
	               }
	           }
	       }
	       outBean.set("NAMES", names);
	       return outBean;
	    }
}
