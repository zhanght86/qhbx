/*
 * Copyright (c) 2011 Ruaho All rights reserved.
 */
package com.rh.core.comm.schedule.mgr;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.Trigger.TriggerState;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;

import com.rh.core.base.Context;
import com.rh.core.base.Context.APP;
import com.rh.core.base.Context.DS;

/**
 * schedule 管理类 这是一个Singleton,无法new instance
 * <p>
 * <code>add</code> <code>remove</code>操作之前必须先调用<code>start</code> 来启动任务计划管理器.
 * </p>
 * <p>
 * 本类提供的job接口类型是quartz schedule类型。可通过ScheduleHelper来生成Job相关对象。 之所以使用quartz schedule类型作为接口类型，是将quartz作为框架的一部分来考虑。
 * </p>
 * @author liwei
 */
public class ScheduleMgr {

    /** 任务设置公司变量 key */
    public static final String CONTEXT_CMPYS_KEY = "CONTEXT_CMPYS";
    /** 任务执行情况说明 key */
    public static final String CONTEXT_JOB_EXECUTED_DESC_KEY = "CURRENT_JOB_DESC";

    /** singleton */
    private static ScheduleMgr instance = new ScheduleMgr();
    /** status */
    private boolean started = false;

    /**
     * 
     * @return 是否启动了
     */
    public boolean isStarted() {
        return started;
    }

 
    /** global scheduler */
    private Scheduler globalSched = null;
    
    /** jvm scheduler */
    private Scheduler localSched = null;
    
    /** jvm scheduler job's status cache */
    private static Hashtable<String, String> localJobStatus = new Hashtable<String, String>();

    /** log */
    private static Log log = LogFactory.getLog(ScheduleMgr.class);

    /**
     * can not new instance
     */
    private ScheduleMgr() {

    }

    /**
     * singleton
     * @return instance
     */
    public static ScheduleMgr getInstance() {
        return instance;
    }

    /**
     * start service
     * 
     * @throws SchedulerException if service not started throws this exception
     */
    public void start() throws SchedulerException {
        if (started) {
            log.warn(" start failed, the scheduleMgr is already running.");
        } else {
            Properties prop = null;
            prop = Context.getProperties(Context.app(APP.WEBINF) + "quartz.properties");
            
            //com.rh.sc.startflag 是否通过jvm控制启动 任务计划
            String scStartFlag = prop.getProperty("com.rh.sc.startflag");
            String jvmPro = System.getProperty("com.rh.sc.startflag", "");
            
            if (null == scStartFlag || scStartFlag.equalsIgnoreCase("false") 
                    || (scStartFlag.equalsIgnoreCase("true") && jvmPro.equalsIgnoreCase("true"))) {
                // 动态指定系统缺省数据源为任务调度使用的数据源
                prop.setProperty("org.quartz.dataSource.myDS.jndiURL", Context.getDSBean().getStr(DS.FULL_NAME));
                // First we must get a reference to a scheduler
                SchedulerFactory sf = new StdSchedulerFactory(prop);
                globalSched = sf.getScheduler();
                globalSched.start();
                log.info("the global scheduler started");
                // scheduler listener
                // sched.getListenerManager().addSchedulerListener(schedulerListener)
                started = true;
            }
            
            //start jvm scheduler
            Properties localJobProp = null;
            localJobProp = Context.getProperties(Context.app(APP.WEBINF) + "local_job.properties");
            SchedulerFactory sf = new StdSchedulerFactory(localJobProp);
            localSched = sf.getScheduler();
            localSched.start();
            log.info("the  local scheduler started");
        }
    }

    /**
     * shutdown
     * @throws SchedulerException if service is not started throw this exception
     */
    public void shutdown() throws SchedulerException {
        if (!started) {
            log.warn(" Is ScheduleMgr running? Stop aborted..");
        } else {
            globalSched.shutdown();
            localSched.shutdown();
            started = false;
        }
    }

    /**
     * add job
     * @param jobDetail jobdetail
     * @throws SchedulerException if schedule job failed, throws this exception
     */
    public void add(JobDetail jobDetail) throws SchedulerException {
        checkStatus();
        globalSched.addJob(jobDetail, true);
        log.debug(" add job:" + jobDetail.getKey());
    }
    
    
    /**
     * get all local jobs
     * @return return all job
     * @throws SchedulerException throws this exception, if schedule invalid
     */
    public Set<JobKey> getAllLocalJobs() throws SchedulerException {
        Set<JobKey> result = new HashSet<JobKey>();
        List<String> groupList = localSched.getJobGroupNames();
        for (String group : groupList) {
            Set<JobKey> jobs = localSched.getJobKeys(GroupMatcher.jobGroupEquals(group));
            result.addAll(jobs);
            jobs.clear();
        }
        return result;
    }
    
    /**
     * 根据<CODE>jobKey</CODE>获取其目前运行状态
     * @param job - job key
     * @return status string
     */
    public String getLocalJobStatus(JobKey job) {
        return  localJobStatus.get(job.toString());
    }
    
    /**
     * get local schedule name
     * @return scheduler name
     * @throws SchedulerException throws this exception ,if get failed
     */

    public String getLocalSchedulerName() throws SchedulerException {
        return localSched.getSchedulerName();
    }
    
    /**
     * 设置目标本地任务的状态
     * @param job - job key
     * @param status - status
     */
    public void setLocalJobStatus(JobKey job, String status) {
        localJobStatus.put(job.toString(), status);
    }
    
    /**
     * 根据key 获取<CODE>JobDetail</CODE>对象
     * @param jobKey 指定<CODE>JobKey</CODE>
     * @return 所指定的<CODE>JobKey</CODE>所对应的<CODE>JobDetail</CODE>
     * @throws SchedulerException 若当前schedule instance不可用会抛出该异常
     */
    public JobDetail getLocalJob(JobKey jobKey) throws SchedulerException {
        return localSched.getJobDetail(jobKey);
    }

    /**
     * add job in local schedule
     * @param jobDetail jobdetail
     * @throws SchedulerException if schedule job failed, throws this exception
     */
    public void addLocalJob(JobDetail jobDetail) throws SchedulerException {
        localSched.addJob(jobDetail, true);
        log.debug(" add local job:" + jobDetail.getKey());
    }
    

    /**
     * @param job jobkey
     * @throws SchedulerException if remove job failed, throws this exception
     */
    public void removeLocalJob(JobKey job) throws SchedulerException {
        localSched.resumeJob(job);
        localSched.deleteJob(job);
        log.debug(" remove local job:" + job);
    }
    

    /**
     * add job Schedule the given <code>{@link org.quartz.Trigger}</code> with the <code>Job</code> identified by the
     * <code>Trigger</code>'s settings.
     * @param trigger trigger
     * @throws SchedulerException if schedule job failed, throws this exception
     */
    public void addLocalTrigger(Trigger trigger) throws SchedulerException {
        localSched.scheduleJob(trigger);
    }

    /**
     * delete local trigger
     * @param trigger the key of <CODE>Trigger</CODE>
     * @throws SchedulerException if remove trigger failed, throws this exception
     */
    public void removeLocalTrigger(TriggerKey trigger) throws SchedulerException {
        localSched.unscheduleJob(trigger);
        log.debug(" unschedule local job:" + trigger);
    }
    

    /**
     * add job Schedule the given <code>{@link org.quartz.Trigger}</code> with the <code>Job</code> identified by the
     * <code>Trigger</code>'s settings.
     * @param trigger trigger
     * @throws SchedulerException if schedule job failed, throws this exception
     */
    public void add(Trigger trigger) throws SchedulerException {
        checkStatus();
        globalSched.scheduleJob(trigger);
    }

    /**
     * @param jobs jobkey array
     * @throws SchedulerException if remove job failed, throws this exception
     */
    public void remove(JobKey[] jobs) throws SchedulerException {
        checkStatus();
        for (JobKey job : jobs) {
            globalSched.resumeJob(job);
            globalSched.deleteJob(job);
        }
    }

    /**
     * @param job jobkey
     * @throws SchedulerException if remove job failed, throws this exception
     */
    public void remove(JobKey job) throws SchedulerException {
        checkStatus();
        globalSched.resumeJob(job);
        globalSched.deleteJob(job);
    }

    /**
     * delete trigger
     * @param trigger the key of <CODE>Trigger</CODE>
     * @throws SchedulerException if remove trigger failed, throws this exception
     */
    public void remove(TriggerKey trigger) throws SchedulerException {
        checkStatus();
        globalSched.unscheduleJob(trigger);
    }

    /**
     * delete trigger
     * @param triggers targer trigger array for delete
     * @throws SchedulerException if remove trigger failed, throws this exception
     */
    public void remove(TriggerKey[] triggers) throws SchedulerException {
        checkStatus();
        for (TriggerKey trigger : triggers) {
            globalSched.unscheduleJob(trigger);
        }
    }

    /**
     * pause job
     * @param jobs jobkey array
     * @throws SchedulerException throws this exception, if pause job failed
     */
    public void pauseJob(JobKey[] jobs) throws SchedulerException {
        checkStatus();
        for (JobKey job : jobs) {
            globalSched.pauseJob(job);
        }
    }

    /**
     * pause trigger
     * @param triggers triggerKey array
     * @throws SchedulerException throws this exception, if pause job failed
     */
    public void pauseTrigger(TriggerKey[] triggers) throws SchedulerException {
        checkStatus();
        for (TriggerKey trigger : triggers) {
            globalSched.pauseTrigger(trigger);
        }
    }

    /**
     * resume jobs
     * @param jobs job Key array
     * @throws SchedulerException throw this exception, if resume job failed
     */
    public void resumeJob(JobKey[] jobs) throws SchedulerException {
        checkStatus();
        for (JobKey job : jobs) {
            globalSched.resumeJob(job);
        }
    }

    /**
     * resume trigger
     * @param triggers job Key array
     * @throws SchedulerException throw this exception, if resume job failed
     */
    public void resumeTrigger(TriggerKey[] triggers) throws SchedulerException {
        checkStatus();
        for (TriggerKey trigger : triggers) {
            globalSched.resumeTrigger(trigger);
        }
    }

    /**
     * get all jobs
     * @return return all job
     * @throws SchedulerException throws this exception, if schedule invalid
     */
    public Set<JobKey> getAllJobs() throws SchedulerException {
        checkStatus();
        Set<JobKey> result = new HashSet<JobKey>();
        List<String> groupList = globalSched.getJobGroupNames();
        for (String group : groupList) {
            Set<JobKey> jobs = globalSched.getJobKeys(GroupMatcher.jobGroupEquals(group));
            result.addAll(jobs);
            jobs.clear();
        }
        // LinkedList list = new LinkedList();
        return result;
    }

    /**
     * 根据key 获取<CODE>JobDetail</CODE>对象
     * @param jobKey 指定<CODE>JobKey</CODE>
     * @return 所指定的<CODE>JobKey</CODE>所对应的<CODE>JobDetail</CODE>
     * @throws SchedulerException 若当前schedule instance不可用会抛出该异常
     */
    public JobDetail getJob(JobKey jobKey) throws SchedulerException {
        checkStatus();
        return globalSched.getJobDetail(jobKey);
    }

    /**
     * 获取激活的<CODE>JobKey</CODE>列表 所谓激活的job是指还在schedule(在未来的时间还会执行)中的job。
     * @return 活动的<CODE>JobKey</CODE>列表
     * @throws SchedulerException 若当前schedule实例异常会抛出该异常
     */
    public Set<JobKey> getEnabledJobList() throws SchedulerException {
        checkStatus();
        Set<JobKey> allJob = getAllJobs();
        Set<JobKey> result = new HashSet<JobKey>();
        for (JobKey job : allJob) {
            List<Trigger> triggers = getTriggersOfJob(job);
            if (null != triggers && 0 < triggers.size()) {
                result.add(job);
            }
        }
        log.debug("alive job size:" + result.size());
        return result;
    }

    /**
     * 获取当前活动的job信息列表
     * @return 当前正在活动job的信息列表
     * @throws SchedulerException 若当前scheduler服务异常时会抛出该异常
     */
    public List<JobExecutionContext> getCurrentlyExecutingJobs() throws SchedulerException {
        checkStatus();
        List<JobExecutionContext> result = globalSched.getCurrentlyExecutingJobs();
        log.debug("currently executing jobs size:" + result.size());
        return result;
    }

    /**
     * 停止正在运行的任务
     * @param jobKey - <CODE>JobKey</CODE>
     * @return 是否终止成功
     * @throws SchedulerException 若当前schedule实例异常会抛出该异常
     */
    public boolean interruptJob(JobKey jobKey) throws SchedulerException {
        checkStatus();
        return globalSched.interrupt(jobKey);
    }

    /**
     * 停止正在运行的任务
     * @param jobs - <CODE>JobKey</CODE>
     * @throws SchedulerException 若当前schedule实例异常会抛出该异常
     */
    public void interruptJobs(JobKey[] jobs) throws SchedulerException {
        checkStatus();
        for (JobKey job : jobs) {
            globalSched.interrupt(job);
        }
    }

    /**
     * 获取指定job所关联的trigger
     * @param jobKey jobkey
     * @return 该job所关联的trigger list
     * @throws SchedulerException 若当前scheduler服务异常时会抛出该异常
     */
    public List<Trigger> getTriggersOfJob(JobKey jobKey) throws SchedulerException {
        checkStatus();
        @SuppressWarnings("unchecked")
        List<Trigger> triggers = (List<Trigger>) globalSched.getTriggersOfJob(jobKey);
        if (null == triggers) {
            return triggers;
        }

        // ignore recovery trigger
        List<Trigger> recoTrigs = new ArrayList<Trigger>();
        for (Trigger trig : triggers) {
            if (isRecoveryTrigger(trig.getKey())) {
                recoTrigs.add(trig);
            }
        }
        // delete from triggers
        if (0 < recoTrigs.size()) {
            triggers.removeAll(recoTrigs);
            recoTrigs.clear();
        }
        return triggers;
    }

    /**
     * 获取指定trigger的状态
     * @param triggerKey 指定的<CODE>TriggerKey</CODE>
     * @return <CODE>TriggerState</CODE> 包含：NONE, NORMAL, PAUSED, COMPLETE, ERROR, BLOCKED
     * @throws SchedulerException 若当前scheduler服务异常时会抛出该异常
     */
    public TriggerState getTriggerState(TriggerKey triggerKey) throws SchedulerException {
        checkStatus();
        return globalSched.getTriggerState(triggerKey);
    }

    /**
     * 获取指定trigger
     * @param triggerKey 指定的<CODE>TriggerKey</CODE>
     * @return <CODE>Trigger</CODE>
     * @throws SchedulerException 若当前scheduler服务异常时会抛出该异常
     */
    public Trigger getTrigger(TriggerKey triggerKey) throws SchedulerException {
        return globalSched.getTrigger(triggerKey);
    }

    /**
     * 查询指定Job是否存在
     * @param jobKey 指定Job key
     * @return 如果已存在返回true
     * @throws SchedulerException 若当前scheduler服务异常时会抛出该异常
     */
    public boolean checkExits(JobKey jobKey) throws SchedulerException {
        return globalSched.checkExists(jobKey);
    }

    /**
     * 查询指定trigger是否存在
     * @param trigKey 指定trigger key
     * @return 如果已存在返回true
     * @throws SchedulerException 若当前scheduler服务异常时会抛出该异常
     */
    public boolean checkExits(TriggerKey trigKey) throws SchedulerException {
        return globalSched.checkExists(trigKey);
    }

    /**
     * get schedId
     * @return schedule Id
     * @throws SchedulerException throw exception,if the schedule service invalid
     */
    // public String getSchedId() throws SchedulerException {
    // checkStatus();
    // String schedId = "";
    // try {
    // schedId = sched.getSchedulerInstanceId();
    // } catch (Exception ignored) {
    // ignored.printStackTrace();
    // }
    // return schedId;
    // }

    /**
     * clean all job
     * @throws SchedulerException throws this exception ,if clean failed
     */
    public void clean() throws SchedulerException {
        globalSched.clear();
    }

    /**
     * get schedule name
     * @return scheduler name
     * @throws SchedulerException throws this exception ,if get failed
     */

    public String getSchedulerName() throws SchedulerException {
        return globalSched.getSchedulerName();
    }

    /**
     * check status
     * @throws SchedulerException if the schedule service not stated, throws this exception
     */
    private void checkStatus() throws SchedulerException {
        if (!started) {
            throw new SchedulerException("Before using this method ,must be started first by calling start");
        }
    }

    /**
     * @param trigger triggerKey
     * @return stateStr
     * @throws SchedulerException throw this exception, if schedule instance error
     */
    public String getState(TriggerKey trigger) throws SchedulerException {
        // 已完成trigger会自动移出，triggers状态仅包括活动、暂停状态
        String stateStr = "UNKNOW";
        TriggerState trigState = getTriggerState(trigger);
        if (trigState == TriggerState.PAUSED) {
            stateStr = "PAUSED";
        } else if (trigState == TriggerState.NORMAL) {
            stateStr = "ALIVE";
        }
        return stateStr;
    }

    /**
     * @param job jobKey
     * @return stateStr
     * @throws SchedulerException throw this exception, if schedule instance error
     */
    public String getState(JobKey job) throws SchedulerException {
        // 已完成trigger会自动移出，triggers状态仅包括活动、暂停状态
        List<Trigger> triggers = ScheduleMgr.getInstance().getTriggersOfJob(job);
        int pausedIndex = 0;
        String jobStateStr = "STOP";
        for (Trigger trig : triggers) {
            // TODO if is recovery trigger set state is recoverying...
            // if (isRecoveryTrigger(trig.getKey())) {
            TriggerState trigState = ScheduleMgr.getInstance().getTriggerState(trig.getKey());
            if (trigState == TriggerState.PAUSED) {
                pausedIndex++;
            } else if (trigState == TriggerState.NORMAL) {
                jobStateStr = "ALIVE";
            }
        }
        if (pausedIndex == triggers.size() && 0 < triggers.size()) {
            jobStateStr = "PAUSED";
        }
        return jobStateStr;
    }

    /**
     * if recovery trigger ?
     * @param triggerKey Trigger'key
     * @return true or false
     */
    public boolean isRecoveryTrigger(TriggerKey triggerKey) {
        return "RECOVERING_JOBS".equals(triggerKey.getGroup());
    }

}
