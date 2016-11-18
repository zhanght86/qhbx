package com.rh.core.wfe.resource;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.rh.core.base.Bean;
import com.rh.core.org.DeptBean;
import com.rh.core.org.UserBean;
import com.rh.core.org.mgr.OrgMgr;
import com.rh.core.org.mgr.UserMgr;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.bean.SqlBean;
import com.rh.core.util.Constant;
import com.rh.core.util.JsonUtils;
import com.rh.core.wfe.WfAct;
import com.rh.core.wfe.def.WfNodeDef;

/**
 * 印章管理
 * @author ruaho_hdy
 *
 */
public class SealExtendBinder implements ExtendBinder {

	@Override
	public ExtendBinderResult run(WfAct currentWfAct, WfNodeDef nextNodeDef) {
		Bean dataBean = currentWfAct.getProcess().getServInstBean();
		String extCls = nextNodeDef.getStr("NODE_EXTEND_CLASS");
        String configStr = ""; //工作流扩展配置
        String[] classes = extCls.split(",,");
        if (classes.length == 2) {
            configStr = classes[1];
        }
        Bean configBean = JsonUtils.toBean(configStr);
        //获取总公司印章管理员角色code
        String sealRole = configBean.getStr("OA_SEAL_ROLE");
		//对应省级机构编码字符串
		String fromOdept = getFromOdept(getSplitArray(dataBean));
		//实例化结果资源
		ExtendBinderResult result = new ExtendBinderResult();
		//符合条件的用户code
		StringBuffer userids = new StringBuffer("");
		//判断是否存在符合条件的送交机构code
		if (StringUtils.isNotBlank(fromOdept)) {
			SqlBean sql = new SqlBean();
			//查询用印机构管理配置信息
			List<Bean> contList = ServDao.finds("OA_SEAL_ODEPT_GROUP", sql);
			//获取当前节点配置用户信息
			List<UserBean> nodeUserList = new ArrayList<UserBean>();
			getZgsUser(UserMgr.getUsersByRole(sealRole), nodeUserList);
			List<Bean> lastFilterUsers = new ArrayList<Bean>();
			getCompList(contList, nodeUserList, lastFilterUsers);
			//配置信息中存在符合节点权限的用户信息
			if (lastFilterUsers.size() > 0) {
				getFilterUserIds(lastFilterUsers, nodeUserList, userids, fromOdept);
			//配置信息中不存在符合节点权限的用户信息
			} else {
				getFilterUserIds(null, nodeUserList, userids, null);
			}
		}
		result.setUserIDs(userids.toString());
		return result;
	}
	
	/**
	 * 获取过滤之后的用户id字符串
	 * @param list 数据集合
	 * @param fromOdept 是否存在符合条条件的配置信息
	 * @param userids符合条件的用户id字符串
	 */
	private void getFilterUserIds(List<Bean> lastList, List<UserBean> nodeUserList, StringBuffer userids, String fromOdept) {
		boolean isExitUser = false; //是否存在符合当前送交机构条件的印章管理员
		if (StringUtils.isNotBlank(fromOdept)) {
			for (Bean b : lastList) {
				String odeptCode = b.getStr("ODEPT_CODE");
				if (odeptCode.contains(fromOdept)) {
					userids.append(b.getStr("USER_CODE")).append(",");
					isExitUser = true;
				}
			}
		}
		if (!isExitUser) {
			for (UserBean b : nodeUserList) {
				userids.append(b.getCode()).append(",");
			}
		}
	}
	
	/**
	 * 获取格式化之后的字符数组
	 * @param dataBean 数据对象
	 * @return 格式化之后的数组
	 */
	private String[] getSplitArray(Bean dataBean) {
		//先获取工作流中符合条件的用户信息，在此用户信息基础上进行过滤 ---未完成，需要与安安讨论
		String endStr = "^";
		String splitStr = "\\^";
		//获取用印所属单位
		DeptBean dept = OrgMgr.getDept(dataBean.getStr("S_ODEPT"));
		String codePath = dept.getCodePath();
		codePath = codePath.trim();
		if (codePath.endsWith(endStr)) {
			codePath = codePath.substring(0, codePath.length() - 1);
		}
		return codePath.split(splitStr);
	}
	
	/**
	 * 获取起草人对应省级层级机构编码
	 * @return 省级机构编码字符串
	 */
	private String getFromOdept(String[] codePathLevel) {
		int level = codePathLevel.length;
		String fromOdept = "";
		//存在层级
		if (level > 0) {
			//总公司以下
			if (level > 2) {
				fromOdept = codePathLevel[2];
				//总公司
			} else if (level == 2) {
				fromOdept = codePathLevel[1];
			}
		}
		return fromOdept;
	}
	
	/**
	 * 获取比对之后的数据集合
	 * @param contList 配置中的数据
	 * @param nodeUserList 节点权限的数据
	 * @param lastFilterUsers 最终数据
	 */
	private void getCompList(List<Bean> contList, List<UserBean> nodeUserList, List<Bean> lastFilterUsers){
		//过滤出配置信息中符合节点权限的用户信息
		for (Bean b : contList) {
			for (UserBean c : nodeUserList) {
				if (b.getStr("USER_CODE").equals(c.getCode())) {
					lastFilterUsers.add(b);
					break;
				}
			}
		}
	}
	
	/**
	 * 获取总公司用户信息
	 * @param users 用户列表
	 * @param list 返回list集合
	 */
	private void getZgsUser(List<UserBean> users, List<UserBean> list){
		for (UserBean user : users) {
			//总公司的用户
			if (user.getODeptLevel() == Constant.NO_INT) {
				list.add(user);
			}
		}
	}
}
