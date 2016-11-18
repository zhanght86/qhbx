package com.rh.oa.aud;

import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.base.TipException;
import com.rh.core.org.DeptBean;
import com.rh.core.org.mgr.OrgMgr;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.bean.SqlBean;
import com.rh.core.util.Constant;
import com.rh.core.util.Strings;

/**
 * 稽核审计查询服务类
 * 
 * @author wangchen
 */
public class AuditQuery extends CommonServ {

    @Override
    protected void beforeQuery(ParamBean paramBean) {
        super.beforeQuery(paramBean);
        paramBean.setSelectKeyWord(" /*+INDEX(SY_COMM_ENTITY IDX_SY_COMM_ENTITY_DATA )*/ ");
        
        String audWhere = "";
        String searchWh = paramBean.getStr("_searchWhere");
        String odeptCodes = paramBean.getStr("S_ODEPT");
        int hasSubOdeptFlag = paramBean.contains("HAS_SUB_ODEPT_FLAG") ? paramBean.getInt("HAS_SUB_ODEPT_FLAG") : 2;
        String currUserCode = Context.getUserBean().getCode();
        String auditableOdeptCodes = AuditUtil.getAuditableOdeptCodes(currUserCode);
        if (auditableOdeptCodes.isEmpty()) {
            searchWh = " and 1=2";
            paramBean.set("_searchWhere", searchWh);
            return;
        }
        if (odeptCodes.isEmpty()) {
            //odeptCodes = auditableOdeptCodes;
            searchWh = " and 1=2";
            paramBean.set("_searchWhere", searchWh);
            return;
        } else {
            if (!AuditUtil.checkAuth(odeptCodes, auditableOdeptCodes)) {
                throw new TipException("没有相应机构的查看权限");
            }
        }
        if (hasSubOdeptFlag == 1) {
            audWhere = getIncludeSubOdeptWhere(odeptCodes);
        } else {
            audWhere = getExcludeSubOdeptWhere(odeptCodes);
        }
        searchWh = searchWh + audWhere;
        paramBean.set("_searchWhere", searchWh);
    }
    
    /**
     * 生成不包含下级机构的where条件
     * @param odeptCodes 机构编码
     * @return where条件
     */
    private String getExcludeSubOdeptWhere(String odeptCodes) {
        odeptCodes = Strings.replace(odeptCodes, ",", "','");
        return " and QUERY_ODEPT in ('" + odeptCodes + "')";
    }

    /**
     * 生成包含下级机构的where条件
     * @param odeptCodes 机构编码
     * @return where条件
     */
    private String getIncludeSubOdeptWhere(String odeptCodes) {
        String[] odeptCodeArr = odeptCodes.split(Constant.SEPARATOR);
        if (odeptCodeArr.length == 1) {
            DeptBean odept = OrgMgr.getDept(odeptCodes);
            String codePath = odept.getCodePath();
            return " and QUERY_ODEPT in (select dept_code from sy_org_dept where code_path like '" + codePath
                    + "%' and DEPT_TYPE = 2 and S_FLAG = 1)";
        } else {
            String allOdeptCodes = "";
            for (String code : odeptCodeArr) {
                DeptBean odept = OrgMgr.getDept(code);
                String codePath = odept.getCodePath();
                SqlBean queryAllOdeptBean = new SqlBean();
                queryAllOdeptBean.andLikeRT("CODE_PATH", codePath);
                queryAllOdeptBean.and("DEPT_TYPE", 2);
                queryAllOdeptBean.and("S_FLAG", 1);
                List<Bean> allOdeptList = ServDao.finds("SY_ORG_DEPT", queryAllOdeptBean);
                for (Bean odeptBean : allOdeptList) {
                    allOdeptCodes = Strings.addValue(allOdeptCodes, odeptBean.getStr("DEPT_CODE"));
                }
            }
            allOdeptCodes = Strings.replace(allOdeptCodes, ",", "','");
            return " and QUERY_ODEPT in ('" + allOdeptCodes + "')";
        }
    }
}