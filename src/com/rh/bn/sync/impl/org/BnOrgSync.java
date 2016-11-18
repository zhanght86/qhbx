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
 * 百年人寿项目机构同步类
 * @author Tanyh 20151009
 *
 */
public class BnOrgSync extends AbstractSync{

    /** 日志对象 **/
    Log log = LogFactory.getLog(BnOrgSync.class);
    
    @Override
    protected List<Bean> getDataBeanList(String beginTime) {
        List<Bean> dataList = new ArrayList<Bean>();
        //获取配置的人力资源系统数据源载体对象
        Bean dsBean = Context.getDSBean("jdbc/rh_hr");
        if (dsBean != null && !dsBean.isEmpty()) {
            DataSource ds = (DataSource) dsBean.get(DS.DS);
            if (ds != null) {
                //HR系统中百年人寿股份有限公司根编码，只获取百年人寿股份有限公司的数据
                String rootCmpyCode = Context.getSyConf("BN_CMPY_CODE_HR", "0001B210000000000BU3");
                //获取数据库连接
                Connection conn = null;
                try {
                    conn = ds.getConnection();
                    dataList = OracleExecutor.getExecutor().query(
                            conn,
                            "select PK_ORG," +
                                    "NAME," +
                                    "CODE," +
                                    "PK_FATHERORG," +
                                    "ENABLESTATE," +
                                    "MODIFIEDTIME," +
                                    "1 AS OPERATE_TYPE from ORG_ORGS " + 
                                    ((beginTime != null && beginTime.length() > 0)?"where MODIFIEDTIME >= '" + beginTime
                                    		+ "' or  creationtime >= '" + beginTime + "' or  TS >= '" + beginTime + "'" : "")+ 
                                    " connect by prior PK_ORG=PK_FATHERORG start with PK_ORG='" + rootCmpyCode + "'");
      
                    String sql =  "select PK_ORG," +
                            "NAME," +
                            "CODE," +
                            "PK_FATHERORG," +
                            "ENABLESTATE," +
                            "MODIFIEDTIME," +
                            "1 AS OPERATE_TYPE from ORG_ORGS " + 
                            ((beginTime != null && beginTime.length() > 0)?"where MODIFIEDTIME >= '" + beginTime
                            		+ "' or  creationtime >= '" + beginTime + "'  or  TS >= '" + beginTime + "'" : "")+ 
                            " connect by prior PK_ORG=PK_FATHERORG start with PK_ORG='" + rootCmpyCode + "'";
                    
                    log.debug("BnOrgSync=="+sql);
                } catch (SQLException e) {
                    log.error("同步机构时，报错：" + e.getMessage());
                } finally {
                    if (conn != null) {
                        try {
                            conn.close();
                        } catch (SQLException e) {
                            log.error("同步机构时，关闭数据库连接失败：" + e.getMessage());
                        }
                    }
                }
            } else {
                log.error("同步机构信息时，未能找到hr数据源，请检查相关配置");
            }
        } else {
            log.error("同步机构信息时，未能找到hr数据源载体Bean，请检查相关配置");
        }
        return dataList;
    }

    @Override
    protected Bean prepareData(Bean data) {
        //HR系统中百年人寿股份有限公司根编码0001B4100000000074UD
        String rootCmpyCode = Context.getSyConf("BN_CMPY_CODE_HR", "0001B210000000000BU3");
        //百年人寿总公司编码
        String zgsCmpyCode = Context.getSyConf("BN_CMPY_CODE_ZGS_HR", "0001B210000000000BU3");
        Bean orgBean = new Bean();
        if (data.getStr("PK_ORG").equals(rootCmpyCode)) {//根数据的同步
            orgBean.set("DEPT_PCODE", "");
            orgBean.set("DEPT_LEVEL", "1");
            orgBean.set("CODE_PATH", data.getStr("PK_ORG") + "^");
        } else {
            Bean pOrgBean = null;
            /**
             * 考虑到hr正式环境的机构架构与测试环境不一样，如：正式环境中，分公司与总公司同级；测试环境中，分公司归属于总公司下
             * 故同步数据时，需要根据不同的根数据来进行：正式环境，需以总分公司的上级作为根数据；测试环境中，以总公司作为根数据即可。
             * 因此，同步程序中将根数据编码以及总公司编码分别作为可配置的选项，以便兼容正式、测试环境的同步
             */
            if (!zgsCmpyCode.equals(data.getStr("PK_ORG")) && rootCmpyCode.equals(data.getStr("PK_FATHERORG"))) {
                //上级机构为根机构时，除了总公司，其余分公司均归属至总公司下
                pOrgBean = ServDao.find("SY_ORG_DEPT", zgsCmpyCode);
            } else {
                //获取上级机构信息
                pOrgBean = ServDao.find("SY_ORG_DEPT", data.getStr("PK_FATHERORG"));
            }
            if (pOrgBean == null || pOrgBean.isEmpty()) {
                log.error("机构：" + data.getStr("NAME") + "，未找到上级机构：" + data.getStr("PK_FATHERORG"));
                return null;
            }
            orgBean.set("DEPT_PCODE", pOrgBean.getStr("DEPT_CODE"));
            orgBean.set("DEPT_LEVEL", pOrgBean.getInt("DEPT_LEVEL") + 1);
            orgBean.set("CODE_PATH", pOrgBean.getStr("CODE_PATH") + data.getStr("PK_ORG") + "^");
        }
        //部门主键
        orgBean.set("DEPT_CODE", data.getStr("PK_ORG"));
        //部门名称
        orgBean.set("DEPT_NAME", data.getStr("NAME"));
        //部门全称
        orgBean.set("DEPT_FULL_NAME", data.getStr("NAME"));
        //部门类型为机构
        orgBean.set("DEPT_TYPE", "2");
        //所属机构
        orgBean.set("ODEPT_CODE", data.getStr("PK_ORG"));
        //顶级部门
        orgBean.set("TDEPT_CODE", data.getStr("PK_ORG"));
        //所属公司code
        orgBean.set("CMPY_CODE", "zhbx");
        //创建人，系统管理员
        orgBean.set("S_USER", "1Xvd5e5X50O8J0Ir-0zlwd");
        //判断启用状态
        if (2 == data.getInt("ENABLESTATE")) {
            //启用
            orgBean.set("S_FLAG", 1);
        } else {
            //非启用状态，均处理为禁用
            orgBean.set("S_FLAG", 2);
        }
        //判断是否已存在，如存在，则先删除，后添加
        Bean oldBean = ServDao.find(getImplServId(), orgBean.getStr("DEPT_CODE"));
        if (oldBean != null && !oldBean.isEmpty()) {
            //获取原有的顺序号
            orgBean.set("DEPT_SORT", oldBean.getInt("DEPT_SORT"));
            //删除旧数据
            ServDao.destroy(getImplServId(), oldBean);
        }
        //更新缓存中的机构信息
        BnOrgCacheUtil.updateOrgMap(orgBean.getStr("DEPT_CODE"));
        return orgBean;
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
