/*
 * Copyright (c) 2011 Ruaho All rights reserved.
 */
package com.rh.oa.sso;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.rh.client.RHClient;
import com.rh.client.RHSyncData;
import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.base.TipException;
import com.rh.core.comm.schedule.job.InitializeContextJob;
import com.rh.core.org.UserBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServMgr;
import com.rh.core.util.Constant;
import com.rh.core.util.DateUtils;
import com.rh.core.util.TaskLock;

/**
 * 定时同步帐号、角色、组织机构等信息
 * @author liuxinhe
 * modified by chensheng -- 2013-6-14
 */
public class AccountJob extends InitializeContextJob {

    /**
     * 日志对象
     */
    private static Log log = LogFactory.getLog(AccountJob.class);
    /**
     * 软虹客户端
     */
    private RHClient client = null;
    /**
     * 同步数据对象
     */
    private RHSyncData syncData = null;
    /**
     * 门户地址
     */
    private String portalURL = null;
    /**
     * 子系统ID
     */
    private String sysId = null;
    /**
     * 公司CODE
     */
    private String cmpyCode = null;
    /**
     * 用户CODE
     */
    private String userLoginName = null;
    /**
     * 用户密码
     */
    private String password = null;
    /**
     * 用户服务
     */
    private static final String SY_ORG_USER = "SY_ORG_USER";
    /**
     * 部门服务
     */
    private static final String SY_ORG_DEPT = "SY_ORG_DEPT";
    /**
     * 角色服务
     */
    private static final String SY_ORG_ROLE = "SY_ORG_ROLE";
    /**
     * 同步成功
     */
    private static final int PT_SYNC_FLAG_OK = 1;
    /**
     * 同步失败
     */
    private static final int PT_SYNC_FLAG_ERROR = 3;
    /**
     * 返回数据总数
     */
    private static int size = 0;
    
    /**
     * 设置门户地址
     * @param portalURL 门户地址
     */
    public void setPortalURL(String portalURL) {
        this.portalURL = portalURL;
    }
    
    /**
     * 设置系统ID
     * @param sysId 系统ID
     */
    public void setSysId(String sysId) {
        this.sysId = sysId;
    }
    
    /**
     * 设置公司CODE
     * @param cmpyCode 公司CODE
     */
    public void setCmpyCode(String cmpyCode) {
        this.cmpyCode = cmpyCode;
    }
    
    /**
     * 设置同步用户登录名
     * @param userLoginName 用户登录名
     */
    public void setUserLoginName(String userLoginName) {
        this.userLoginName = userLoginName;
    }
    
    /**
     * 设置同步用户登录密码
     * @param password 用户登录密码
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取client便于退出
     * @return 返回
     */
    public RHClient getClient() {
        return client;
    }

    /**
     * 构造函数
     */
    public AccountJob() {
    }

    /**
     * 实现Job方法，进行定义调度处理
     * @param context 调度上下文信息
     * @throws JobExecutionException 当例外发生
     */
    @Override
    protected void executeJob(JobExecutionContext context) throws JobExecutionException {
//        UserBean userBean = (UserBean) Context.getThread(THREAD.USERBEAN);
//        System.out.println(userBean.getName());
        JobDataMap jobData = context.getJobDetail().getJobDataMap();
        sysId = jobData.getString("SYS_ID");
        portalURL = Context.getSyConf("SY_PORTAL_IURL", null);
        if (portalURL == null) { // 如果接口地址没有配置则取正常的门户地址
            log.error("门户接口地址系统变量SY_PORTAL_IURL未配置，尝试取门户地址SY_PORTAL_URL！");
            portalURL = Context.getSyConf("SY_PORTAL_URL", null);
            if (portalURL == null) {
                log.error("门户接口地址系统变量SY_PORTAL_IURL以及门户地址系统变量SY_PORTAL_URL均未配置！");
                throw new RuntimeException("门户接口地址系统变量SY_PORTAL_IURL以及门户地址系统变量SY_PORTAL_URL均未配置！");
            }
        }
        cmpyCode =  jobData.getString("CMPY");
        userLoginName = jobData.getString("USER");
        password = jobData.getString("PWD");
        if (StringUtils.isBlank(cmpyCode) || StringUtils.isBlank(userLoginName) || StringUtils.isBlank(password)) {
            log.error("同步的用户信息未配置！");
            throw new RuntimeException("同步的用户信息未配置！");
        }
        
        // 开始同步门户数据
        boolean locked = false;
        TaskLock lock = null;
        lock = new TaskLock("JOB", FilenameUtils.getName("AccountJob"));
        locked = lock.lock();
        try {
            if (locked) {
                this.syncData();
            }
        } catch (Exception e) {
            // 如果不是最后一条数据抛出的异常则记录日志
            if (!(e instanceof TipException)) {
                log.error(e.getMessage(), e);
            }
        } finally {
            lock.release();
            try {
                if (this.client != null) {
                    this.client.logout();
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        log.debug("返回的数据总量：" + size);
    }

    /**
     * 同步
     * @throws Exception 
     */
    public void syncData() throws Exception {
        // 首先登录到门户
        this.login2Portal();
        // 获取同步数据的对象
        syncData = new RHSyncData(client, sysId);
        // 查询满足条件的数据变更
        if (client.isLogin()) {
            Iterator<Map<String, String>> it = syncData.getData();
            while (it.hasNext()) {
                Map<String, String> logMap = it.next();
                String logId = logMap.get("LOG_ID");
                String desc = logMap.get("DESCRIPTION");
                if (logId != null && logId.length() > 0) {
                    try {
                        this.handleData(logMap);
                    } catch (Exception e) {
                        String msg = "e.getMessage()返回为null";
                        if (e.getMessage() != null) {
                            msg = e.getMessage();
                        }
                        log.error("同步数据异常，" + msg, e);
                        String stackTrace = "";
                        for (StackTraceElement elem : e.getStackTrace()) {  
                            stackTrace += elem;  
                        }  
                        // 如果没有登录则退出
                        if (msg.indexOf("请先登录系统") >= 0) {
                            return;
                        }
                        this.isError(logId, desc + "@@同步数据异常，" + msg + stackTrace);
                        
                        // 最后一条数据如果还异常则抛出异常，这样就可以捕获异常退出登录
                        if (!it.hasNext()) { 
                            throw new TipException("同步数据异常，" + msg);
                        }
                    }
                } else {
                    this.isError(logId, desc + "@@同步数据异常，变更数据ID为空！");
                }
            }
        }
    }
    
    /**
     * 登录到门户
     */
    private void login2Portal() {
        client = new RHClient(portalURL);
        try {
            client.login(cmpyCode, userLoginName, password);
        } catch (Exception e) {
            log.error("登录异常，" + e.getMessage(), e);
            try {
                client.logout();
            } catch (Exception e1) {
                log.error("退出异常，", e1);
            }
        } 
    }
    
    /**
     * 处理变更的数据
     * @param logMap 变更日志
     * @throws Exception 异常
     */
    private void handleData(Map<String, String> logMap) throws Exception {
        if (syncData.isDept(logMap)) {
            this.handleDept(logMap);
        } else if (syncData.isUser(logMap)) {
            this.handleUser(logMap);
        } else if (syncData.isRole(logMap)) {
            this.handleRole(logMap);
        } 
    }

    /**
     * 处理部门数据
     * @param logMap 变更数据
     * @throws Exception 异常
     */
    private void handleDept(Map<String, String> logMap) throws Exception {
        Map<String, String> deptMap = syncData.getDept(logMap);
        if (deptMap != null) {
            size++;
            // 供EntityMgr用
            String odeptCode = deptMap.get("ODEPT_CODE");
            if (deptMap.get("S_ODEPT") == null) {
                deptMap.put("S_ODEPT", odeptCode);
            }
            if (deptMap.get("QUERY_ODEPT") == null) {
                deptMap.put("QUERY_ODEPT", odeptCode);
            }
            
            String sql = " and DEPT_CODE='" + deptMap.get("DEPT_CODE") + "'";
            
//            String sql = " and (DEPT_CODE='" + deptMap.get("DEPT_CODE")
//                    + "' or DEPT_SRC_TYPE3='" + deptMap.get("DEPT_SRC_TYPE3") + "')";
            
            Bean existBean = this.isExist(SY_ORG_DEPT, sql);
            if (existBean != null) { // 如果该条数据已经存在
                deptMap.put(Constant.KEY_ID, existBean.getId());
                deptMap.put("DEPT_CODE", existBean.getId());
                this.hasDataHandler(SY_ORG_DEPT, logMap, deptMap);
            } else {
                this.noDataHandler(SY_ORG_DEPT, logMap, deptMap);
            }
        }
    }
    
    /**
     * 处理用户数据
     * @param logMap 变更数据
     * @throws Exception 
     */
    private void handleUser(Map<String, String> logMap) throws Exception {
        Map<String, String> userMap = syncData.getUser(logMap);
        if (userMap != null) {
            size++;
             // 供EntityMgr用
            String odeptCode = userMap.get("ODEPT_CODE");
            if (userMap.get("S_ODEPT") == null) {
                userMap.put("S_ODEPT", odeptCode);
            }
            if (userMap.get("QUERY_ODEPT") == null) {
                userMap.put("QUERY_ODEPT", odeptCode);
            }
            
            userMap.put("USER_PASSWORD", this.getPassword(userMap));
            String sql = " and USER_CODE='" + userMap.get("USER_CODE") + "'";
            
//            String sql = " and (USER_CODE='" + userMap.get("USER_CODE")
//                    + "' or (CMPY_CODE='zhbx' and USER_LOGIN_NAME='"
//                    + userMap.get("USER_LOGIN_NAME") + "'))";
            
            Bean existBean = this.isExist(SY_ORG_USER, sql);
            // 日志描述
            if (existBean != null) { // 如果该条数据已经存在
                userMap.put(Constant.KEY_ID, existBean.getId());
                userMap.put("USER_CODE", existBean.getId());
                /*
                 * 如果离职则把S_FLAG改成2
                 */
                String userState = userMap.get("USER_STATE");
                if (StringUtils.isNotBlank(userState) && userState == "2") {
                    userMap.put("S_FLAG", "2");
                }
                this.hasDataHandler(SY_ORG_USER, logMap, userMap);
            } else {
                this.noDataHandler(SY_ORG_USER, logMap, userMap);
            }
        }
    }
    
    /**
     * 处理角色数据
     * @param logMap 变更数据
     * @throws Exception 异常
     */
    private void handleRole(Map<String, String> logMap) throws Exception {
        Map<String, String> roleMap = syncData.getRole(logMap);
        if (roleMap != null) {
            size++;
            // 供EntityMgr用，角色不属于任何一个机构，所以用公共用户superadmin的机构
            Bean user = ServDao.find("SY_ORG_USER", new ParamBean().set("USER_LOGIN_NAME", "superadmin"));
            UserBean userBean = new UserBean(user);
            String odeptCode = userBean.getODeptCode();
            if (roleMap.get("S_ODEPT") == null) {
                roleMap.put("S_ODEPT", odeptCode);
            }
            if (roleMap.get("QUERY_ODEPT") == null) {
                roleMap.put("QUERY_ODEPT", odeptCode);
            }
            
            String sql = " and ROLE_CODE='" + roleMap.get("ROLE_CODE") + "'";
            Bean existBean = this.isExist(SY_ORG_ROLE, sql);
            // 日志描述
            if (existBean != null) { // 如果该条数据已经存在
                roleMap.put(Constant.KEY_ID, existBean.getId());
                roleMap.put("ROLE_CODE", existBean.getId());
                this.hasDataHandler(SY_ORG_ROLE, logMap, roleMap);
            } else {
                this.noDataHandler(SY_ORG_ROLE, logMap, roleMap);
            }
        }
    }
    
    /**
     * 判断数据存不存在
     * @param servId 服务ID
     * @param sql 查询语句
     * @return 返回条数
     */
    private Bean isExist(String servId, String sql) {
        ParamBean paramBean = new ParamBean();
        paramBean.set(Constant.PARAM_WHERE, sql);
        return ServDao.find(servId, paramBean);
    }
    
    /**
     * 数据存在时的处理方法
     * @param servId 更新的服务ID
     * @param logMap 变更数据
     * @param dataMap 操作数据
     * @throws Exception 异常
     */
    private void hasDataHandler(String servId, Map<String, String> logMap, Map<String, String> dataMap) 
            throws Exception {
        String logId = logMap.get("LOG_ID");
        String desc = logMap.get("DESCRIPTION");
        if (syncData.isAdd(logMap) || syncData.isModify(logMap)) {
            this.modify(servId, dataMap, logId, desc);
        } else if (syncData.isDelete(logMap)) {
            this.delete(servId, dataMap, logId, desc);
        } else if (syncData.isDestory(logMap)) {
            this.destroy(servId, dataMap, logId, desc);
        }
    }
    
    /**
     * 数据不存在时的处理方法
     * @param servId 更新的服务ID
     * @param logMap 变更数据
     * @param dataMap 操作数据
     * @throws Exception 异常
     */
    private void noDataHandler(String servId, Map<String, String> logMap, Map<String, String> dataMap)
            throws Exception {
        String logId = logMap.get("LOG_ID");
        String desc = logMap.get("DESCRIPTION");
        if (syncData.isAdd(logMap) || syncData.isModify(logMap)) {
            this.add(servId, dataMap, logId, desc);
        } else if (syncData.isDelete(logMap) || syncData.isDestory(logMap)) {
            String logDesc = desc + "@@该数据在OA系统中不存在，不允许禁用或者删除！";
            this.addLog(logId, PT_SYNC_FLAG_OK, logDesc);
            log.error(logDesc);
        }
    }
    
    /**
     * 添加数据
     * @param servId 服务ID
     * @param dataMap 数据
     * @param logId 日志ID
     * @param desc 原来的描述
     * @throws Exception 
     */
    private void add(String servId, Map<String, String> dataMap, String logId, String desc) throws Exception {
        ParamBean paramBean = this.getParamBean(servId, dataMap, ServMgr.ACT_SAVE);
        paramBean.setAddFlag(true);
        ServMgr.act(paramBean);
        this.isOk(logId, desc + "@@添加成功");
    }
    
    /**
     * 修改数据
     * @param servId 服务ID
     * @param dataMap 数据
     * @param logId 日志ID
     * @param desc 原来的描述
     * @throws Exception 
     */
    private void modify(String servId, Map<String, String> dataMap, String logId, String desc) throws Exception {
        ServMgr.act(this.getParamBean(servId, dataMap, ServMgr.ACT_SAVE));
        this.isOk(logId, desc + "@@修改成功");
    }
    
    /**
     * 禁用数据
     * @param servId 服务ID
     * @param dataMap 数据
     * @param logId 日志ID
     * @param desc 原来的描述
     * @throws Exception 
     */
    private void delete(String servId, Map<String, String> dataMap, String logId, String desc) throws Exception {
        dataMap.put("S_FLAG", "2");
        ServMgr.act(this.getParamBean(servId, dataMap, ServMgr.ACT_SAVE));
        this.isOk(logId, desc + "@@禁用成功");
    }
    
    /**
     * 删除数据
     * @param servId 服务ID
     * @param dataMap 数据
     * @param logId 日志ID
     * @param desc 原来的描述
     * @throws Exception 
     */
    private void destroy(String servId, Map<String, String> dataMap, String logId, String desc) throws Exception {
        ServMgr.act(this.getParamBean(servId, dataMap, ServMgr.ACT_DELETE));
        this.isOk(logId, desc + "@@删除成功");
    }
    
    /**
     * 获取操作参数Bean
     * @param servId 服务ID
     * @param dataMap 操作数据
     * @param act 操作
     * @return 返回操作Bean
     */
    private ParamBean getParamBean(String servId, Map<String, String> dataMap, String act) {
        ParamBean paramBean = new ParamBean();
        paramBean.putAll(dataMap);
        paramBean.setAct(act);
        paramBean.setServId(servId);
        // 单条ACT操作启用事物处理
        paramBean.setTransFlag(true);
        return paramBean;
    }
    
    /**
     * 同步成功
     * @param logId 日志ID
     * @param logDesc 日志内容
     * @throws Exception 
     */
    private void isOk(String logId, String logDesc) throws Exception {
        if (logDesc == null || logDesc.length() == 0) {
            logDesc = "同步成功";
        }
        this.addLog(logId, PT_SYNC_FLAG_OK, logDesc);
    }
    
    /**
     * 同步失败
     * @param logId 日志ID
     * @param logDesc 日志内容
     * @throws Exception 
     */
    private void isError(String logId, String logDesc) throws Exception {
        if (logDesc == null || logDesc.length() == 0) {
            logDesc = "同步失败";
        }
        this.addLog(logId, PT_SYNC_FLAG_ERROR, logDesc);
    }
    
    /**
     * 添加操作日志
     * @param logId 日志ID
     * @param syncFlag 日志类型
     * @param logDesc 日志描述
     * @throws Exception 
     */
    private void addLog(String logId, int syncFlag, String logDesc) throws Exception {
        Map<String, Object> logMap  = new HashMap<String, Object>();
        logMap.put("SYNC_FLAG", syncFlag);
        logMap.put("DESCRIPTION", logDesc);
        logMap.put("SYNC_TIME", DateUtils.getDatetime());
        syncData.modify(logId, logMap);
    }
    
    /**
     * 获取门户里的明码，使用本系统的加密方法加密
     * @param userMap 用户数据Map
     * @return 返回本系统的加密密码
     */
    private String getPassword(Map<String, String> userMap) {
        return syncData.getPassword(userMap);
    }

    public void interrupt() {
    }
    
}
