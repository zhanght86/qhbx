package com.rh.lw.ct.mgr;

import org.apache.commons.lang.StringUtils;

import com.rh.core.base.Bean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServMgr;
import com.rh.core.serv.ServUtils;

/**
 * @author chensheng
 */
public class ContractMgr {
    /**
     * 合同服务
     */
    public static final String LW_CT_CONTRACT = "LW_CT_CONTRACT";
    /**
     * 合同台账服务
     */
    public static final String LW_CT_CONTRACT_BOOK = "LW_CT_CONTRACT_BOOK";
    /**
     * 合格授权表
     */
    public static final String LW_CT_AUTHORITY = "LW_CT_AUTHORITY";
    /**
     * 合同ID
     */
    public static final String COL_CT_ID = "CT_ID";
    /**
     * 自定义查询的查询标示字段
     */
    public static final String COL_SEARCH_FLAG = "SEARCH_FLAG";
    /**
     * 合同编号
     */
    public static final String COL_CT_CODE = "CT_CODE";
    /**
     * 所属项目
     */
    public static final String COL_CT_PROJ = "CT_PROJ";
    /**
     * 合同名称
     */
    public static final String COL_CT_NAME = "CT_NAME";
    /**
     * 合同类别
     */
    public static final String COL_CT_TYPE = "CT_TYPE";
    /**
     * 密级
     */
    public static final String COL_CT_SECRET = "CT_SECRET";
    /**
     * 期限类型
     */
    public static final String COL_CT_DTYPE = "CT_DTYPE";
    /**
     * 签订日期 开始
     */
    public static final String COL_CT_STIME_BEGIN = "CT_STIME_BEGIN";
    /**
     * 签订日期 结束
     */
    public static final String COL_CT_STIME = "CT_STIME";
    /**
     * 起始时间 开始
     */
    public static final String COL_CT_BTIME_BEGIN = "CT_BTIME_BEGIN";
    /**
     * 起始时间
     */
    public static final String COL_CT_BTIME = "CT_BTIME";
    /**
     * 终止时间 开始
     */
    public static final String COL_CT_ETIME_BEGIN = "CT_ETIME_BEGIN";
    /**
     * 终止时间
     */
    public static final String COL_CT_ETIME = "CT_ETIME";
    /**
     * 起草时间 开始
     */
    public static final String COL_S_ATIME_BEGIN = "S_ATIME_BEGIN";
    /**
     * 起草时间
     */
    public static final String COL_S_ATIME = "S_ATIME";
    /**
     * 费用类型
     */
    public static final String COL_CT_FTYPE = "CT_FTYPE";
    /**
     * 币种
     */
    public static final String COL_CT_CTYPE = "CT_CTYPE";
    /**
     * 主办部门
     */
    public static final String COL_S_TDEPT = "S_TDEPT";
    /**
     * 承办人
     */
    public static final String COL_CT_USER = "CT_USER";
    /**
     * 是否关联交易
     */
    public static final String COL_CT_IS_AT = "CT_IS_AT";
    /**
     * 年份
     */
    public static final String COL_CT_YEAR = "CT_YEAR";
    /**
     * 清稿版本
     */
    public static final String COL_CT_CVERSION = "CT_CVERSION";
    /**
     * 合同流水号
     */
    public static final String COL_CT_SERIAL = "CT_SERIAL";
    /**
     * 机关代字
     */
    public static final String COL_CT_WORD = "CT_WORD";
    /**
     * 正本
     */
    public static final String FILE_ZHENGWEN = "ZHENGWEN";
    /**
     * 附件
     */
    public static final String FILE_FUJIAN = "FUJIAN";
    /**
     * 修改稿
     */
    public static final String FILE_XIUGAIGAO = "XIUGAIGAO";
    /**
     * 范本服务
     */
    public static final String LW_CT_TEMPLATE = "LW_CT_TEMPLATE";
    /**
     * 范本ID
     */
    public static final String COL_TP_ID = "TP_ID";
    /**
     * 范本正文
     */
    public static final String FILE_TP_FILE = "TP_FILE";
    
    /**
     * 按照年份和机构查出最大的流水号
     * @param ctBean 合同Bean
     * @return 返回最大的合同流水号
     */
    public static int getSerial(Bean ctBean) {
        String odeptCode = ctBean.getStr("S_ODEPT");
        String ctYear = ctBean.getStr("CT_YEAR");

        int maxValue = 1;
        ParamBean paramBean = new ParamBean().setServId(ContractMgr.LW_CT_CONTRACT)
                .setAct("find").set("S_ODEPT", odeptCode).set(ContractMgr.COL_CT_YEAR, ctYear);
      
        Bean result = ServMgr.act(paramBean);
        maxValue = result.getInt("MAX(CT_SERIAL)");
        if (maxValue <= 0) {
            maxValue = 1;
        } else {
            ++maxValue;
        }
        return maxValue;
    }

    /**
     * 获取查询条件
     * @param paramBean 参数
     * @return 返回查询SQL
     */
    public static String getQuerySql(ParamBean paramBean) {
        StringBuffer queryBuf = new StringBuffer();
        
        // 合同编号
        String ctCode = paramBean.getStr(ContractMgr.COL_CT_CODE);
        if (StringUtils.isNotEmpty(ctCode)) {
            queryBuf.append(" and CT_CODE like '%").append(ctCode).append("%'");
        }
        
        // 所属项目
        String ctProj = paramBean.getStr(ContractMgr.COL_CT_PROJ);
        if (StringUtils.isNotEmpty(ctCode)) {
            queryBuf.append(" and CT_PROJ like '%").append(ctProj).append("%'");
        }
        
        // 合同名称
        String ctName = paramBean.getStr(ContractMgr.COL_CT_NAME);
        if (StringUtils.isNotEmpty(ctName)) {
            queryBuf.append(" and CT_NAME like '%").append(ctName).append("%'");
        }
        
        // 合同类别
        int ctType = paramBean.getInt(ContractMgr.COL_CT_TYPE);
        if (ctType != 0) {
            queryBuf.append(" and CT_TYPE=").append(ctType);
        }
        
        // 密级
        int ctSecret = paramBean.getInt(ContractMgr.COL_CT_SECRET);
        if (ctSecret != 0) {
            queryBuf.append(" and CT_SECRET=").append(ctSecret);
        }
        
        // 期限类型
        int ctDtype = paramBean.getInt(ContractMgr.COL_CT_DTYPE);
        if (ctDtype != 0) {
            queryBuf.append(" and CT_DTYPE=").append(ctDtype);
        }
        
        // 签订日期
        String ctStimeBegin = paramBean.getStr(ContractMgr.COL_CT_STIME_BEGIN);
        String ctStime = paramBean.getStr(ContractMgr.COL_CT_STIME);
        if (StringUtils.isNotBlank(ctStimeBegin)) {
            queryBuf.append(" and CT_STIME>='").append(ctStimeBegin).append("'");
        }
        if (StringUtils.isNotBlank(ctStime)) {
            queryBuf.append(" and CT_STIME<='").append(ctStime).append("'");
        }
        
        //起始日期
        String ctBtimeBegin = paramBean.getStr(ContractMgr.COL_CT_BTIME_BEGIN);
        String ctBtime = paramBean.getStr(ContractMgr.COL_CT_BTIME);
        if (StringUtils.isNotBlank(ctBtimeBegin)) {
            queryBuf.append(" and CT_BTIME>='").append(ctBtimeBegin).append("'");
        }
        if (StringUtils.isNotBlank(ctStime)) {
            queryBuf.append(" and CT_BTIME<='").append(ctBtime).append("'");
        }
        
        // 终止时间
        String ctEtimeBegin = paramBean.getStr(ContractMgr.COL_CT_ETIME_BEGIN);
        String ctEtime = paramBean.getStr(ContractMgr.COL_CT_ETIME);
        if (StringUtils.isNotBlank(ctEtimeBegin)) {
            queryBuf.append(" and CT_ETIME>='").append(ctEtimeBegin).append("'");
        }
        if (StringUtils.isNotBlank(ctEtime)) {
            queryBuf.append(" and CT_ETIME<='").append(ctEtime).append("'");
        }
        
        // 起草时间
        String sAtimeBegin = paramBean.getStr(ContractMgr.COL_S_ATIME_BEGIN);
        String sAtime = paramBean.getStr(ContractMgr.COL_S_ATIME);
        if (StringUtils.isNotBlank(sAtimeBegin)) {
            queryBuf.append(" and S_ATIME>='").append(sAtimeBegin).append("'");
        }
        if (StringUtils.isNotBlank(sAtime)) {
            queryBuf.append(" and S_ATIME<='").append(sAtime).append("'");
        }
        
        // 费用类型
        int ctFtype = paramBean.getInt(ContractMgr.COL_CT_FTYPE);
        if (ctFtype != 0) {
            queryBuf.append(" and CT_FTYPE=").append(ctFtype);
        }
        
        // 币种
        int ctCtype = paramBean.getInt(ContractMgr.COL_CT_CTYPE);
        if (ctCtype != 0) {
            queryBuf.append(" and CT_CTYPE=").append(ctCtype);
        }
        
        // 主办部门
        String  sTdept = paramBean.getStr(ContractMgr.COL_S_TDEPT);
        if (StringUtils.isNotBlank(sTdept)) {
            queryBuf.append(" and S_TDEPT='").append(sTdept).append("'");
        }
        
        // 承办人
        String  ctUser = paramBean.getStr(ContractMgr.COL_CT_USER);
        if (StringUtils.isNotBlank(ctUser)) {
            queryBuf.append(" and CT_USER='").append(ctUser).append("'");
        }
        
        // 是否关联交易
        int ctIsAt = paramBean.getInt(ContractMgr.COL_CT_IS_AT);
        if (ctIsAt != 0) {
            queryBuf.append(" and CT_IS_AT=").append(ctIsAt);
        }
        
        // 年份
        String  ctYear = paramBean.getStr(ContractMgr.COL_CT_YEAR);
        if (StringUtils.isNotBlank(ctYear)) {
            queryBuf.append(" and CT_YEAR='").append(ctYear).append("'");
        }
        
        // 加上台账库的过滤条件
        String queryWhere = ServUtils.getServDef(ContractMgr.LW_CT_CONTRACT_BOOK).getServExpressionWhere();
        queryBuf.append(queryWhere);
        
        return queryBuf.toString();
    }
    
    /**
     * 去合同授权表里看看当前人有没有该合同权限
     * @param ctBean 合同Bean
     * @param userId  用户ID
     * @return 返回
     */
    public static boolean isAuthOwner(Bean ctBean, String userId) {
        String ctId = ctBean.getId();
        int count = ServDao.count(ContractMgr.LW_CT_AUTHORITY, 
                new Bean().set("CT_ID", ctId).set("AUTH_OWNER", userId)); 
        return count > 0;
    }
    
}