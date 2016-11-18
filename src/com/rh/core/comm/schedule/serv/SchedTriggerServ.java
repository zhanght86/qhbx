/*
 * Copyright (c) 2012 Ruaho All rights reserved.
 */
package com.rh.core.comm.schedule.serv;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.CronTrigger;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.Trigger.TriggerState;
import org.quartz.TriggerKey;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.base.TipException;
import com.rh.core.comm.ScheduleHelper;
import com.rh.core.comm.schedule.mgr.ScheduleMgr;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.util.Constant;
import com.rh.core.util.Lang;

/**
 * schedule trigger service
 * @author liwei
 */
public class SchedTriggerServ extends SchedBaseServ {

	/** current servid **/
	private static final String CURRENT_SERVICE = "SY_COMM_SCHED_TRIGGER";

	/** log */
	private static Log log = LogFactory.getLog(SchedJobServ.class);

	/** milliseconds scends rate */
	private static final int RATE = 1000;

	/**
	 * @see com.rh.core.serv.CommonServ#query(com.rh.core.base.Bean)
	 * @param param bean
	 * @return result bean
	 */
	public OutBean query(ParamBean param) {
		OutBean result = super.query(param);
		int size = 0;
		Bean pageBean = result.getPage();
		size = pageBean.getInt("ALLNUM");
		JobKey job = getJobKey(param);

		List<Trigger> triggersOfJob;
		try {
			triggersOfJob = ScheduleMgr.getInstance().getTriggersOfJob(job);
			if (size != triggersOfJob.size()) {
				log.warn("the triggers size of job in db not match with schedule manager, " + "we attempt to restore");
				syncTriggers(triggersOfJob, size);
			}
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 查询指定trigger
	 * @param param 参数Bean
	 * @return 查询结果
	 */
	public OutBean byid(ParamBean param) {
		// TODO check trigger state match?
	    OutBean result = super.byid(param);
		  if (0 ==  result.getId().length()) {
              return result;
          }
		  
		boolean matched = false;
		Trigger exitsTrig = null;
		try {
			String pk = param.getId();
			TriggerKey triggerKey = ScheduleHelper.getTriggerKey(pk);
			exitsTrig = ScheduleMgr.getInstance().getTrigger(triggerKey);
		} catch (SchedulerException e) {
			throw new RuntimeException("get trigger failed.", e);
		}
		if (null != exitsTrig) {
			matched = equals(result, exitsTrig);
		}
		// if not found this job,we delete it
		if (!matched) {
			Bean delBean = new Bean();
			delBean.setId(param.getId());
			ServDao.deletes(CURRENT_SERVICE, delBean);
			log.warn(" the trigger not matched with schedule manager, we delete it!");
			OutBean out = new OutBean();
			out.setError("the trigger not found. Please check the log of job history.");
			return out;
		} else {
			return super.byid(param);
		}
	}

	/**
	 * 添加一个Trigger 添加后trigger将启动 新增的trigger将自动运行
	 * @param param 参数bean，包含job信息
	 * @return 添加结果
	 */
	public OutBean save(ParamBean param) {
		// TODO param validation
		// get job params
		boolean isModify = false;
		String jobPk = param.getStr(JOB_PK);
		JobKey jobKey;
		// if param not has pk, we create and set it
		String trigCode = param.get(TRIG_PK, Lang.getUUID());
		if (null != param.getId() && 0 < param.getId().length()) {
			trigCode = param.getId();
		}
		if (!param.contains(TRIG_PK)) {
			param.set(TRIG_PK, trigCode);
		}
		String trigDesc = param.getStr(TRIG_DESC);
		int repeatCount = param.getInt(TRIG_REPEAT_COUNT);
		int trigInterval = param.getInt(TRIG_INTERVAL);
		String startTimeStr = param.getStr(TRIG_START_TIME);
		String endTimeStr = param.getStr(TRIG_END_TIME);
		String cronExpr = param.getStr(TRIG_CRON_EXPRESSION);
		// 1:simple trigger,2:cronatab trigger
		int type = param.getInt(TRIG_TYPE);
		Date startTime = parseDate(startTimeStr);
		Date endTime = parseDate(endTimeStr);

		if (!param.getAddFlag()) { // edit & save
			Trigger exitsTrigger = null;
			isModify = true;
			try {
				TriggerKey trigKey = ScheduleHelper.getTriggerKey(param.getId());
				exitsTrigger = ScheduleMgr.getInstance().getTrigger(trigKey);
			} catch (SchedulerException e) {
				throw new TipException(" load trigger failed.");
			}

			if (!param.contains(TRIG_PK)) {
				trigCode = exitsTrigger.getKey().getName();
			}
			if (!param.contains(TRIG_DESC)) {
				trigDesc = exitsTrigger.getDescription();
			}
			if (!param.contains(TRIG_START_TIME)) {
				startTime = exitsTrigger.getStartTime();
			}
			if (!param.contains(TRIG_END_TIME)) {
				endTime = exitsTrigger.getEndTime();
			}
			if (!param.contains(TRIG_TYPE)) {
				if (exitsTrigger instanceof SimpleTrigger) {
					type = 1;
					SimpleTrigger sTrig = (SimpleTrigger) exitsTrigger;
					if (!param.contains(TRIG_REPEAT_COUNT)) {
						repeatCount = sTrig.getRepeatCount();
					}
					if (!param.contains(TRIG_INTERVAL)) {
						trigInterval = (int) sTrig.getRepeatInterval() / RATE;
					}
				} else {
					type = 2;
					CronTrigger cTrig = (CronTrigger) exitsTrigger;
					if (!param.contains(TRIG_CRON_EXPRESSION)) {
						cronExpr = cTrig.getCronExpression();
					}
				}
			}
			jobKey = exitsTrigger.getJobKey();
		} else { // create & save
			try {
				jobKey = ScheduleHelper.getJobKey(jobPk);
			} catch (SchedulerException se) {
				throw new TipException("jobName is invalid");
			}
		}
		if (null != endTime) {
			param.set(TRIG_END_TIME, new Timestamp(endTime.getTime()));
		}
		Trigger trigger = null;
		// build trigger
		OutBean outBean;
		try {
			if (type == 1) {
				trigger = ScheduleHelper.buildTrigger(trigCode, trigDesc, jobKey, startTime, endTime, repeatCount,
						trigInterval);
			} else {
				trigger = ScheduleHelper.buildTrigger(trigCode, jobKey, trigDesc, cronExpr, startTime, endTime);
			}
			// must has startTime
			param.set(TRIG_START_TIME, new Timestamp(trigger.getStartTime().getTime()));
			param.set("serv", CURRENT_SERVICE);
			param.set("SCHED_NAME", ScheduleMgr.getInstance().getSchedulerName());
			param.set(TRIG_STATE, "RUNNING");
			outBean = super.save(param);
			if (isModify) {
				ScheduleMgr.getInstance().remove(trigger.getKey());
			}
			param.set(TRIG_PK, trigger.getKey().getName());
			ScheduleMgr.getInstance().add(trigger);
			updateJobAndTriggersState(trigger.getJobKey());
		} catch (SchedulerException se) {
			throw new RuntimeException("add trigger for job failed.", se);
		} catch (ParseException e) {
			throw new TipException(" unix crontab expresstion format error.");
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("save failed.", e);
		}
		outBean.setOk(Context.getSyMsg("SY_SAVE_DATA_SUCCEED"));
		return outBean;
	}

	/**
	 * 为job删除指定trigger
	 * @param param 参数Bean， 包含JobName
	 * @return 删除结果
	 */
	public OutBean delete(ParamBean param) {
		TriggerKey[] trigKeys = getTriggerKeyArray(param);
		OutBean outBean;
		try {
			// cache the job
			JobKey job = ScheduleMgr.getInstance().getTrigger(trigKeys[0]).getJobKey();
			// remove all triggers
			ScheduleMgr.getInstance().remove(trigKeys);
			// update job state
			updateState(job);
			outBean = super.delete(param);
		} catch (Exception e) {
			throw new RuntimeException("remove job failed", e);
		}
		outBean.setOk(Context.getSyMsg("SY_DELETE_OK", "1"));
		return outBean;
	}

	/**
	 * 暂停trigger
	 * @param param 参数Bean，包含Trigger name
	 * @return 处理结果
	 */
	public OutBean pauseTrigger(ParamBean param) {
		TriggerKey[] triggerKeys = getTriggerKeyArray(param);
		try {
			// cache the job
			JobKey job = null;
			ScheduleMgr.getInstance().pauseTrigger(triggerKeys);
			for (TriggerKey triggerKey : triggerKeys) {
				Trigger trigger = ScheduleMgr.getInstance().getTrigger(triggerKey);
				if (null == job) {
					job = trigger.getJobKey();
				}

				// update trigger state
				updateState(trigger);
			}
			// update jobState
			updateState(job);
		} catch (Exception e) {
			throw new RuntimeException("pause trigger failed", e);
		}
		OutBean outBean = new OutBean(param);
		outBean.setOk(Context.getSyMsg(SY_PAUSE_OPERATION_SUCCESSFUL));
		return outBean;
	}

	/**
	 * 恢复trigger
	 * @param param 参数Bean， 包含trigger name
	 * @return 处理结果
	 */
	public OutBean resumeTrigger(ParamBean param) {
		TriggerKey[] triggerKeys = getTriggerKeyArray(param);
		try {
			JobKey job = null;
			ScheduleMgr.getInstance().resumeTrigger(triggerKeys);
			for (TriggerKey triggerKey : triggerKeys) {
				// get trigger
				Trigger trigger = ScheduleMgr.getInstance().getTrigger(triggerKey);
				if (null == job) {
					job = trigger.getJobKey();
				}
				// update trigger state
				updateState(trigger);
			}
			// update jobState
			updateState(job);
		} catch (Exception e) {
			throw new RuntimeException("resume job failed", e);
		}
		OutBean outBean = new OutBean(param);
		outBean.setOk(Context.getSyMsg(SY_START_OPERATION_SUCCESSFUL));
		return outBean;
	}

	/**
	 * @param jobKey 任务key
	 * 
	 */
	public void init(JobKey jobKey) {
		Bean outBean = finds(new ParamBean(CURRENT_SERVICE).set(JOB_PK, jobKey.getName()));
		List<Bean> triggers = null;
		if (outBean != null) {
			triggers = outBean.getList(Constant.RTN_DATA);
		}
		if (null == triggers || 0 == triggers.size()) {
			return;
		}
		for (Bean trig : triggers) {
			String trigCode = trig.get(TRIG_PK, Lang.getUUID());
			if (!trig.contains(TRIG_PK)) {
				trig.set(TRIG_PK, trigCode);
			}
			String trigDesc = trig.getStr(TRIG_DESC);
			int repeatCount = trig.getInt(TRIG_REPEAT_COUNT);
			int trigInterval = trig.getInt(TRIG_INTERVAL);
			String startTimeStr = trig.getStr(TRIG_START_TIME);
			String endTimeStr = trig.getStr(TRIG_END_TIME);
			String cronExpr = trig.getStr(TRIG_CRON_EXPRESSION);
			// 1:simple trigger,2:cronatab trigger
			int type = trig.getInt(TRIG_TYPE);
			Date startTime = parseDate(startTimeStr);
			Date endTime = parseDate(endTimeStr);
			Trigger trigger = null;
			// build trigger
			try {
				if (type == 1) {
					trigger = ScheduleHelper.buildTrigger(trigCode, trigDesc, jobKey, startTime, endTime, repeatCount,
							trigInterval);
				} else {
					trigger = ScheduleHelper.buildTrigger(trigCode, jobKey, trigDesc, cronExpr, startTime, endTime);
				}
				ScheduleMgr.getInstance().add(trigger);
				updateJobAndTriggersState(trigger.getJobKey());
			} catch (SchedulerException se) {
				throw new RuntimeException("add trigger for job failed.", se);
			} catch (ParseException e) {
				throw new TipException(" unix crontab expresstion format error.");
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("save failed.", e);
			}

		}
	}

	/**
	 * 根据参数，提取jobname 并返回<CODE>JobKey</CODE>
	 * @param param 参数Bean
	 * @return 根据参数提取到的<CODE>JobKey</CODE>
	 */
	protected JobKey getJobKey(ParamBean param) {
		// TODO jobGroup
		String jobName = null;
		Map<String, String> paramInLinkData = extractParamFromLink(param);
		if (null != paramInLinkData && 0 < paramInLinkData.size()) {
			jobName = paramInLinkData.get(JOB_PK);
		}
		if (null != jobName) {
			JobKey jobKey = null;
			try {
				jobKey = ScheduleHelper.getJobKey(jobName);
			} catch (SchedulerException e) {
				throw new RuntimeException("get sched id error", e);
			}
			return jobKey;
		} else {
			return super.getJobKey(param);
		}
	}

	/**
	 * to Bean
	 * @param trig <CODE>Trigger</CODE>
	 * @return <CODE>Trigger</CODE> CODE>
	 */
	protected Bean valueOfBean(Trigger trig) {
		return valueOfBean(trig, null);
	}

	/**
	 * to Bean
	 * @param trig <CODE>Trigger</CODE>
	 * @param state <CODE>TriggerState</CODE>
	 * @return <CODE>Trigger</CODE> CODE>
	 */
	@SuppressWarnings("static-access")
	protected Bean valueOfBean(Trigger trig, TriggerState state) {
		Bean trigBean = new Bean();
		trigBean.set(TRIG_PK, trig.getKey().getName());
		if (null == trig.getKey().getGroup() || 0 == trig.getKey().getGroup().length()) {
			trigBean.set(TRIG_GROUP, "DEFAULT");
		} else {
			trigBean.set(TRIG_GROUP, trig.getKey().getGroup());
		}
		trigBean.set(TRIG_DESC, trig.getDescription());
		if (null != trig.getNextFireTime()) {
			trigBean.set(TRIG_NEXT_FIRE_TIME, new Timestamp(trig.getNextFireTime().getTime()));
		}
		if (null != trig.getPreviousFireTime()) {
			trigBean.set(TRIG_PREV_FIRE_TIME, new Timestamp(trig.getPreviousFireTime().getTime()));
		}
		trigBean.set(JOB_PK, trig.getJobKey().getName());
		if (null == trig.getJobKey().getGroup() || 0 == trig.getJobKey().getGroup().length()) {
			trigBean.set(JOB_GROUP, "DEFAULT");
		} else {
			trigBean.set(JOB_GROUP, trig.getJobKey().getGroup());
		}
		trigBean.set(TRIG_START_TIME, new Timestamp(trig.getStartTime().getTime()));
		if (null != trig.getEndTime()) {
			trigBean.set(TRIG_END_TIME, new Timestamp(trig.getEndTime().getTime()));
		}
		if (state == state.PAUSED) {
			trigBean.set(TRIG_STATE, "PAUSED");
		} else if (state == state.COMPLETE) {
			trigBean.set(TRIG_STATE, "COMPLETE");
		} else {
			trigBean.set(TRIG_STATE, "ALIVE");
		}
		try {
			trigBean.set("SCHED_NAME", ScheduleMgr.getInstance().getSchedulerName());
		} catch (SchedulerException e) {
			e.printStackTrace();
		}

		if (trig instanceof SimpleTrigger) {
			SimpleTrigger sTrig = (SimpleTrigger) trig;
			trigBean.set(TRIG_REPEAT_COUNT, sTrig.getRepeatCount());
			// store with milliseconds
			trigBean.set(TRIG_INTERVAL, sTrig.getRepeatInterval() / RATE);
			trigBean.set(TRIG_TYPE, "1");
		} else if (trig instanceof CronTrigger) {
			CronTrigger cTrig = (CronTrigger) trig;
			trigBean.set(TRIG_CRON_EXPRESSION, cTrig.getCronExpression());
			trigBean.set(TRIG_TYPE, "2");
		}
		return trigBean;
	}

	/**
	 * sync all triggers of one job with schedule manager
	 * @param allTriggers all triggers
	 * @param currentSize current jobs size in the database
	 * @throws SchedulerException throws this exception, if schedule instance error
	 */
	protected void syncTriggers(List<Trigger> allTriggers, int currentSize) throws SchedulerException {
		int increment = 0;
		String allIds = "";
		String jobName = "";
		if (null != allTriggers) {
			for (Trigger trig : allTriggers) {
				Bean exits = ServDao.find(CURRENT_SERVICE, trig.getKey().getName());
				if (null == exits) {
					// if not exits, we insert this trigger into database
					TriggerState state = ScheduleMgr.getInstance().getTriggerState(trig.getKey());
					Bean addBean = valueOfBean(trig, state);
					ServDao.create(CURRENT_SERVICE, addBean);
					jobName = trig.getJobKey().getName();
					increment++;
				}
				allIds += ("'" + trig.getKey().getName() + "',");
			}
		}
		allIds += "'-1' ";
		if ((currentSize + increment) > allTriggers.size()) {
			Bean delParam = new Bean();
			String condition = " AND TRIGGER_CODE NOT IN (" + allIds + ")";
			condition += "  AND JOB_CODE ='" + jobName + "'";
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
	protected boolean equals(Bean bean, Trigger target) {
		Bean exitsTrigBean = valueOfBean(target);
		if (!equals(bean, exitsTrigBean, TRIG_TYPE)) {
			log.warn(TRIG_TYPE + " trigger field :" + bean.get(TRIG_TYPE) + " != " + exitsTrigBean.get(TRIG_TYPE) + "["
					+ bean.get(TRIG_PK) + "]");
			return false;
		} else {
			if (1 == bean.getInt(TRIG_TYPE)) {
				if (!equals(bean, exitsTrigBean, TRIG_REPEAT_COUNT)) {
					log.warn(TRIG_REPEAT_COUNT + " trigger field :" + bean.get(TRIG_REPEAT_COUNT) + " != "
							+ exitsTrigBean.get(TRIG_REPEAT_COUNT) + "[" + bean.get(TRIG_PK) + "]");
					return false;
				}
				if (!equals(bean, exitsTrigBean, TRIG_INTERVAL)) {
					log.warn(TRIG_INTERVAL + " trigger field :" + bean.get(TRIG_INTERVAL) + " != "
							+ exitsTrigBean.get(TRIG_INTERVAL) + "[" + bean.get(TRIG_PK) + "]");
					return false;
				}
			} else {
				if (!equals(bean, exitsTrigBean, TRIG_CRON_EXPRESSION)) {
					log.warn(TRIG_CRON_EXPRESSION + " trigger field :" + bean.get(TRIG_CRON_EXPRESSION) + " != "
							+ exitsTrigBean.get(TRIG_CRON_EXPRESSION) + "[" + bean.get(TRIG_PK) + "]");
					return false;
				}
			}
		}
		return true;
	}

}
