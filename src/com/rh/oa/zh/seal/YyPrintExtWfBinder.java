package com.rh.oa.zh.seal;

import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.base.TipException;
import com.rh.core.org.DeptBean;
import com.rh.core.org.mgr.OrgMgr;
import com.rh.core.util.Strings;
import com.rh.core.wfe.WfAct;
import com.rh.core.wfe.def.WfNodeDef;
import com.rh.core.wfe.resource.ExtendBinder;
import com.rh.core.wfe.resource.ExtendBinderResult;

/**
 * 
 * @author wangchen
 *
 */
public class YyPrintExtWfBinder implements ExtendBinder {

	/**
	 * 指定打印机构的指定角色可以打印
	 * @param thisWfAct 当前节点实例
	 * @param nextNodeDef 下个节点定义
	 * @return 扩展匹配的结果
	 */
	public ExtendBinderResult run(WfAct thisWfAct, WfNodeDef nextNodeDef) {
		Bean thisBean = thisWfAct.getProcess().getServInstBean();
		String yyDate = thisBean.getStr("YY_DATE");
		
		//未盖章禁止送交打印
        if (yyDate.isEmpty()) {
            throw new TipException("文件请先盖章！");
        }
		
		String printOdeptCode = thisBean.getStr("YY_PRINTUSER_ODEPT");
		String printRoleCode = Context.getSyConf("OA_GW_PRINT_ROLE", "");
		if (printOdeptCode.isEmpty()) {
		    printRoleCode = "";
		}
		
		//临时处理方法
        List<DeptBean> list = OrgMgr.getChildDepts(thisBean.getStr("S_CMPY"), printOdeptCode);
        if (list.size() > 0) {
            String deptStr = "";
            for (DeptBean dept : list) {
                deptStr = Strings.addValue(deptStr, dept.getCode());
            }
            printOdeptCode = deptStr;
        }
		
		
		ExtendBinderResult result = new ExtendBinderResult();
		result.setDeptIDs(printOdeptCode);
		result.setRoleCodes(printRoleCode);
		result.setUserIDs("");
		result.setBindRole(false);
		
		return result;
	}
}
