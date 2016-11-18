package com.rh.bn.sync.impl.oa;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rh.bn.sync.AbstractSync;
import com.rh.bn.sync.db.BnSyncQbDataSource;
import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.base.Context.THREAD;
import com.rh.core.base.db.impl.OracleExecutor;
import com.rh.core.serv.ServDao;
import com.rh.core.util.DateUtils;

/**
 * 同步百年人寿旧签报系统签报审批意见
 * @author Tanyh 20150808
 *
 */
public class QbMindSync extends AbstractSync{
    /** 日志对象 **/
    Log log = LogFactory.getLog(QbMindSync.class);
    /** 意见服务ID **/
    private static final String MIND_SERV_ID = "BN_QB_MIND";
    /** 签报服务ID **/
    private static final String QB_SERV_ID = "BN_QB_DATA_QUERY";
    
    @Override
    protected List<Bean> getDataBeanList(String beginTime) {
        Connection conn = null;
        List<Bean> dataBeanList = new ArrayList<Bean>();
        try{
            // 获取签报数据库连接
            conn = BnSyncQbDataSource.getDBConnection();
            
            String sql ="SELECT * FROM (select ROWNUM AS NUM,PROCESSID," +
                    "ORDERS," +
                    "TRANSITION," +
                    "TN," +
                    "USERID," +
                    "USERNAME," + 
                    "DECISION," +
                    "TRANSFERPERSON," +
                    "AUTHORIZEDPERSON," +
                    "INSTRUCTIONSVALUE," +
                    "TO_CHAR(APPROVETIME,'yyyy-MM-dd hh24:mi:ss') AS APPROVETIME," +
                    "1 AS OPERATE_TYPE FROM TEMP_INSTRUCTIONS )";
            //获取总条数
            int dataCount = OracleExecutor.getExecutor().count(conn,"select count(*) COUNT_  from  TEMP_INSTRUCTIONS");
            //获取sql每次查询的条数
            int sqlMax = Context.app("SQL_MAX_SIZE", 50000);
            if (dataCount>=sqlMax) {
				for (int i = 0; i < dataCount/sqlMax+1; i++) {
					 // 获取签报审批意见
		            dataBeanList.addAll(OracleExecutor.getExecutor().query(conn,sql.concat(" where NUM > "+i*sqlMax)
		            		.concat(" and NUM <= "+(i+1)*sqlMax)));
				}
			}else {
				 // 获取签报审批意见
	            dataBeanList = OracleExecutor.getExecutor().query(conn,sql);
			}
        } catch (Exception e) {
            log.error("获取签报意见出错：" + e.getMessage());
            dataBeanList = new ArrayList<Bean>();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    log.error("签报数据库连接关闭出错：" + e.getMessage());
                }
            }
        }
        return dataBeanList;
    }

    @Override
    protected Bean prepareData(Bean data) {
        Bean mindBean = new Bean();
        // 数据ID，即：签报号
        mindBean.set("DATA_ID", data.getStr("PROCESSID"));
        // 服务ID
        mindBean.set("SERV_ID", QB_SERV_ID);
        // 意见排序
        mindBean.set("MIND_SORT", data.getStr("ORDERS"));
        // 意见编码
        mindBean.set("MIND_CODE", "SH-0006");
        // 意见编码名称
        mindBean.set("MIND_CODE_NAME", "审核意见");
        // 意见类型：一般意见
        mindBean.set("MIND_TYPE", "1");
        // 意见内容
        mindBean.set("MIND_CONTENT", data.getStr("INSTRUCTIONSVALUE"));
        // 意见显示规则
        mindBean.set("MIND_DIS_RULE", "3");
        // 意见填写时间
        mindBean.set("MIND_TIME", data.getStr("APPROVETIME"));
        // 意见级别
        mindBean.set("MIND_LEVEL", "0");
        // 填写人
        mindBean.set("S_USER", data.getStr("USERID"));
        // 填写人姓名
        mindBean.set("S_UNAME", data.getStr("USERNAME"));
        // 填写人所属处室编码
        mindBean.set("S_DEPT", "");
        // 填写人所属处室名称
        mindBean.set("S_DNAME", "");
        // 填写人所属部门编码
        mindBean.set("S_TDEPT", "");
        // 填写人所属部门名称
        mindBean.set("S_TNAME", "");
        // 填写人所属机构
        mindBean.set("S_ODEPT", "");
        // 公司编码
        mindBean.set("S_CMPY", Context.getThread(THREAD.CMPYCODE));
        // 修改时间
        mindBean.set("S_MTIME", DateUtils.getDatetime());
        return mindBean;
    }

    @Override
    protected boolean isDeleted() {
        return false;
    }

    @Override
    protected String getImplServId() {
        return MIND_SERV_ID;
    }

    @Override
    protected void logDataSyncState(Bean dataBean, int syncFlag, String syncDesc) {
        // 插入日志表
        Bean logBean = new Bean();
        logBean.set("DATA_ID", dataBean.getStr("MIND_ID"));
        logBean.set("SERV_ID", getImplServId());
        logBean.set("SYNC_FLAG", syncFlag);
        logBean.set("SYNC_DESC", syncDesc);
        logBean.set("SYNC_TIME", DateUtils.getDatetime());
        ServDao.create(getLogServId(), logBean);
    }

}
