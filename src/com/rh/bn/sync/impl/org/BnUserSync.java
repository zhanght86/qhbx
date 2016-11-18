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
 * 百年人寿项目用户同步类
 * @author Tanyh 20151009
 *
 */
public class BnUserSync extends AbstractSync{

    /**日志对象**/
    Log log = LogFactory.getLog(BnUserSync.class);
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
                    List<Bean> usList = OracleExecutor.getExecutor().query(
                            conn,
                            "select a.PK_PSNDOC AS USER_CODE, " +
                            "a.NAME AS USER_NAME, " +
                            "a.EMAIL AS USER_EMAIL, " +
                            "max(b.ts) as TS "+
                            "from BD_PSNDOC a,hi_PSNJOB b,om_post c,bd_psncl d where a.PK_PSNDOC=b.PK_PSNDOC and b.LASTFLAG='Y' and ( 1=1 " + 
                            ((beginTime != null && beginTime.length() > 0) ? " and b.MODIFIEDTIME >= '" + beginTime + "'" : "") +
                            ((beginTime != null && beginTime.length() > 0) ? " or a.CREATIONTIME >= '" + beginTime + "'" : "") +
                            ((beginTime != null && beginTime.length() > 0) ? " or a.TS >= '" + beginTime + "'" : "") +
                            ")  and b.PK_POST=c.PK_POST(+) and b.PK_PSNCL=d.PK_PSNCL(+) group by a.PK_PSNDOC,a.NAME,a.EMAIL");  
                    for (int i = 0; i < usList.size(); i++) {//由于人员的当前有效状态有多个，取最新的。
						Bean uBean = usList.get(i);
						String user_code = uBean.getStr("USER_CODE");
						String user_name = uBean.getStr("USER_NAME");
						String user_email = uBean.getStr("USER_EMAIL");
						String ts = uBean.getStr("TS");
						List<Bean> listbBeans = OracleExecutor.getExecutor().query(
								conn,getSql(user_code, user_name, user_email, beginTime,ts));  
						dataList.addAll(listbBeans);
					}
                } catch (SQLException e) {
                    log.error("同步用户时，报错：" + e.getMessage());
                } finally {
                    if (conn != null) {
                        try {
                            conn.close();
                        } catch (SQLException e) {
                            log.error("同步用户时，关闭数据库连接失败：" + e.getMessage());
                        }
                    }
                }
            } else {
                log.error("同步用户信息时，未能找到hr数据源，请检查相关配置");
            }
        } else {
            log.error("同步用户信息时，未能找到hr数据源载体Bean，请检查相关配置");
        }
        return dataList;
    }

    
    public String getSql(String user_code,String user_name,String user_email,String beginTime,String ts) {
    	 String sql =  "select a.PK_PSNDOC AS USER_CODE," +
                 "a.NAME AS USER_NAME," +
                 "a.SEX AS USER_SEX," +
                 "b.CLERKCODE AS USER_WORK_NUM," +
                 "b.PK_DEPT AS DEPT_CODE," +
                 "a.PK_ORG AS ODEPT_CODE," +
                 "a.OFFICEPHONE AS USER_OFFICE_PHONE," +
                 "a.MOBILE AS USER_MOBILE," +
                 "a.EMAIL AS USER_EMAIL," +
                 "c.POSTNAME AS JOBNAME," +
                 "a.ENABLESTATE AS USER_STATE," +
                 "b.POSTSTAT AS POST_STAT," +
                 "b.MODIFIEDTIME AS MODIFIEDTIME," +
                 "d.CODE AS USER_STATE_TYPE," +
                 "1 AS OPERATE_TYPE from BD_PSNDOC a,hi_PSNJOB b,om_post c,bd_psncl d " +
                 "where  a.PK_PSNDOC = '"+user_code+"' and a.EMAIL = '"+user_email+"'  and  a.PK_PSNDOC=b.PK_PSNDOC " +
                 " and b.ts = '"+ts+"' and b.LASTFLAG='Y' and ( 1=1 " +
                 ((beginTime != null && beginTime.length() > 0) ? "and b.MODIFIEDTIME >= '" + beginTime + "'" : "") +
                 ((beginTime != null && beginTime.length() > 0) ? "or a.CREATIONTIME >= '" + beginTime + "'" : "") +
                   ((beginTime != null && beginTime.length() > 0) ? "or a.TS >= '" + beginTime + "'" : "") +
                 ")  and b.PK_POST=c.PK_POST(+) and b.PK_PSNCL=d.PK_PSNCL(+) order by b.MODIFIEDTIME asc ";
    	 log.debug("BnUserSync=="+user_name+" : "+sql);
    	 return sql;
	}
   
    @Override
    protected Bean prepareData(Bean data) {
        //判断同步过来的用户所属机构是否存在系统中
        if (!BnOrgCacheUtil.containsOrg(data.getStr("ODEPT_CODE"))) {
            log.error("用户:" + data.getStr("USER_NAME") + "，编码:" + data.getStr("USER_CODE") + "不属于百年人寿公司及分公司，不同步。");
            return null;
        }
        Bean userBean = new Bean();
        //获取用户所属部门信息
        Bean deptBean = ServDao.find("SY_ORG_DEPT", data.getStr("DEPT_CODE"));
        if (deptBean == null || deptBean.isEmpty()) {
            log.error("用户：" + data.getStr("USER_NAME") + "，未找到所属部门：" + data.getStr("DEPT_CODE"));
            return null;
        }
        //用户主键
        userBean.set("USER_CODE", data.getStr("USER_CODE"));
        //用户名
        userBean.set("USER_NAME", data.getStr("USER_NAME"));
        // 设置用户邮箱名为系统登录名，如果用户没有填写邮箱，则取用户工号
        if (data.getStr("USER_EMAIL").length() > 0) {
            userBean.set("USER_LOGIN_NAME", data.getStr("USER_EMAIL").split("@")[0]);
        } else {
            userBean.set("USER_LOGIN_NAME", data.getStr("USER_WORK_NUM"));
        }
        //用户全拼
        userBean.set("USER_SPELLING", userBean.getStr("USER_LOGIN_NAME"));
        //性别
        userBean.set("USER_SEX", data.getStr("USER_SEX"));
        //所属部门code
        userBean.set("DEPT_CODE", data.getStr("DEPT_CODE"));
        //用户密码 
        userBean.set("USER_PASSWORD", "ad32a707be4a4e9ffd5c53d06c546aa1");// 用户初始密码为aq12wsde3
        //用户邮箱
        userBean.set("USER_EMAIL", data.getStr("USER_EMAIL"));
        //用户工号
        userBean.set("USER_WORK_NUM", data.getStr("USER_WORK_NUM"));
        //用户手机号
        userBean.set("USER_MOBILE", data.getStr("USER_MOBILE"));
         //用户排序默认为999999
        userBean.set("USER_SORT", "999999");
        //用户办公电话
        userBean.set("USER_OFFICE_PHONE", data.getStr("USER_OFFICE_PHONE"));
        //职务
        userBean.set("USER_POST", data.getStr("JOBNAME"));
        // 设置用户在职状态：只有离职的人员不能登录，其他人员都可以登录
        if ("2".equals(data.getStr("USER_STATE")) && "Y".equals(data.getStr("POST_STAT"))) {// 在职
            userBean.set("USER_STATE", 1);
            userBean.set("S_FLAG", 1);
        } else {
            if ("07".equals(data.getStr("USER_STATE_TYPE"))) {// 离职
                userBean.set("USER_STATE", 2);
                userBean.set("S_FLAG", 2);
            } else if ("08".equals(data.getStr("USER_STATE_TYPE"))) {
                //退休
//                userBean.set("USER_STATE",1);
//                userBean.set("S_FLAG", 1);
               userBean.set("USER_STATE", 3);
               userBean.set("S_FLAG", 2);
            } else {// 非正常用户
                userBean.set("USER_STATE",1);
                userBean.set("S_FLAG", 1);
//                userBean.set("USER_STATE", 4);
//                userBean.set("S_FLAG", 2);
            }
        }
        //所属公司
        userBean.set("CMPY_CODE", "zhbx");
        //创建人为系统管理员
        userBean.set("S_USER", "1Xvd5e5X50O8J0Ir-0zlwd");
        //判断是否已存在，如存在，则先删除，后添加
        Bean oldBean = ServDao.find(getImplServId(), userBean.getStr("USER_CODE"));
        if (oldBean != null && !oldBean.isEmpty()) {
            //获取原有的顺序号
            userBean.set("USER_SORT", oldBean.getInt("USER_SORT"));
            //获取原有的密码
            userBean.set("USER_PASSWORD", oldBean.getInt("USER_PASSWORD"));
            //删除旧数据
            ServDao.destroy(getImplServId(), oldBean);
        }
        return userBean;
    }

    @Override
    protected boolean isDeleted() {
        return true;
    }

    @Override
    protected String getImplServId() {
        return "SY_ORG_USER";
    }

    @Override
    protected void logDataSyncState(Bean dataBean, int syncFlag, String syncDesc) {
        // 插入日志表
        Bean logBean = new Bean();
        logBean.set("DATA_ID", dataBean.getStr("USER_CODE"));
        logBean.set("SERV_ID", getImplServId());
        logBean.set("SYNC_FLAG", syncFlag);
        logBean.set("SYNC_DESC", syncDesc);
        logBean.set("SYNC_TIME", DateUtils.getDatetime());
        ServDao.create(getLogServId(), logBean);
    }

}
