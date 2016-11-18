/*
 * Copyright (c) 2012 Ruaho All rights reserved.
 */
package com.rh.core.comm.schedule.serv;

import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.base.TipException;
import com.rh.core.comm.ScheduleHelper;
import com.rh.core.comm.schedule.mgr.ScheduleMgr;
import com.rh.core.org.mgr.OrgMgr;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServMgr;
import com.rh.core.util.Constant;
import com.rh.core.util.Lang;

/**
 * 计划调度服务类
 * @author liwei
 */
public class SchedJobServ extends SchedBaseServ {

    /** log */
    private static Log log = LogFactory.getLog(SchedJobServ.class);

    /** job service id */
    private static final String CURRENT_SERVICE = "SY_COMM_SCHED";

    /**
     * @see com.rh.core.serv.CommonServ#query(com.rh.core.base.Bean)
     * @param param bean
     * @return result bean
     */
    public OutBean query(ParamBean param) {
        OutBean result = super.query(param);
        Set<JobKey> allJobs;
        try {
            // get current all jobs size
            int currentSize = ServDao.count(CURRENT_SERVICE, new Bean());
            allJobs = ScheduleMgr.getInstance().getAllJobs();
            // if schedule manager' jobs size != databases's jobs size
            // we sync job with schedule manager
            if (currentSize != allJobs.size()) {
                log.warn("the jos's size in db not match with schedule manager, we attempt to restore");
                syncJobs(allJobs, currentSize);
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 添加一个任务
     * @param param 参数bean，包含job信息
     * @return 添加结果
     */
    public OutBean save(ParamBean param) {
        // TODO param validation
        param.set("serv", CURRENT_SERVICE);
        String classStr = param.getStr(JOB);
        String jobName = param.get(JOB_PK, Lang.getUUID());
        String jobDesc = param.getStr(JOB_DESC);
        boolean recovery = false;
        if (1 == param.getInt(JOB_RECOVERY)) {
            recovery = true;
        }
        String dataParam = param.getStr(JOB_DATA);
        JobDataMap srcData = null;
        JobDataMap data = valueOfMap(dataParam);

        if (!param.getAddFlag()) { // edit & save
            JobDetail exitsJob = null;
            try {
                JobKey jobKey = ScheduleHelper.getJobKey(param.getId());
                exitsJob = ScheduleMgr.getInstance().getJob(jobKey);
            } catch (SchedulerException e) {
                throw new TipException(" load job failed.");
            }
            if (!param.contains(JOB)) {
                classStr = exitsJob.getJobClass().toString().replace("class ", "");
            }
            // can not update jobName
            // if (!param.contains(JOB_KEY)) {
            jobName = exitsJob.getKey().getName();
            // }
            if (!param.contains(JOB_DESC)) {
                jobDesc = exitsJob.getDescription();
            }
            if (!param.contains(JOB_DATA)) {
            		srcData = exitsJob.getJobDataMap();
            		data = srcData;
            } else {
            		srcData = exitsJob.getJobDataMap();
            }

            if (!param.contains(JOB_RECOVERY)) {
                recovery = exitsJob.requestsRecovery();
            }

        } else { // create & save
            param.set("JOB_STATE", "STOP");
        }

        if (!param.contains(JOB_PK)) {
            param.set(JOB_PK, jobName);
        }

        // job cmpys status schema key in bean
        // 所属公司? 1:不指定,2:全公司,3:自定义 <br>
        // 任务所属公司配置,指定后可从任务上下文环境中读取.<br>
        int cmpyStatus = param.get(JOB_CMPYS_STATUS, 1);
        if (1 == cmpyStatus) {
            data.put(ScheduleMgr.CONTEXT_CMPYS_KEY, null);
        } else if (2 == cmpyStatus) {
            List<Bean> orgList = OrgMgr.getAllCmpys();
            String[] cmpys = new String[orgList.size()];
            for (int i = 0; i < orgList.size(); i++) {
                Bean org = orgList.get(i);
                cmpys[i] = org.getStr("CMPY_CODE");
            }
            data.put(ScheduleMgr.CONTEXT_CMPYS_KEY, cmpys);
        } else if (3 == cmpyStatus) {
            String cmpysStr = param.getStr(JOB_CMPYS);
            if (cmpysStr.lastIndexOf(",") == cmpysStr.length()) {
                cmpysStr = cmpysStr.substring(0, cmpysStr.length() - 1);
            }
            String[] cmpys = cmpysStr.split(",");
            data.put(ScheduleMgr.CONTEXT_CMPYS_KEY, cmpys);
        }

        // 执行用户
        String contextUser = param.getStr(JOB_CONTEXT_USER);
        if (0 < contextUser.length()) {
            data.put(JOB_CONTEXT_USER, contextUser);
        } else {
            if (null != srcData) {
                Object user = srcData.get(JOB_CONTEXT_USER);
                data.put(JOB_CONTEXT_USER, user);
            }
        }

        OutBean outBean;
        JobDetail jobDetail = null;
        // build job
        try {
            jobDetail = ScheduleHelper.buildJob(classStr, jobName, jobDesc, data, recovery);
        } catch (ClassNotFoundException e) {
            throw new TipException("the job class param invalid, " + classStr);
        } catch (SchedulerException se) {
            throw new RuntimeException("the schedule instance error", se);
        }

        // add job
        try {
            param.set("SCHED_NAME", ScheduleMgr.getInstance().getSchedulerName());
            param.set("JOB_GROUP", jobDetail.getKey().getGroup());
            outBean = super.save(param);
            ScheduleMgr.getInstance().add(jobDetail);
        } catch (SchedulerException e) {
            throw new RuntimeException("add job failed", e);
        } catch (Exception e) {
            throw new RuntimeException("add job failed", e);
        }
        return outBean;
    }

    /**
     * 删除指定job
     * @param param 参数Bean， 包含JobName
     * @return 删除结果
     */
    public OutBean delete(ParamBean param) {
        OutBean outBean;
        JobKey[] jobs = getJobKeyArray(param);
        try {
            ScheduleMgr.getInstance().remove(jobs);
            param.setServId(CURRENT_SERVICE);
            outBean = super.delete(param);
        } catch (SchedulerException e) {
            throw new RuntimeException("remove job failed", e);
        }
        return outBean;

    }

    /**
     * 查询指定job
     * @param param 参数Bean， 包含JobName
     * @return 查询结果
     */
    public OutBean byid(ParamBean param) {
        // TODO check job state match?
        JobKey job = getJobKey(param);
        OutBean result = super.byid(param);
        // 如果为新建
        if (0 == result.getId().length()) {
            return result;
        }
        boolean matched = false;
        JobDetail exitsJob = null;
        try {
            exitsJob = ScheduleMgr.getInstance().getJob(job);
        } catch (SchedulerException e) {
            throw new RuntimeException(Context.getSyMsg("SY_SCHED_ERROR"), e);
        }
        if (null != exitsJob) {
            matched = equals(result, exitsJob);
        }
        if (!matched) {
            ServDao.deletes(CURRENT_SERVICE, param);
            log.warn(" the job not matched with schedule manager, we delete it and revert at next query.");
            result.setError("this job has been deleted ");
        }
        return result;
    }

    /**
     * 暂停job
     * @param param 参数Bean，包含jobName
     * @return 处理结果
     */
    public OutBean pauseJob(ParamBean param) {
        JobKey[] jobs = getJobKeyArray(param);
        try {
            ScheduleMgr.getInstance().pauseJob(jobs);
            updateJobAndTriggersState(jobs);
        } catch (Exception e) {
            throw new RuntimeException("pause job failed", e);
        }
        OutBean outBean = new OutBean(param);
        outBean.setOk(Context.getSyMsg(SY_PAUSE_OPERATION_SUCCESSFUL));
        return outBean;
    }

    /**
     * 恢复job
     * @param param 参数Bean， 包含job name
     * @return 处理结果
     */
    public OutBean resumeJob(ParamBean param) {
        JobKey[] jobs = getJobKeyArray(param);
        try {
            ScheduleMgr.getInstance().resumeJob(jobs);
            updateJobAndTriggersState(jobs);
        } catch (Exception e) {
            throw new RuntimeException("resume job failed", e);
        }
        OutBean out = new OutBean();
        out.setOk(Context.getSyMsg(SY_START_OPERATION_SUCCESSFUL));
        return out;
    }

    /**
     * 终止任务
     * @param param 参数Bean，包含jobName
     * @return 处理结果
     */
    public OutBean interruptJobs(ParamBean param) {
        JobKey[] jobs = getJobKeyArray(param);
        try {
            ScheduleMgr.getInstance().interruptJobs(jobs);
            updateJobAndTriggersState(jobs);
        } catch (Exception e) {
            throw new RuntimeException("interrupt job failed", e);
        }
        OutBean outBean = new OutBean(param);
        outBean.setOk(Context.getSyMsg(SY_PAUSE_OPERATION_SUCCESSFUL));
        return outBean;
    }

    /**
     * 任务初始化
     */
    public void init() {
        if (!ScheduleMgr.getInstance().isStarted()) {
            return;
        }

        Set<JobKey> allJobs = null;
        try {
            allJobs = ScheduleMgr.getInstance().getAllJobs();
        } catch (SchedulerException e) {
            throw new RuntimeException("schedule manager get all jobs failed.", e);
        }

        if (null != allJobs && 0 < allJobs.size()) {
            return;
        }
        SchedTriggerServ triggerServ = new SchedTriggerServ();
        OutBean outBean = ServMgr.act(new ParamBean(CURRENT_SERVICE, "finds"));
        List<Bean> jobs = null;
        if (outBean != null) {
            jobs = outBean.getList(Constant.RTN_DATA);
        }
        if (null == jobs || 0 == jobs.size()) {
            return;
        }
        log.warn("---Schedule data missing, we start restore ...");
        for (Bean job : jobs) {
            // sync job to schedule manager
            String classStr = job.getStr(JOB);
            String jobName = job.get(JOB_PK, Lang.getUUID());
            String jobDesc = job.getStr(JOB_DESC);
            boolean recovery = false;
            if (1 == job.getInt(JOB_RECOVERY)) {
                recovery = true;
            }
            String dataParam = job.getStr(JOB_DATA);
            JobDataMap data = valueOfMap(dataParam);
            JobDetail jobDetail = null;
            // build job
            try {
                jobDetail = ScheduleHelper.buildJob(classStr, jobName, jobDesc, data, recovery);
                ScheduleMgr.getInstance().add(jobDetail);
            } catch (ClassNotFoundException e) {
                throw new TipException("the job class param invalid, " + classStr);
            } catch (SchedulerException se) {
                throw new RuntimeException("the schedule instance error", se);
            }
            // sync job's triggers
            triggerServ.init(jobDetail.getKey());
        }
    }

    /**
     * value of bean
     * @param job <CODE>JobDetail</CODE>
     * @param state state string
     * @return bean object
     */
    protected Bean valueOfBean(JobDetail job, String state) {
        Bean result = new Bean();
        result.set(JOB_PK, job.getKey().getName());
        result.set(JOB_GROUP, job.getKey().getGroup());
        result.set(JOB, job.getJobClass().toString().replace("class ", ""));
        result.set(JOB_DESC, job.getDescription());
        String jobData = job.getJobDataMap().getWrappedMap().toString();
        jobData = jobData.substring(1, jobData.length() - 1);
        result.set(JOB_DATA, jobData);
        if (job.requestsRecovery()) {
            result.set(JOB_RECOVERY, "1");
        } else {
            result.set(JOB_RECOVERY, "2");
        }
        try {
            result.set("SCHED_NAME", ScheduleMgr.getInstance().getSchedulerName());
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        job.requestsRecovery();
        if (null != state && 0 < state.length()) {
            result.set("JOB_STATE", state);
        }
        return result;

    }

    /**
     * sync all jobs with schedule manager
     * @param allJobs all jobs key
     * @param currentSize current jobs size in the database
     * @throws SchedulerException throws this exception, if schedule instance error
     */
    protected void syncJobs(Set<JobKey> allJobs, int currentSize) throws SchedulerException {
        int increment = 0;
        String allIds = "";
        for (JobKey job : allJobs) {
            Bean exits = ServDao.find(CURRENT_SERVICE, job.getName());
            if (null == exits) {
                String state = ScheduleMgr.getInstance().getState(job);
                // if not exits, we insert this job into database
                Bean addBean = valueOfBean(ScheduleMgr.getInstance().getJob(job), state);
                ServDao.create(CURRENT_SERVICE, addBean);
                increment++;
            }
            allIds += ("'" + job.getName() + "',");
        }
        if ((currentSize + increment) > allJobs.size()) {
            Bean delParam = new Bean();
            allIds = allIds.substring(0, allIds.length() - 1);
            String condition = " AND JOB_CODE NOT IN (" + allIds + ")";
            condition += "  AND SCHED_NAME='" + ScheduleMgr.getInstance().getSchedulerName() + "'";
            delParam.set(Constant.PARAM_WHERE, condition);
            ServDao.deletes(CURRENT_SERVICE, delParam);
        }
    }

    /**
     * bean and trigger compare the main attributes
     * @param bean Bean
     * @param target Trigger
     * @return is matched?
     */
    protected boolean equals(Bean bean, JobDetail target) {
        Bean exitsJobBean = valueOfBean(target, "");
        if (!equals(bean, exitsJobBean, JOB)) {
            log.warn(JOB + " job field :" + bean.get(JOB) + " != " + exitsJobBean.get(JOB) + "["
                    + bean.get(JOB_PK) + "]");
            return false;
        }
        if (!equals(bean, exitsJobBean, JOB_DESC)) {
            log.warn(JOB_DESC + " job field :" + bean.get(JOB_DESC) + " != " + exitsJobBean.get(JOB_DESC)
                    + "[" + bean.get(JOB_PK) + "]");
            return false;
        }
        if (!equals(bean, exitsJobBean, JOB_RECOVERY)) {
            log.warn(JOB_RECOVERY + " job field :" + bean.get(JOB_RECOVERY) + " != "
                    + exitsJobBean.get(JOB_RECOVERY) + "[" + bean.get(JOB_PK) + "]");
            return false;
        }
        // if (!equals(bean, exitsJobBean, JOB_DATA, "MAP")) {
        // log.warn(JOB_DATA + " job field :" + bean.get(JOB_DATA) + " != " + exitsJobBean.get(JOB_DATA)
        // + "[" + bean.get(JOB_PK) + "]");
        // return false;
        // }

        return true;
    }

}
