/*
 * Copyright (c) 2012 Ruaho All rights reserved.
 */
package com.rh.core.comm.schedule.serv;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.Trigger.TriggerState;
import org.quartz.TriggerKey;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.base.TipException;
import com.rh.core.comm.ScheduleHelper;
import com.rh.core.comm.schedule.mgr.ScheduleMgr;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.util.Constant;

/**
 * 计划调度服务类
 * @author liwei
 */
public class SchedBaseServ extends CommonServ {

	/** job schema key in bean */
	protected static final String JOB = "JOB_CLASS_NAME";
	/** job name schema key in bean */
	protected static final String JOB_PK = "JOB_CODE";
	/** job group name */
	protected static final String JOB_GROUP = "JOB_GROUP";
	/** job description schema key in bean */
	protected static final String JOB_DESC = "DESCRIPTION";
	/** job data schema key in bean */
	protected static final String JOB_DATA = "JOB_DATA";
    /** job cmpys status schema key in bean 
     * 所属公司? 1:不指定,2:全公司,3:自定义 <br>
     * 任务所属公司配置,指定后可从任务上下文环境中读取.<br>
     * */
    protected static final String JOB_CMPYS_STATUS = "JOB_CMPYS_STATUS";
	 /** job cmpys schema key in bean */
    protected static final String JOB_CMPYS = "JOB_CMPYS";
    /** job context user schema key in bean */
    protected static final String JOB_CONTEXT_USER = "JOB_CONTEXT_USER";
	/** job recovery schema key in bean */
	protected static final String JOB_RECOVERY = "REQUESTS_RECOVERY";
	/** job trigger crontab expression (for crontab expression trigger) */
	protected static final String TRIG_CRON_EXPRESSION = "CRONTAB_EXPRESSTION";
	/** job trigger type (simple/crontab expression) */
	protected static final String TRIG_TYPE = "TRIGGER_TYPE";
	/** job trigger start time(for simple trigger) */
	protected static final String TRIG_START_TIME = "START_TIME";
	/** job trigger end time(for simple trigger) */
	protected static final String TRIG_END_TIME = "END_TIME";
	/** job trigger repeat count(for simple trigger) */
	protected static final String TRIG_REPEAT_COUNT = "SIMPLE_TRIGGER_REPEAT_COUNT";
	/** job trigger interval(for simpler trigger) */
	protected static final String TRIG_INTERVAL = "SIMPLE_TRIGGER_INTERVAL";
	/** job trigger name */
	protected static final String TRIG_PK = "TRIGGER_CODE";
	/** job trigger state */
	protected static final String TRIG_STATE = "TRIGGER_STATE";
	/** job trigger group */
	protected static final String TRIG_GROUP = "TRIGGER_GROUP";
	/** previous fire time of trigger */
	protected static final String TRIG_PREV_FIRE_TIME = "PREV_FIRE_TIME";
	/** next fire time of trigger */
	protected static final String TRIG_NEXT_FIRE_TIME = "NEXT_FIRE_TIME";
	/** job trigger description */
	protected static final String TRIG_DESC = "DESCRIPTION";
	/** job state */
	protected static final String SCHED_STATE = "SCHED_STATE";
	/** job complete state */
	protected static final String SCHED_STATE_COMPLETE = SCHED_STATE + "_COMPLETE";
	/** job complete state display value */
	protected static final String SCHED_STATE_COMPLETE_DISPLAY = "SY_STATE_STOPED";
	/** job alive state */
	protected static final String SCHED_STATE_ALIVE = SCHED_STATE + "_ALIVE";
	/** job alive state display value */
	protected static final String SCHED_STATE_ALIVE_DISPLAY = "SY_STATE_ALIVE";
	/** job paused state */
	protected static final String SCHED_STATE_PAUSED = SCHED_STATE + "_PAUDED";
	/** job paused state display value */
	protected static final String SCHED_STATE_PAUSED_DISPLAY = "SY_STATE_PAUSED";
	/** system pause of operation code */
	protected static final String SY_PAUSE_OPERATION_SUCCESSFUL = "SY_PAUSE_OPERATION_SUCCESSFUL";
	/** system start of operation code */
	protected static final String SY_START_OPERATION_SUCCESSFUL = "SY_START_OPERATION_SUCCESSFUL";

	/**
	 * 运行一个计划任务
	 * @param param 参数Bean 包含trigger信息及 JobName
	 * @return 运行结果
	 */
	public Bean scheduleJob(Bean param) {
		// build trigger
		Trigger trigger = buildTrigger(param);
		// add trigger for job
		try {
			ScheduleMgr.getInstance().add(trigger);
		} catch (Exception e) {
			throw new RuntimeException("schedule job failed", e);
		}
		return param;
	}

	/**
	 * 获取JOB执行日志
	 * @param param 参数Bean
	 * @return 查询结果
	 */
	public OutBean getJobHistory(ParamBean param) {
		return new SchedHistoryServ().query(param);
	}

	/**
	 * 获取所有JobList 默认排序按照job状态排序： 活动、已完成
	 * @param param 参数Bean 目前没有使用
	 * @return 结果Bean
	 * @deprecated monitorJobs
	 */
	// TODO paging and cache
	public Bean getJobs(Bean param) {
		Bean result = new Bean();
		LinkedList<Bean> list = new LinkedList<Bean>();
		try {
			Set<JobKey> allJob = ScheduleMgr.getInstance().getAllJobs();
			for (JobKey job : allJob) {
				Bean jobBean = new Bean();
				JobDetail jobDetail = ScheduleMgr.getInstance().getJob(job);
				List<Trigger> triggers = ScheduleMgr.getInstance().getTriggersOfJob(job);
				jobBean.set(JOB_DESC, jobDetail.getDescription());
				jobBean.set(JOB, jobDetail.getJobClass());
				jobBean.set(JOB_PK, jobDetail.getKey().getName());
				jobBean.set(JOB_DATA, jobDetail.getJobDataMap().getWrappedMap());
				jobBean.set(JOB_RECOVERY, jobDetail.requestsRecovery());
				jobBean.set("", "");
				// get job state
				// the job is an stop job, if not has triggers
				if (null == triggers || 0 >= triggers.size()) {
					jobBean.set(SCHED_STATE, SCHED_STATE_COMPLETE);
				} else {
					for (Trigger trig : triggers) {
						TriggerState state = ScheduleMgr.getInstance().getTriggerState(trig.getKey());
						if (state == TriggerState.PAUSED) {
							jobBean.set(SCHED_STATE, SCHED_STATE_PAUSED);
							break;
						} else {
							jobBean.set(SCHED_STATE, SCHED_STATE_ALIVE);
						}
					}

				}
				// order by state alive, complete
				if (jobBean.get(SCHED_STATE).equals(SCHED_STATE_COMPLETE)) {
					list.addLast(jobBean);
				} else {
					list.addFirst(jobBean);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("get jobs from scheduler error.", e);
		}
		result.set(Constant.RTN_DATA, list);
		return result;
	}
	
	/**
	 * 根据参数，提取jobname 并返回<CODE>JobKey</CODE>
	 * @param param 参数Bean
	 * @return 根据参数提取到的<CODE>JobKey</CODE>
	 */
	protected JobKey getJobKey(Bean param) {
		return  getJobKeyArray(param)[0];
	}

	/**
	 * 根据参数，提取jobname 并返回<CODE>JobKey</CODE> array
	 * @param param 参数Bean
	 * @return 根据参数提取到的<CODE>JobKey</CODE> array
	 */
	protected JobKey[] getJobKeyArray(Bean param) {
		String[] jobNames = getIds(param);
		if (0 >= jobNames.length) {
			throw new TipException("'PK' can not be null");
		}
		JobKey[] result = null;
		try {
			result = ScheduleHelper.getJobKeyArray(jobNames);
		} catch (SchedulerException e) {
			throw new RuntimeException("get sched id error", e);
		}
		return result;
	}

	/**
	 * 根据参数，提取triggerName 并返回<CODE>TriggerKey</CODE>
	 * @param param 参数Bean
	 * @return 根据参数提取到的<CODE>TriggerKey</CODE>
	 */
	protected TriggerKey getTriggerKey(Bean param) {
		return getTriggerKeyArray(param)[0];
	}
	
	/**
	 * 根据参数，提取triggerName 并返回<CODE>TriggerKey</CODE>array
	 * @param param 参数Bean
	 * @return 根据参数提取到的<CODE>TriggerKey</CODE>
	 */
	protected TriggerKey[] getTriggerKeyArray(Bean param) {
		// TODO trigGroup

		String[] trigNames = getIds(param);
		if (0 >= trigNames.length) {
			throw new TipException("'_PK_' can not be null");
		}
		TriggerKey[] result = null;
		try {
			result = ScheduleHelper.getTriggerKeyArray(trigNames);
		} catch (SchedulerException e) {
			throw new RuntimeException("get sched id error", e);
		}
		return result;
	}

	/**
	 * no paging
	 * @param paramBean param参数
	 * @param size 数据量
	 * @return 构造的分页Bean
	 */
	protected Bean getPageData(Bean paramBean, int size) {
		final int maxNumber = 1000;
		Bean page = (Bean) paramBean.get("LPAGE");
		if (page == null) { // 初始化页面设定
			page = new Bean();
			page.set("NOWPAGE", 1);
			page.set("SHOWNUM", maxNumber);
			page.set("ALLNUM", size);
			page.set("PAGES", 1);
		}
		return page;
	}

	/**
	 * 创建trigger
	 * @param param 参数Bean， 包含trigger信息
	 * @return 根据参数构建的trigger
	 */
	protected Trigger buildTrigger(Bean param) {
		Trigger trigger = null;
		String jobName = param.getStr(JOB_PK);
		JobKey jobKey = getJobKey(param);
		String trigDesc = param.getStr(TRIG_DESC);

		String startTimeParam = param.getStr(TRIG_START_TIME);
		Date triggerStartTime = parseDate(startTimeParam);

		String endTimeParam = param.getStr(TRIG_END_TIME);
		Date triggerEndTime = parseDate(endTimeParam);

		int repeatCount = param.getInt(TRIG_REPEAT_COUNT);
		int interval = param.getInt(TRIG_INTERVAL);

		// TODO add crontab expression trigger parser
		try {
			trigger = ScheduleHelper.buildTrigger("trigger_" + jobName, trigDesc, jobKey, triggerStartTime,
					triggerEndTime, repeatCount, interval);
		} catch (SchedulerException se) {
			throw new RuntimeException("the schedule instance error", se);
		}
		return trigger;
	}

	/** date format expresstion **/
	private static final String DATE_FORMAT_PATTEN = "yyyy-MM-dd HH:mm:ss";

	/**
	 * get Date
	 * @param timeParam time string ,format: yyyy-mm-dd HH:MM:SS
	 * @return <CODE>Date</CODE>
	 */
	protected Date parseDate(String timeParam) {
		Date trigTime = null;
		if (0 < timeParam.length()) {
			try {
				SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_PATTEN, Locale.getDefault());

				trigTime = format.parse(timeParam);
			} catch (ParseException e) {
				throw new TipException("date format must be '" + DATE_FORMAT_PATTEN + "', date:" + timeParam);
			}
		}
		return trigTime;

	}

	/**
	 * get date String
	 * @param date <CODE>Date</CODE>
	 * @return date str
	 */
	protected String formatDate(Date date) {
		String timeString = "";
		if (null != date) {
			SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_PATTEN, Locale.getDefault());
			timeString = format.format(date);
		}
		return timeString;

	}

	/**
	 * extracting data from link param
	 * @param param param of link
	 * @return param map
	 */
	protected Map<String, String> extractParamFromLink(Bean param) {
		String data = param.getStr("_linkWhere");
		Map<String, String> paramInData = null;
		if (0 < data.length()) {
			paramInData = new HashMap<String, String>();
			String[] paramArray = data.split(" and");
			// for each param item
			for (String item : paramArray) {
				String[] tmp = item.replace("'", "").trim().split("=");
				if (2 == tmp.length) {
					paramInData.put(tmp[0], tmp[1]);
				}
			}
		}
		return paramInData;
	}

	/**
	 * clean up all job resource
	 * @throws SchedulerException throws this exception ,if the schedule instance error
	 */
	public void cleanup() throws SchedulerException {
		ScheduleMgr.getInstance().clean();
		String schedName = ScheduleMgr.getInstance().getSchedulerName();
		ServDao.deletes("SY_COMM_SCHED", new Bean().set("SCHED_NAME", schedName));
		ServDao.deletes("SY_COMM_SCHED_TRIGGER", new Bean().set("SCHED_NAME", schedName));
		ServDao.deletes("SY_COMM_SCHED_HIS", new Bean().set("SCHED_NAME", schedName));
	}

	/**
	 * update all of job's triggers state
	 * @param jobs <CODE>Job</CODE>
	 * @throws SchedulerException throws this exception, if schedule instance error
	 */
	public void updateJobAndTriggersState(JobKey[] jobs) throws SchedulerException {
		for (JobKey job : jobs) {
			updateJobAndTriggersState(job);
		}
	}

	/**
	 * update all of job's triggers state
	 * @param job <CODE>Job</CODE>
	 * @throws SchedulerException throws this exception, if schedule instance error
	 */
	public void updateJobAndTriggersState(JobKey job) throws SchedulerException {
		// 已完成trigger会自动移出，triggers状态仅包括活动、暂停状态
		List<Trigger> triggers = ScheduleMgr.getInstance().getTriggersOfJob(job);
		int pausedIndex = 0;
		String jobStateStr = "STOP";
		for (Trigger trig : triggers) {
			String trigStateStr = "STOP";
			TriggerState trigState = ScheduleMgr.getInstance().getTriggerState(trig.getKey());
			if (trigState == TriggerState.PAUSED) {
				trigStateStr = "PAUSED";
				pausedIndex++;
			} else if (trigState == TriggerState.NORMAL) {
				trigStateStr = "ALIVE";
				jobStateStr = "ALIVE";
			}
			updateState(trig, trigStateStr);
		}
		if (pausedIndex == triggers.size() && 0 < triggers.size()) {
			jobStateStr = "PAUSED";
		}
		updateState(job, jobStateStr);
	}

	/**
	 * update all of job state
	 * @param job <CODE>JobKey</CODE>
	 * @param state the state String
	 */
	public void updateState(JobKey job, String state) {
		Bean param = new Bean();
		param.set("JOB_NAME", job.getName());
		param.set("JOB_STATE", state);
		param.setId(job.getName());
		ServDao.update("SY_COMM_SCHED", param);
	}
	
	/**
	 * update job state base on it's triggers
	 * @param job job key
	 * @throws SchedulerException throws this exception,if schedule instance error
	 */
	public void updateState(JobKey job) throws SchedulerException {
		String stateStr = ScheduleMgr.getInstance().getState(job);
		updateState(job, stateStr);
	}

	/**
	 * update trigger state
	 * @param trigger Trigger
	 * @throws SchedulerException throws this exception,if schedule instance error
	 */
	public void updateState(Trigger trigger) throws SchedulerException {
		String stateStr = ScheduleMgr.getInstance().getState(trigger.getKey());
		updateState(trigger, stateStr);

	}

	/**
	 * update trigger state
	 * @param trigger Trigger
	 * @param state state String
	 * @throws SchedulerException throws this exception,if schedule instance error
	 */
	public void updateState(Trigger trigger, String state) throws SchedulerException {
		Bean param = new Bean().setId(trigger.getKey().getName());
		if (null != trigger.getNextFireTime()) {
			param.set("NEXT_FIRE_TIME", new Timestamp(trigger.getNextFireTime().getTime()));
		}
		if (null != trigger.getPreviousFireTime()) {
			param.set("PREV_FIRE_TIME", new Timestamp(trigger.getPreviousFireTime().getTime()));
		}
		param.set("TRIGGER_STATE", state);
		ServDao.update("SY_COMM_SCHED_TRIGGER", param);
	}

	/**
	 * get paused state display message
	 * @return message string
	 */
	protected String getPausedStateMsg() {
		return Context.getSyMsg(SCHED_STATE_PAUSED_DISPLAY);
	}

	/**
	 * get complete state display message
	 * @return message string
	 */
	protected String getCompleteStateMsg() {
		return Context.getSyMsg(SCHED_STATE_COMPLETE_DISPLAY);
	}

	/**
	 * get alive state display message
	 * @return message string
	 */
	protected String getAliveStateMsg() {
		return Context.getSyMsg(SCHED_STATE_ALIVE_DISPLAY);
	}

	/**
	 * a'value equals b'value with the key
	 * @param a bean
	 * @param b bean
	 * @param key key
	 * @param type values's type
	 * @return is matched
	 */
	protected boolean equals(Bean a, Bean b, String key, String type) {
		if ("DATETIME".equals(type)) {
			Date dateA = null;
			Date dateB = null;
			try {
				dateA = parseDate(a.getStr(key));
			} catch (Exception e) {
				dateA = null;
			}
			try {
				dateB = parseDate(b.getStr(key));
			} catch (Exception e) {
				dateB = null;
			}
			if (null == dateA) {
				return null == dateB;
			} else if (null == dateB) {
				return null == dateA;
			} else {
				return 0 == dateA.compareTo(dateB);
			}
		} else if ("MAP".equals(type)) {
			return valueOfMap(a.getStr(key)).equals(valueOfMap(b.getStr(key).toString()));
		} else {
			return a.getStr(key).equals(b.getStr(key));
		}
	}

	/**
	 * a'value equals b'value with the key
	 * @param a bean
	 * @param b bean
	 * @param key key
	 * @return is matched
	 */
	protected boolean equals(Bean a, Bean b, String key) {
		return a.getStr(key).equals(b.getStr(key));
	}

	/**
	 * valueOf map
	 * @param dataStr format key1=value1,key2=value2
	 * @return map
	 */
	protected JobDataMap valueOfMap(String dataStr) {
		JobDataMap data = new JobDataMap();
		if (null != dataStr && 0 < dataStr.length()) {
			String[] items = dataStr.split(",");
			for (String item : items) {
				String[] tmp = item.split("=");
				if (2 == tmp.length) {
					data.put(tmp[0].trim(), tmp[1].trim());
				}
			}
		}
		return data;
	}

	/**
	 * get ids
	 * @param paramBean param bean
	 * @return id array
	 */
	private String[] getIds(Bean paramBean) {
		String[] ids = paramBean.getId().trim().split(Constant.SEPARATOR);
		return ids;
	}

}
