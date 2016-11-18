package com.rh.bn.imp;

import java.util.List;

import jxl.Cell;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.base.TipException;
import com.rh.core.org.UserBean;
import com.rh.core.org.mgr.UserMgr;
import com.rh.core.serv.ServDao;

/**
 * 用户导入处理类
 * @author tanyh 20161107
 *
 */
public class BnUserImp extends AbstractImp{

	@Override
	protected Bean prepareData(Cell[] cell, int index) {
		Bean user = new Bean();
		// 当前用户
		UserBean userBean = Context.getUserBean();
		if (cell == null || cell.length <= 0) {
			logList.add(createLogBean("", "", "第" + (index + 1) + "行为空行"));
			return null;
		}
		if (cell.length < 4) {
			logList.add(createLogBean("", "", "第" + (index + 1) + "行列数不够，应该为5列"));
			return null;
		}
		// 用户登录名
		String userLoginName = cell[0].getContents();
		if (userLoginName == null || userLoginName.trim().length() <= 0) {
			logList.add(createLogBean("", "", "第" + (index + 1) + "行，缺少用户ID"));
			return null;
		}
		try {
			// 判断登录名是否已存在
			UserBean userData = UserMgr.getUser(userLoginName, userBean.getCmpyCode());
			if (userData != null && userData.getId().length() > 0) {
				logList.add(createLogBean(userLoginName, "", "第" + (index + 1) + "行，用户ID已存在"));
				return null;
			}
			user.set("USER_LOGIN_NAME", userLoginName);
		} catch (TipException e) {
			// 用户登录名不存在
			user.set("USER_LOGIN_NAME", userLoginName);
		}
		// 用户名
		String userName = cell[1].getContents();
		if (userName == null || userName.trim().length() <= 0) {
			logList.add(createLogBean(userLoginName, "", "第" + (index + 1) + "行，缺少用户姓名"));
			return null;
		}
		user.set("USER_NAME", userName);
		// 用户所属部门或处室
		String deptPath = cell[2].getContents();
		if (deptPath == null || deptPath.trim().length() <= 0) {
			logList.add(createLogBean(userLoginName, "", "第" + (index + 1) + "行，缺少用户所属机构、部门或处室信息"));
			return null;
		}
		// 此单元格的数据已处理过，则直接获取即可
		if (sameData.containsKey(deptPath)) {
			user.set("DEPT_CODE", sameData.get(deptPath));
		} else {
			String[] depts = deptPath.split("-->");
			String pDeptCode = "";
			String deptCode = "";
			for (String dept : depts) {
				List<Bean> deptList = ServDao.finds("SY_ORG_DEPT_ALL", " and DEPT_NAME='" + dept + "'" + 
						(pDeptCode.length() > 0 ? " and DEPT_PCODE='" + pDeptCode + "'" : ""));
				// 未找到部门或机构信息
				if (deptList == null || deptList.size() <= 0) {
					logList.add(createLogBean(userLoginName, userName, "第" + (index + 1) + "行，部门或机构信息：" + dept + ",未找到"));
					return null;
				} else if (deptList.size() > 1) {
					// 找到多条机构或部门信息
					logList.add(createLogBean(userLoginName, userName, "第" + (index + 1) + "行，部门或机构信息：" + dept + ",找到多条"));
					return null;
				}
				pDeptCode = deptList.get(0).getId();
				deptCode = pDeptCode;
			}
			if (deptCode.length() <= 0) {
				// 找到的机构或部门信息不完整
				logList.add(createLogBean(userLoginName, userName, "第" + (index + 1) + "行，部门或机构信息：" + depts[depts.length - 1] + ",未找到"));
				return null;
			}
			user.set("DEPT_CODE", deptCode);
			sameData.put(deptPath, deptCode);
		}
		// 用户邮箱
		String userMail = cell[3].getContents();
		if (userMail == null || userMail.trim().length() <= 0) {
			logList.add(createLogBean(userLoginName, userName, "第" + (index + 1) + "行，缺少用户邮箱信息"));
			return null;
		}
		user.set("USER_EMAIL", userMail);
		// 用户全拼
		user.set("USER_SPELLING", userLoginName);
		// 用户密码
		user.set("USER_PASSWORD", "ad32a707be4a4e9ffd5c53d06c546aa1");// 用户初始密码为aq12wsde3
		// 用户状态，在职
		user.set("USER_STATE", 1);
		user.set("S_FLAG", 1);
        //所属公司
		user.set("CMPY_CODE", userBean.getCmpyCode());
        //创建人为当前用户
		user.set("S_USER", userBean.getCode());
		return user;
	}

	@Override
	protected String getServId() {
		return "BN_SY_ORG_USER";
	}

	@Override
	protected String getOptName() {
		return "用户导入";
	}
	
	
}
