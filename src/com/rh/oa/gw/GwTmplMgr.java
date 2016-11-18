package com.rh.oa.gw;

import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.org.CmpyBean;
import com.rh.core.org.DeptBean;
import com.rh.core.org.mgr.OrgMgr;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServMgr;
import com.rh.core.serv.ServUtils;
import com.rh.core.serv.bean.SqlBean;
import com.rh.core.util.Constant;
import com.rh.oa.gw.util.GwConstant;

/**
 * 公文模版管理类
 * 
 * @author zhaozhenxing
 *
 */
public class GwTmplMgr {
    /**
     * 根据公文模版主键获取模版定义信息
     * @param tmplId 公文模版主键（与公文类的服务主键一致）
     * @return 公文模版定义信息
     */
    public static Bean getTmplByid(String tmplId) {
        ParamBean tmplParamBean = new ParamBean();
        tmplParamBean.setId(tmplId);
        Bean tmplBean = ServMgr.act(GwConstant.OA_GW_TMPL, "byid", tmplParamBean);
        

        return tmplBean;
    } 
    
    /**
     * 
     * @param tmplId 模板ID
     * @param odeptCode 替换模板名称中的变量时，使用的机构CODE
     * @return 公文模板数据对象，模板的显示标题（TMPL_TITLE）已被替换过。
     */
    public static Bean getTmpl(String tmplId, String odeptCode) {
        
        Bean tmplBean = getTmplByid(tmplId);
        if (tmplBean == null) {
            return null;
        }

        DeptBean orgBean = OrgMgr.getDept(odeptCode);
        if (orgBean == null) {
            return tmplBean;
        }
        
        Bean bean = orgBean.copyOf();
        CmpyBean cmpyBean = OrgMgr.getCmpy(orgBean.getCmpyCode());
        bean.set("CMPY_FULLNAME", cmpyBean.getFullname());

        // 中华保险专用处理
        if (orgBean.getLevel() == 1) { // 集团公司
            bean.set("CMPY_FULLNAME", "");
            bean.set("DEPT_NAME", bean.getStr("DEPT_FULL_NAME"));
        } else if (orgBean.getLevel() == 2) { // 财险公司
            bean.set("DEPT_NAME", "");
        }
        
        tmplBean.set("TMPL_TITLE", ServUtils.replaceValues(tmplBean.getStr("TMPL_TITLE")
                , ServMgr.SY_ORG_DEPT, bean)); 
        
        return tmplBean;
    }
    
    /**
     * 通过部门ID和模板ID获取机关代字
     * @param deptId 部门ID
     * @param tmplId 模板ID
     * @return 返回所有机关代字
     */
    public static List<Bean> getYearCodeList(String deptId, String tmplId) {
        Bean bean = GwTmplMgr.getTmplByid(tmplId);
       
        SqlBean sql = new SqlBean();
        sql.and("TMPL_CODE", tmplId);
        if (bean.getInt("CODE_STYLE") == 1) {
            String[] deptIds = { deptId, OrgMgr.getDept(deptId).getODeptCode() };
            sql.andIn("DEPT_ID", deptIds);
        } else if (bean.getInt("CODE_STYLE") == 2) { // 机构
            sql.and("S_ODEPT", OrgMgr.getDept(deptId).getODeptCode());
        } else {
            sql.and("DEPT_ID", "null");
        }
        
        sql.set(Constant.PARAM_SELECT, " distinct CODE_NAME ID, CODE_NAME NAME, CODE_SORT CODE_SORT");
        sql.orders("CODE_SORT");
        
        return ServDao.finds("OA_GW_CODE_V", sql);
    }
    
    /**
     * 
     * @param tmplPrefix 模板前缀
     * @return 取得指定前缀的公文模板
     */
    public static List<Bean> getTmplList(String tmplPrefix) {
//        UserBean user = Context.getUserBean();
        SqlBean sql = new SqlBean();
        sql.andLikeRT("TMPL_CODE", tmplPrefix);
//        sql.and("S_CMPY", user.getCmpyCode());

        return ServDao.finds(GwConstant.OA_GW_TMPL, sql);
    }
    
}
