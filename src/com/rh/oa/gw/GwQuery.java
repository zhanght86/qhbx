package com.rh.oa.gw;

import java.util.ArrayList;
import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServMgr;
import com.rh.core.util.Constant;
import com.rh.core.util.JsonUtils;

/**
 * 公文查询
 *
 */
public class GwQuery extends CommonServ {
    /**
     * 
     * @param paramBean 参数Bean 
     * @return 公文类型列表
     */
    public OutBean getTmplTypeList(ParamBean paramBean) {
        List<Bean> typeTmplList = new ArrayList<Bean>();
        
        //查询公文类型
        Bean queryTypeBean = new Bean();
        queryTypeBean.set("S_CMPY", Context.getUserBean().getCmpyCode());
        String queryTypeSql = " and SERV_ID like 'OA_GW_TYPE_%'";
        
        queryTypeBean.set(Constant.PARAM_WHERE, queryTypeSql);
        
        List<Bean> servBeanList = ServDao.finds(ServMgr.SY_SERV, queryTypeBean);
        for (Bean servBean: servBeanList) { //根据类型查公文模板
            Bean rtnTypeBean = new Bean();

            String gwType = servBean.getStr("SERV_ID");
            
            Bean queryTmplBean = new Bean();
            queryTmplBean.set("S_CMPY", Context.getUserBean().getCmpyCode());
            String queryTmplSql = " and SERV_PID = '" + gwType + "'";
            
            queryTmplBean.set(Constant.PARAM_SELECT, "SERV_ID, SERV_NAME");
            
            queryTmplBean.set(Constant.PARAM_WHERE, queryTmplSql);
            
            List<Bean> tmplServList = ServDao.finds(ServMgr.SY_SERV, queryTmplBean);

            rtnTypeBean.set("GW_TYPE", gwType);
            
            rtnTypeBean.set("TYPE_NAME", servBean.getStr("SERV_NAME"));
            
            rtnTypeBean.set("tmplList", tmplServList);
            
            typeTmplList.add(rtnTypeBean);
        }

        OutBean outBean = new OutBean();
        outBean.setData(JsonUtils.toJson(typeTmplList));
        outBean.setOk();
        return outBean;
    }
    
    
    /**
     * 查询前添加查询条件
     * 
     * @param paramBean 
     */
    public void beforeQuery(ParamBean paramBean) {
        paramBean.set("_extWhere", addFieldFilter(paramBean).toString());
        paramBean.setOrder("GW_BEGIN_TIME desc");
    }
    
    /**
     * 
     * @param paramBean 参数Bean
     * @return 查询条件
     */
    private StringBuilder addFieldFilter(ParamBean paramBean) {
        StringBuilder strWhere = new StringBuilder(" "); 
        if (paramBean.isNotEmpty("GW_TITLE")) { //标题
            strWhere.append(" and GW_TITLE like '%");
            strWhere.append(paramBean.getStr("GW_TITLE"));
            strWhere.append("%'");
        }
        if (paramBean.isNotEmpty("GW_CODE")) { //文件编号 hdy 2013-5-29 10:59
            strWhere.append(" and GW_CODE like '%");
            strWhere.append(paramBean.getStr("GW_CODE"));
            strWhere.append("%'");
        }
        if (paramBean.isNotEmpty("TMPL_TYPE_CODE")) { //文件类型，如：收文、发文
            strWhere.append(" and TMPL_TYPE_CODE = '");
            strWhere.append(paramBean.getStr("TMPL_TYPE_CODE"));
            strWhere.append("'");
        }
        if (paramBean.isNotEmpty("TMPL_CODE")) { //文件子类型，如：公司发文
            strWhere.append(" and TMPL_CODE = '");
            strWhere.append(paramBean.getStr("TMPL_CODE"));
            strWhere.append("'");
        }
        if (paramBean.isNotEmpty("S_WF_STATE") && paramBean.getInt("S_WF_STATE") > 0) { //办理状态 
            strWhere.append(" and S_WF_STATE = ");
            strWhere.append(paramBean.getStr("S_WF_STATE"));
        }
        if (paramBean.isNotEmpty("S_EMERGENCY") && paramBean.getInt("S_EMERGENCY") > 0) { //紧急程度
            strWhere.append(" and S_EMERGENCY = ");
            strWhere.append(paramBean.getStr("S_EMERGENCY"));
        }
        if (paramBean.isNotEmpty("GW_SECRET")) { //密级
            strWhere.append(" and GW_SECRET = '");
            strWhere.append(paramBean.getStr("GW_SECRET"));
            strWhere.append("'");
        }
        if (paramBean.isNotEmpty("S_UNAME")) { //拟稿人
            strWhere.append(" and S_USER = '");
            strWhere.append(paramBean.getStr("S_UNAME"));
            strWhere.append("'");
        }
        if (paramBean.isNotEmpty("S_TNAME")) { //拟稿部门 hdy 2013-6-9 13:56
            strWhere.append(" and S_TDEPT = '");
            strWhere.append(paramBean.getStr("S_TNAME"));
            strWhere.append("'");
        }
        if (paramBean.isNotEmpty("GW_MAIN_TO")) {  //主送
            strWhere.append(" and GW_MAIN_TO like '%");
            strWhere.append(paramBean.getStr("GW_MAIN_TO"));
            strWhere.append("%'");
        }
        if (paramBean.isNotEmpty("GW_COPY_TO")) {  //抄送
            strWhere.append(" and GW_COPY_TO like '%");
            strWhere.append(paramBean.getStr("GW_COPY_TO"));
            strWhere.append("%'");
        }
        if (paramBean.isNotEmpty("GW_COSIGN_TO")) {  //会签
            strWhere.append(" and GW_COSIGN_TO like '%");
            strWhere.append(paramBean.getStr("GW_COSIGN_TO"));
            strWhere.append("%'");
        }
        if (paramBean.isNotEmpty("GW_BEGIN_TIME_1")) {  //拟稿开始时间
            strWhere.append(" and GW_BEGIN_TIME >= '");
            strWhere.append(paramBean.getStr("GW_BEGIN_TIME_1"));
            strWhere.append("'");
        }
        if (paramBean.isNotEmpty("GW_BEGIN_TIME_2")) { //拟稿结束时间
            strWhere.append(" and GW_BEGIN_TIME <= '");
            strWhere.append(paramBean.getStr("GW_BEGIN_TIME_2"));
            strWhere.append("'");
        }     
        if (paramBean.isNotEmpty("GW_SIGN_TIME_1")) { //签发开始时间 
            strWhere.append(" and GW_SIGN_TIME >= '");
            strWhere.append(paramBean.getStr("GW_SIGN_TIME_1"));
            strWhere.append("'");
        }
        if (paramBean.isNotEmpty("GW_SIGN_TIME_2")) { //签发结束时间  
            strWhere.append(" and GW_SIGN_TIME <= '");
            strWhere.append(paramBean.getStr("GW_SIGN_TIME_2"));
            strWhere.append("'");
        }
        if (paramBean.isNotEmpty("GW_CW_TIME_1")) { //成文开始时间
            strWhere.append(" and GW_CW_TIME >= '");
            strWhere.append(paramBean.getStr("GW_CW_TIME_1"));
            strWhere.append("'");
        }
        if (paramBean.isNotEmpty("GW_CW_TIME_2")) { //成文结束时间
            strWhere.append(" and GW_CW_TIME <= '");
            strWhere.append(paramBean.getStr("GW_CW_TIME_2"));
            strWhere.append("'");
        }
        if (paramBean.isNotEmpty("GW_ZB_TNAME")) {  //主办部门
            strWhere.append(" and GW_ZB_TNAME like '%");
            strWhere.append(paramBean.getStr("GW_ZB_TNAME"));
            strWhere.append("%'");
        }
        if (paramBean.isNotEmpty("GW_SW_CNAME")) {  //来文单位
            strWhere.append(" and GW_SW_CNAME like '%");
            strWhere.append(paramBean.getStr("GW_SW_CNAME"));
            strWhere.append("%'");
        }
        if (paramBean.isNotEmpty("GW_SW_TYPE")) {  //收文类别
            strWhere.append(" and GW_SW_TYPE = '");
            strWhere.append(paramBean.getStr("GW_SW_TYPE"));
            strWhere.append("'");
        }
        
        return strWhere;
    }
}
