package com.rh.bn.sync.impl.oa;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rh.bn.sync.AbstractSync;
import com.rh.bn.sync.db.BnSyncQbDataSource;
import com.rh.bn.util.DocUtil;
import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.base.TipException;
import com.rh.core.base.Context.THREAD;
import com.rh.core.base.db.impl.OracleExecutor;
import com.rh.core.comm.FileMgr;
import com.rh.core.org.UserBean;
import com.rh.core.org.mgr.UserMgr;
import com.rh.core.serv.ServDao;
import com.rh.core.util.DateUtils;
import com.rh.core.util.Lang;

/**
 * 同步百年人寿旧签报系统签报数据
 * @author Tanyh
 *
 */
public class QbDataSync extends AbstractSync{
    
    private static Log log = LogFactory.getLog(QbDataSync.class);
    /** 旧系统迁移过来的签报数据服务ID **/
    private static final String QB_SERV_ID = "BN_QB_DATA_QUERY";
    @Override
    protected List<Bean> getDataBeanList(String beginTime) {
        Connection conn = null;
        List<Bean> dataBeanList = new ArrayList<Bean>();
        try{
            // 获取签报数据库连接
            conn = BnSyncQbDataSource.getDBConnection();
            
            String sql = "SELECT * FROM (SELECT ROWNUM AS NUM, PROCESSID," +
        			"TASKID," +
        			"TO_CHAR(STARTTIME,'yyyy-MM-dd hh24:mi:ss') AS STARTTIME," +
        			"TO_CHAR(PASSTIME,'yyyy-MM-dd hh24:mi:ss') AS PASSTIME," +
        			"TRAJECTORY," +
        			"APPROVEDLIST," +
        			"APPROVELIST," +
        			"APPROVEUSER," +
        			"APPROVERSEE," +
        			"APPROVALPERSON," +
        			"TASKCONTENT," +
        			"PHONE," +
        			"DEPARTMENTS," +
        			"TASKNAME," +
        			"REQUESTPERSON," +
        			"1 AS OPERATE_TYPE from TEMP_VARIABLEINSTANCE )" +
        			" ";
            // 先取得的记录总数
            int dataCount = OracleExecutor.getExecutor().count(conn,"select count(*) COUNT_  from TEMP_VARIABLEINSTANCE");            
            // 查询签报数据
            if (dataCount > Context.app("SQL_MAX_SIZE", 50000)) {
                // for循环，每次取指定的数量的记录，放入dataBeanList
            	for (int i=0;i<dataCount/Context.app("SQL_MAX_SIZE", 50000)+1;i++) {
            		dataBeanList.addAll(OracleExecutor.getExecutor().query(conn, 
                    sql.concat(" where NUM >"+i*Context.app("SQL_MAX_SIZE", 50000)).
                    concat(" and NUM<="+(i+1)*Context.app("SQL_MAX_SIZE", 50000))));
            	}
            } else {
            	dataBeanList = OracleExecutor.getExecutor().query(conn, 
                    sql);
            }
        } catch (Exception e) {
            log.error("获取签报数据出错：" + e.getMessage());
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
//    protected List<Bean> getDataBeanList(String beginTime) {
//        Connection conn = null;
//        List<Bean> dataBeanList = null;
//        try{
//            // 获取签报数据库连接
//            conn = BnSyncQbDataSource.getDBConnection();
//            // 查询签报数据
//            dataBeanList = OracleExecutor.getExecutor().query(conn, 
//                    "SELECT PROCESSID," +
//                    "TASKID," +
//                    "TO_CHAR(STARTTIME,'yyyy-MM-dd hh24:mi:ss') AS STARTTIME," +
//                    "TO_CHAR(PASSTIME,'yyyy-MM-dd hh24:mi:ss') AS PASSTIME," +
//                    "TRAJECTORY," +
//                    "APPROVEDLIST," +
//                    "APPROVELIST," +
//                    "APPROVEUSER," +
//                    "APPROVERSEE," +
//                    "APPROVALPERSON," +
//                    "TASKCONTENT," +
//                    "PHONE," +
//                    "DEPARTMENTS," +
//                    "TASKNAME," +
//                    "REQUESTPERSON," +
//                    "1 AS OPERATE_TYPE from TEMP_VARIABLEINSTANCE " +
//                    " ");
//        } catch (Exception e) {
//            log.error("获取签报数据出错：" + e.getMessage());
//            dataBeanList = new ArrayList<Bean>();
//        } finally {
//            if (conn != null) {
//                try {
//                    conn.close();
//                } catch (SQLException e) {
//                    log.error("签报数据库连接关闭出错：" + e.getMessage());
//                }
//            }
//        }
//        return dataBeanList;
//    }

    @Override
    protected Bean prepareData(Bean data) {
        Bean newData = new Bean();
        // 签报号
        newData.set("QB_ID", data.getStr("PROCESSID"));
        // 任务ID
        newData.set("TASK_ID", data.getStr("TASKID"));
        // 签报标题
        newData.set("QB_TITLE", data.getStr("TASKNAME"));
        // 开始时间
        newData.set("START_TIME", data.getStr("STARTTIME"));
        // 结束时间
        newData.set("END_TIME", data.getStr("PASSTIME"));
        // 流转痕迹
        newData.set("FLOW_TRACE", data.getStr("TRAJECTORY"));
        // 已审批列表
        //newData.set("APPROVED_LIST", data.getStr("APPROVEDLIST"));
        // 审批列表
        //newData.set("APPROVE_LIST", data.getStr("APPROVELIST"));
        // 当前审批人
        //newData.set("APPROVE_USER", data.getStr("APPROVEUSER"));
        // 看签报权限
        newData.set("APPROVER_SEE", data.getStr("APPROVERSEE"));
        // 签报审批人
        newData.set("APPROVAL_PERSON", data.getStr("APPROVALPERSON"));
        // 起草人
        UserBean userBean = new UserBean(new Bean());
        if (data.getStr("REQUESTPERSON").length() > 0) {
            try{
                //根据用户登录名获取用户信息
                userBean = UserMgr.getUser(data.getStr("REQUESTPERSON"), String.valueOf(Context.getThread(THREAD.CMPYCODE)));
                // 起草人
                newData.set("S_USER", userBean.getId());
                // 起草人姓名
                newData.set("S_UNAME", userBean.getName());
            } catch (Exception e) {
                log.error("获取用户" + data.getStr("REQUESTPERSON") + "信息失败：" + e.getMessage());
                // 起草人
                newData.set("S_USER", data.getStr("REQUESTPERSON"));
                // 起草人姓名
                newData.set("S_UNAME", data.getStr("REQUESTPERSON"));
            }
        }
        // 联系电话
        newData.set("CONTACT_PHONE", data.getStr("PHONE"));
        // 起草部门
        newData.set("S_DEPT", data.getStr("DEPARTMENTS"));
        // 公司编码
        newData.set("S_CMPY", Context.getThread(THREAD.CMPYCODE));
        // 签报请示具体事项说明，即正文
        if (data.get("TASKCONTENT") != null) {
            Clob content = (Clob) data.get("TASKCONTENT");
            ByteArrayInputStream bais = null;
            FileOutputStream fos = null;
            try {
                // 设置用户信息
                Bean user = new Bean();
                user.set("USER_CODE", newData.getStr("S_USER"));
                user.set("USER_NAME", "");
                user.set("CMPY_CODE", Context.getThread(THREAD.CMPYCODE));
                Context.setThread(THREAD.USERBEAN, new UserBean(user));
                //生成正文
                Bean paramBean = new Bean();
                paramBean.set("FILE_ID", Lang.getUUID());
                paramBean.set("SERV_ID", QB_SERV_ID);
                paramBean.set("DATA_ID", newData.getStr("QB_ID"));
                paramBean.set("FILE_NAME", "请示事项具体内容.doc");
                paramBean.set("FILE_CAT", "QB_ZHENGWEN");
                paramBean.set("FILE_MTYPE", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
                Reader reader = content.getCharacterStream();
                char[] b = new char[1024];
                int word = 0;
                StringBuffer sb = new StringBuffer("");
                while ( (word=reader.read(b)) != -1) {
//                    sb.append(new String(b));
                    //写入字符串对象中
                    if (word < 1024) {
                        sb.append((new String(b)).substring(0, word));
                    } else {
                        sb.append(new String(b));
                    }
                }
                reader.close();
                //FileMgr.upload(paramBean, new ByteArrayInputStream(sb.toString().getBytes("GBK")));
                String filePath = FileMgr.buildPathExpr(QB_SERV_ID, paramBean.getStr("FILE_ID") + ".doc");
                paramBean.set("FILE_PATH", filePath);
                //filePath = FileMgr.getAbsolutePath(filePath);
                File target = new File(FileMgr.getAbsolutePath(filePath));
                if (!target.getParentFile().exists()) {
                    target.getParentFile().mkdirs();
                }
                Map<String, Object> dataMap = new HashMap<String, Object>();
                dataMap.put("content", sb.toString());
                DocUtil.process(dataMap, "zhengwen.xml", target);
                //文件数据入库
                ServDao.create("SY_COMM_FILE", paramBean);
            } catch (SQLException e) {
                log.error("签报号：" + newData.getStr("QB_ID") + ",读取签报正文信息失败：" + e.getMessage());
                throw new TipException("签报号：" + newData.getStr("QB_ID") + ",读取签报正文信息失败。");
            } catch (Exception e) {
                log.error("签报号：" + newData.getStr("QB_ID") + ",读取签报正文信息失败：" + e.getMessage());
                throw new TipException("签报号：" + newData.getStr("QB_ID") + ",读取签报正文信息失败。");
            } finally {
                    try {
                        if (bais != null) {
                            bais.close();
                        }
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (IOException e) {
                        log.error(e.getMessage());
                    }
            }
        }
        return newData;
    }

    @Override
    protected boolean isDeleted() {
        return false;
    }

    @Override
    protected String getImplServId() {
        return QB_SERV_ID;
    }

    @Override
    protected void logDataSyncState(Bean dataBean, int syncFlag, String syncDesc) {
        // 插入日志表
        Bean logBean = new Bean();
        logBean.set("DATA_ID", dataBean.getStr("PROCESSID"));
        logBean.set("SERV_ID", getImplServId());
        logBean.set("SYNC_FLAG", syncFlag);
        logBean.set("SYNC_DESC", syncDesc);
        logBean.set("SYNC_TIME", DateUtils.getDatetime());
        ServDao.create(getLogServId(), logBean);
    }

}
