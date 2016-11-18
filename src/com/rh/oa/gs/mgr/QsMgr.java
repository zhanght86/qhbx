package com.rh.oa.gs.mgr;

import org.apache.commons.lang.StringUtils;

import com.rh.core.serv.ParamBean;

/**
 * @author chensheng
 */
public class QsMgr {
    /**
     * 自定义查询的查询标示字段
     */
    public static final String COL_SEARCH_FLAG = "SEARCH_FLAG";
    /**
     * 标题
     */
    public static final String COL_GW_TITLE = "GW_TITLE";
    /**
     * 拟稿部门
     */
    public static final String COL_S_TNAME = "S_TNAME";
    /**
     * 拟稿人
     */
    public static final String COL_S_UNAME = "S_UNAME";
    /**
     * 拟稿查询开始时间
     */
    public static final String COL_GW_BEGIN_TIME_BEGIN = "GW_BEGIN_TIME_BEGIN";
    /**
     * 拟稿查询开始时间
     */
    public static final String COL_GW_BEGIN_TIME_END = "GW_BEGIN_TIME_END";
    /**
     * 紧急程度
     */
    public static final String COL_S_EMERGENCY = "  S_EMERGENCY";
    /**
     * 当前流程状态
     */
    public static final String COL_S_WF_STATE = "S_WF_STATE";
    /**
     * 文件类型
     */
    public static final String COL_GW_FILE_TYPE = "GW_FILE_TYPE";
    /**
     * 来文单位
     */
    public static final String COL_GW_SW_CNAME = "GW_SW_CNAME";
    /**
     * 联系人
     */
    public static final String COL_GW_CONTACT = "GW_CONTACT";
    /**
     * 公文编号
     */
    public static final String COL_GW_YEAR_CODE = "GW_YEAR_CODE";
    /**
     * 公文年份
     */
    public static final String COL_GW_YEAR = "GW_YEAR";
    /**
     *  公文类型：大类型
     */
    public static final String COL_TMPL_TYPE_CODE = "TMPL_TYPE_CODE";
    
    /**
     *  公文模板：小类型
     */
    public static final String COL_TMPL_CODE = "TMPL_CODE";
    
    /**
     * 公文编号流水号
     */
    public static final String COL_GW_YEAR_NUMBER = "GW_YEAR_NUMBER";
    
    /**
     * 获取查询条件
     * @param paramBean 参数
     * @return 返回查询SQL
     */
    public static String getQuerySql(ParamBean paramBean) {
        StringBuffer queryBuf = new StringBuffer();
        
        // 标题
        String title = paramBean.getStr(QsMgr.COL_GW_TITLE);
        if (StringUtils.isNotEmpty(title)) {
            queryBuf.append(" and GW_TITLE like '%").append(title).append("%'");
        }
        
        // 拟稿部门
        String tdept = paramBean.getStr(QsMgr.COL_S_TNAME);
        if (StringUtils.isNotEmpty(tdept)) {
            queryBuf.append(" and S_TNAME like '%").append(tdept).append("%'");
        }
        
        // 拟稿人
        String user = paramBean.getStr(QsMgr.COL_S_UNAME);
        if (StringUtils.isNotEmpty(user)) {
            queryBuf.append(" and S_UNAME like '%").append(user).append("%'");
        }
        
        // 拟稿时间
        String startTime = paramBean.getStr(QsMgr.COL_GW_BEGIN_TIME_BEGIN);
        String endTime = paramBean.getStr(QsMgr.COL_GW_BEGIN_TIME_END);
        if (StringUtils.isNotBlank(startTime)) {
            queryBuf.append(" and GW_BEGIN_TIME>='").append(startTime).append("'");
        }
        if (StringUtils.isNotBlank(endTime)) {
            queryBuf.append(" and GW_BEGIN_TIME<='").append(endTime).append("'");
        }
        
        // 紧急程度
        int emergency = paramBean.getInt(QsMgr.COL_S_EMERGENCY);
        if (emergency != 0) {
            queryBuf.append(" and S_EMERGENCY=").append(emergency);
        }
        
        // 流程状态
        int state = paramBean.getInt(QsMgr.COL_S_WF_STATE);
        if (state != 0) {
            queryBuf.append(" and S_WF_STATE=").append(state);
        }
        
        // 来文单位
        String gwSwCname = paramBean.getStr(QsMgr.COL_GW_SW_CNAME);
        if (StringUtils.isNotBlank(gwSwCname)) {
            queryBuf.append(" and GW_SW_CNAME like '%").append(gwSwCname).append("%'");
        }
        
        // 联系人
        String gwContact = paramBean.getStr(QsMgr.COL_GW_CONTACT);
        if (StringUtils.isNotBlank(gwContact)) {
            queryBuf.append(" and GW_CONTACT like '%").append(gwContact).append("%'");
        }
        
        // 公文编号
        String gwYearCode = paramBean.getStr(QsMgr.COL_GW_YEAR_CODE);
        if (StringUtils.isNotBlank(gwYearCode)) {
            queryBuf.append(" and GW_YEAR_CODE like '%").append(gwYearCode).append("%'");
        }
        
        return queryBuf.toString();
    }
   
}