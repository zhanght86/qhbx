package com.rh.oa.aud;

import java.util.ArrayList;
import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.org.DeptBean;
import com.rh.core.org.mgr.OrgMgr;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.bean.SqlBean;
import com.rh.core.util.Constant;
import com.rh.core.util.Strings;

/**
 * 稽核审计工具类
 * 
 * @author wangchen
 */
public class AuditUtil {

    /**
     * 获取用户可稽核审计的机构字符串
     * @param audUser 稽核员
     * @return 机构字符串
     */
    public static String getAuditableOdeptCodes(String audUser) {
        List<Bean> auditList = getList(audUser);
        String odeptCodes = "";
        for (Bean aud : auditList) {
            odeptCodes = Strings.addValue(odeptCodes, aud.getStr("ODEPT_CODE"));
        }
        return odeptCodes;
    }
    
    /**
     * 获取用户可稽核审计的机构列表
     * @param audUser 稽核员
     * @return 机构列表
     */
    public static List<DeptBean> getAuditableOdeptList(String audUser) {
        List<Bean> auditList = getList(audUser);
        List<DeptBean> deptList = new ArrayList<DeptBean>();
        for (Bean aud : auditList) {
            deptList.add(OrgMgr.getDept(aud.getStr("ODEPT_CODE")));
        }
        return deptList;
    }

    /**
     * 内部方法：查出用户被授权机构列表
     * @param audUser 查询用户
     * @return 被授权机构列表
     */
    private static List<Bean> getList(String audUser) {
        SqlBean queryAuditBean = new SqlBean();
        queryAuditBean.and("USER_CODE", audUser);
        queryAuditBean.and("S_FLAG", Constant.YES_INT);
        queryAuditBean.orders("DEPT_LEVEL,DEPT_SORT");
        List<Bean> auditList = ServDao.finds("OA_AUDIT_MGR", queryAuditBean);
        return auditList;
    }
    
    /**
     * 检查查询的机构串是否有权限
     * @param odeptCodes 待查询机构串
     * @param auditableOdeptCodes 可稽核审计的机构字符串
     * @return 是/否
     */
    public static boolean checkAuth(String odeptCodes, String auditableOdeptCodes) {
        String[] odeptCodeArr = odeptCodes.split(Constant.SEPARATOR);
        String[] auditableOdeptCodeArr = auditableOdeptCodes.split(Constant.SEPARATOR);
//        for (String code : odeptCodeArr) {
//            if (auditableOdeptCodes.indexOf(code) < 0) {
//                return false;
//            }
//        }
        for (String code : odeptCodeArr) {
            DeptBean odept = OrgMgr.getDept(code);
            String codePath = odept.getCodePath();
            for (String auditableCode : auditableOdeptCodeArr) {
                if (codePath.indexOf(auditableCode) >= 0) {
                    return true;
                }
            }
        }
        return false;
    }  
}