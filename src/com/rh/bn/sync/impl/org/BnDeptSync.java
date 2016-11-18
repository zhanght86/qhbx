package com.rh.bn.sync.impl.org;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rh.bn.sync.AbstractSync;
import com.rh.bn.sync.util.BnOrgCacheUtil;
import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.base.Context.DS;
import com.rh.core.base.db.impl.OracleExecutor;
import com.rh.core.serv.ServDao;
import com.rh.core.util.DateUtils;

/**
 * 百年人寿项目部门同步类
 * @author Tanyh 20151009
 *
 */
public class BnDeptSync extends AbstractSync{
    
    /**日志对象**/
    Log log = LogFactory.getLog(BnDeptSync.class);

    @Override
    protected List<Bean> getDataBeanList(String beginTime) {
        List<Bean> dataList = new ArrayList<Bean>();
        //获取配置的人力资源系统数据源载体对象
        Bean dsBean = Context.getDSBean("jdbc/rh_hr");
        if (dsBean != null && !dsBean.isEmpty()) {
            DataSource ds = (DataSource) dsBean.get(DS.DS);
            if (ds != null) {
                //获取数据库连接
                Connection conn = null;
                try {
                    conn = ds.getConnection();
                    dataList = OracleExecutor.getExecutor().query(
                            conn,
                            "select PK_DEPT," +
                                    "NAME," +
                                    "CODE," +
                                    "PK_FATHERORG," +
                                    "PK_ORG," +
                                    "HRCANCELED," +
                                    "DEPTCANCELDATE," +
                                    "ENABLESTATE," +
                                    "MODIFIEDTIME," +
                                    "1 AS OPERATE_TYPE from ORG_DEPT "
                                    + ((beginTime!=null&&beginTime.length()>0)?"where MODIFIEDTIME >= '" + beginTime
                                    + "' or  creationtime >= '" + beginTime + "' or  TS >= '" + beginTime + "'":"") + 
                                    " connect by prior PK_DEPT=PK_FATHERORG start with PK_FATHERORG='~'");
              
                    
                    String sql =  "select PK_DEPT," +
                            "NAME," +
                            "CODE," +
                            "PK_FATHERORG," +
                            "PK_ORG," +
                            "HRCANCELED," +
                            "DEPTCANCELDATE," +
                            "ENABLESTATE," +
                            "MODIFIEDTIME," +
                            "1 AS OPERATE_TYPE from ORG_DEPT "
                            + ((beginTime!=null&&beginTime.length()>0)?"where MODIFIEDTIME >= '" + beginTime
                                    + "' or  creationtime >= '" + beginTime + "' or  TS >= '" + beginTime + "'":"") + 
                            " connect by prior PK_DEPT=PK_FATHERORG start with PK_FATHERORG='~'";
                    
                    log.debug("BnDeptSync=="+sql);
                
                } catch (SQLException e) {
                    log.error("同步部门时，报错：" + e.getMessage());
                } finally {
                    if (conn != null) {
                        try {
                            conn.close();
                        } catch (SQLException e) {
                            log.error("同步部门时，关闭数据库连接失败：" + e.getMessage());
                        }
                    }
                }
            } else {
                log.error("同步部门信息时，未能找到hr数据源，请检查相关配置");
            }
        } else {
            log.error("同步部门信息时，未能找到hr数据源载体Bean，请检查相关配置");
        }
        return dataList;
    }

    @Override
    protected Bean prepareData(Bean data) {
        //判断同步过来的部门所属机构是否存在系统中
        if (!BnOrgCacheUtil.containsOrg(data.getStr("PK_ORG"))) {
            log.error("部门:" + data.getStr("NAME") + "，编码:" + data.getStr("PK_DEPT") + "不属于百年人寿公司及分公司，不同步。");
            return null;
        }
        Bean newDept = new Bean();
        //部门主键
        newDept.set("DEPT_CODE", data.getStr("PK_DEPT"));
        //部门名称
        newDept.set("DEPT_NAME", data.getStr("NAME"));
        //部门全称
        newDept.set("DEPT_FULL_NAME", data.getStr("NAME"));
        //部门类型为部门
        newDept.set("DEPT_TYPE", "1");
        if (data.getStr("PK_FATHERORG").length() <= 0 || "~".equals(data.getStr("PK_FATHERORG"))) {//无上级部门
            //获取所属机构信息
            Bean orgBean = ServDao.find("SY_ORG_DEPT", data.getStr("PK_ORG"));
            if (orgBean == null || orgBean.isEmpty()) {
                log.error("部门：" + data.getStr("NAME") + "，未找到所属机构：" + data.getStr("PK_ORG"));
                return null;
            }
            //部门层级
            newDept.set("DEPT_LEVEL", orgBean.getInt("DEPT_LEVEL") +1);
            //部门编码路径
            newDept.set("CODE_PATH", orgBean.getStr("CODE_PATH") + data.getStr("PK_DEPT") + "^");
            //上级部门编码
            newDept.set("DEPT_PCODE", orgBean.getStr("DEPT_CODE"));
            //顶级部门编码
            newDept.set("TDEPT_CODE", data.getStr("PK_DEPT"));
        } else {
            Bean pDept = ServDao.find("SY_ORG_DEPT", data.getStr("PK_FATHERORG"));
            if (pDept == null || pDept.isEmpty()) {
                log.error("部门：" + data.getStr("NAME") + "，未找到上级部门：" + data.getStr("PK_FATHERORG"));
                return null;
            }
            //部门层级
            newDept.set("DEPT_LEVEL", pDept.getInt("DEPT_LEVEL") +1);
            //部门编码路径
            newDept.set("CODE_PATH", pDept.getStr("CODE_PATH") + data.getStr("PK_DEPT") + "^");
            //上级部门编码
            newDept.set("DEPT_PCODE", pDept.getStr("DEPT_CODE"));
            //顶级部门编码
            newDept.set("TDEPT_CODE", pDept.getStr("DEPT_CODE"));
        }
        //所属机构编码
        newDept.set("ODEPT_CODE", data.getStr("PK_ORG"));
        //所属公司
        newDept.set("CMPY_CODE", "zhbx");
        //创建人为系统管理员
        newDept.set("S_USER", "1Xvd5e5X50O8J0Ir-0zlwd");
        //判断启用状态
        if (2 == data.getInt("ENABLESTATE") && "N".equals(data.getStr("HRCANCELED"))) {
            //启用，且未被撤销
            newDept.set("S_FLAG", 1);
        } else {
            //否则，处理为禁用
            newDept.set("S_FLAG", 2);
        }
        //判断是否已存在，如存在，则先删除，后添加
        Bean oldBean = ServDao.find(getImplServId(), newDept.getStr("DEPT_CODE"));
        if (oldBean != null && !oldBean.isEmpty()) {
            //获取原有的顺序号
            newDept.set("DEPT_SORT", oldBean.getInt("DEPT_SORT"));
            //删除旧数据
            ServDao.destroy(getImplServId(), oldBean);
        }
        return newDept;
    }

    @Override
    protected boolean isDeleted() {
        return true;
    }

    @Override
    protected String getImplServId() {
        return "SY_ORG_DEPT";
    }

    @Override
    protected void logDataSyncState(Bean dataBean, int syncFlag, String syncDesc) {
        // 插入日志表
        Bean logBean = new Bean();
        logBean.set("DATA_ID", dataBean.getStr("DEPT_CODE"));
        logBean.set("SERV_ID", getImplServId());
        logBean.set("SYNC_FLAG", syncFlag);
        logBean.set("SYNC_DESC", syncDesc);
        logBean.set("SYNC_TIME", DateUtils.getDatetime());
        ServDao.create(getLogServId(), logBean);
    }

}
