package com.rh.bn.comm;

import com.rh.core.base.TipException;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServMgr;
import com.rh.core.util.Constant;

/**
 * 百年项目批量新增部门及处室处理类
 * @author tanyh 20161107
 *
 */
public class BnDeptAddServ extends CommonServ{

	/**
	 * 批量新增部门及处室
	 * @param paramBean 参数对象
	 * @return outBean 返回结果对象
	 */
	public OutBean batchGen(ParamBean paramBean) {
		OutBean outBean = new OutBean();
		// 所属机构
		if (paramBean.getStr("ODEPT_CODES").length() <= 0) {
			throw new TipException("请选择所属机构");
		}
		// 待创建的部门
		if (paramBean.getStr("TDEPT_NAMES").length() <= 0) {
			throw new TipException("请输入部门名称");
		}
		String[] oDeptCodes = paramBean.getStr("ODEPT_CODES").split(",");
		String[] tDeptNames = paramBean.getStr("TDEPT_NAMES").split(",");
		// 待创建的处室
		String[] deptNames = paramBean.getStr("DEPT_NAMES").split(",");
		if (oDeptCodes != null && oDeptCodes.length > 0) {
			for (String oDept : oDeptCodes) {
				for (String tDeptName : tDeptNames) {
					if (tDeptName != null && tDeptName.trim().length() > 0) {
						ParamBean param = new ParamBean();
						param.set("DEPT_NAME", tDeptName);
						param.set("DEPT_FULL_NAME", tDeptName);
						param.set("DEPT_PCODE", oDept);
						param.set("DEPT_TYPE", Constant.DEPT_TYPE_DEPT);
						OutBean result = ServMgr.act("SY_ORG_DEPT", ServMgr.ACT_SAVE, param);
						// 部门创建成功
						if (result.getId().length() > 0 && deptNames != null && deptNames.length > 0) {
							for (String deptName : deptNames) {
								if (deptName != null && deptName.trim().length() > 0) {
									ParamBean dept = new ParamBean();
									dept.set("DEPT_NAME", deptName);
									dept.set("DEPT_FULL_NAME", deptName);
									dept.set("DEPT_PCODE", result.getId());
									dept.set("DEPT_TYPE", Constant.DEPT_TYPE_DEPT);
									ServMgr.act("SY_ORG_DEPT", ServMgr.ACT_SAVE, dept);
								}
							}
						}
					}
				}
			}
		}
		outBean.setOk("创建成功");
		outBean.set("SUCCESS", "true");
		return outBean;
	}
}
