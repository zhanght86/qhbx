package com.rh.oa.cn.serv;

import org.apache.commons.lang.StringUtils;

import com.rh.core.base.Context;
import com.rh.core.org.UserBean;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.oa.cn.mgr.ConcertMgr;
import com.rh.oa.gw.GwServ;

/**
 * 工作协调单
 * @author chensheng
 * 
 */
public class ConcertServ extends GwServ {

    @Override
    protected void beforeQuery(ParamBean paramBean) {
        super.beforeQuery(paramBean);
        
        // 如果是查询的话
        boolean searchFlag = paramBean.getBoolean(ConcertMgr.COL_SEARCH_FLAG);
        if (searchFlag) {
            paramBean.setQueryExtWhere(this.getQuerySql(paramBean));
        }
    }
    
    /**
     * 获取查询条件
     * @param paramBean 参数
     * @return 返回查询SQL
     */
    private String getQuerySql(ParamBean paramBean) {
        StringBuffer queryBuf = new StringBuffer();
        
        // 标题
        String title = paramBean.getStr(ConcertMgr.COL_CO_TITLE);
        if (StringUtils.isNotEmpty(title)) {
            queryBuf.append(" and CO_TITLE like '%").append(title).append("%'");
        }
        
        // 拟稿部门
        String tdept = paramBean.getStr(ConcertMgr.COL_S_TDEPT);
        if (StringUtils.isNotEmpty(tdept)) {
            queryBuf.append(" and S_TDEPT='").append(tdept).append("'");
        }
        
        // 拟稿人
        String user = paramBean.getStr(ConcertMgr.COL_S_USER);
        if (StringUtils.isNotEmpty(user)) {
            queryBuf.append(" and S_USER='").append(user).append("'");
        }
        
        // 拟稿时间
        String startTime = paramBean.getStr(ConcertMgr.COL_S_ATIME_BEGIN);
        String endTime = paramBean.getStr(ConcertMgr.COL_S_ATIME_END);
        if (StringUtils.isNotBlank(startTime)) {
            queryBuf.append(" and S_ATIME>='").append(startTime).append("'");
        }
        if (StringUtils.isNotBlank(endTime)) {
            queryBuf.append(" and S_ATIME<='").append(endTime).append("'");
        }
        
        // 紧急程度
        int emergency = paramBean.getInt(ConcertMgr.COL_S_EMERGENCY);
        if (emergency != 0) {
            queryBuf.append(" and S_EMERGENCY=").append(emergency);
        }
        
        // 流程状态
        int state = paramBean.getInt(ConcertMgr.COL_S_WF_STATE);
        if (state != 0) {
            queryBuf.append(" and S_WF_STATE=").append(state);
        }
        
        return queryBuf.toString();
    }
    
    @Override
    protected void afterByid(ParamBean paramBean, OutBean outBean) {
        UserBean user = Context.getUserBean();
        //如果是控股公司
        if (user.getODeptBean().getLevel() == 1) {
            outBean.set("MODIFIY_RED_TITLE", Context.getSyConf("OA_CN_CONCERT_REDHEAD_TITLE", ""));
        }
    }
}
