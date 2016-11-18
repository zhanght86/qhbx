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
import com.rh.core.comm.FileMgr;
import com.rh.core.serv.ServDao;
import com.rh.core.util.DateUtils;

/**
 * 同步百年人寿旧签报系统签报附件
 * @author Tanyh 20150808
 *
 */
public class QbAttachmentSync extends AbstractSync{
    /** 日志对象 **/
    Log log = LogFactory.getLog(QbAttachmentSync.class);
    /** 附件服务ID **/
    private static final String ATTACHMENT_SERV_ID = "SY_COMM_FILE";
    /** 签报服务ID **/
    private static final String QB_SERV_ID = "BN_QB_DATA_QUERY";

    @Override
    protected List<Bean> getDataBeanList(String beginTime) {
        Connection conn = null;
        List<Bean> dataBeanList = new ArrayList<Bean>();
        try{
            // 获取签报数据库连接
            conn = BnSyncQbDataSource.getDBConnection();
            String sql = "SELECT *  FROM (select ROWNUM AS NUM , PROCESSID," +
                    "ORDERS," +
                    "USERID," +
                    "USERNAME," +
                    "REPOSITORYID," +
                    "FILENAME," +
                    "FOLDERID," +
                    "1 AS OPERATE_TYPE FROM TEMP_ATTACHMENT) ";

            //获取总条数
            int dataCount = OracleExecutor.getExecutor().count(conn,"select count(*) COUNT_  from  TEMP_ATTACHMENT");
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
            // 获取签报附件
//            dataBeanList = OracleExecutor.getExecutor().query(conn,
//                    "select PROCESSID," +
//                    "ORDERS," +
//                    "USERID," +
//                    "USERNAME," +
//                    "REPOSITORYID," +
//                    "FILENAME," +
//                    "FOLDERID," +
//                    "1 AS OPERATE_TYPE FROM TEMP_ATTACHMENT " +
//                    " ");
        } catch (Exception e) {
            log.error("获取签报附件出错：" + e.getMessage());
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
        Bean attachBean = new Bean();
        // 数据ID
        attachBean.set("DATA_ID", data.getStr("PROCESSID"));
        // 服务ID
        attachBean.set("SERV_ID", QB_SERV_ID);
        // 文件名称
        attachBean.set("FILE_NAME", data.getStr("FILENAME"));
        // 文件路径
        attachBean.set("FILE_PATH", "@SYS_FILE_PATH@/" + Context.getThread(THREAD.CMPYCODE) + "/2016/4/2016-04-04/" + QB_SERV_ID + "/"
                + String.valueOf(DateUtils.getYear())
                + "/" + DateUtils.getDate() + "/" + data.getStr("FOLDERID") + "/" + data.getStr("REPOSITORYID") +
                "/1.0");
        // 文件类型
        attachBean.set("FILE_MTYPE", FileMgr.getMTypeByName(data.getStr("FILENAME")));
        // 文件分类
        attachBean.set("FILE_CAT", "QB_FUJIAN");
        // 文件上传人
        attachBean.set("S_USER", data.getStr("USERID"));
        // 上传人姓名
        attachBean.set("S_UNAME", data.getStr("USERNAME"));
        // 上传人所属处室
        attachBean.set("S_DEPT", "");
        // 上传人所属处室名称
        attachBean.set("S_DNAME", "");
        // 公司code
        attachBean.set("S_CMPY", Context.getThread(THREAD.CMPYCODE));
        // 上传时间
        attachBean.set("S_MTIME", "");
        return attachBean;
    }

    @Override
    protected boolean isDeleted() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected String getImplServId() {
        // TODO Auto-generated method stub
        return ATTACHMENT_SERV_ID;
    }

    @Override
    protected void logDataSyncState(Bean dataBean, int syncFlag, String syncDesc) {
        // 插入日志表
        Bean logBean = new Bean();
        logBean.set("DATA_ID", dataBean.getStr("FILE_ID"));
        logBean.set("SERV_ID", getImplServId());
        logBean.set("SYNC_FLAG", syncFlag);
        logBean.set("SYNC_DESC", syncDesc);
        logBean.set("SYNC_TIME", DateUtils.getDatetime());
        ServDao.create(getLogServId(), logBean);
    }

}
