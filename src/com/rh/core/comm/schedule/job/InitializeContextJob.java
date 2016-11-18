package com.rh.core.comm.schedule.job;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.rh.core.base.Context;
import com.rh.core.base.Context.THREAD;
import com.rh.core.comm.schedule.mgr.ScheduleMgr;
import com.rh.core.org.UserStateBean;
import com.rh.core.org.mgr.UserMgr;

/**
 * 初始化任务上下文的Job
 * 
 * @author cuihf
 * 
 */
public abstract class InitializeContextJob implements RhJob {

    private static Log log = LogFactory.getLog(InitializeContextJob.class);

    private List<String> errorMessages;

    @Override
    public final void execute(JobExecutionContext context) throws JobExecutionException {
        UserStateBean userState = null;
        Object user = context.getJobDetail().getJobDataMap().get("JOB_CONTEXT_USER");
        String userId = "";
        if (null != user) {
            userId = (String) user;
        }
        if (userId.length() > 0) {
            userState = UserMgr.getUserState(userId);
            Context.setThreadUser(userState);
        }

        if (context != null && context.getJobDetail().getJobDataMap().get(ScheduleMgr.CONTEXT_CMPYS_KEY) != null) {
            String[] cmpyCodes = (String[]) context.getJobDetail().getJobDataMap().get(ScheduleMgr.CONTEXT_CMPYS_KEY);
            for (String cmpyCode : cmpyCodes) {
                Context.setThread(THREAD.CMPYCODE, cmpyCode);
                try {
                    executeJob(context);
                } catch (JobExecutionException e) {
                    log.error("任务执行失败，任务：" + this.getClass().getName() + ";公司编码：" + cmpyCode);
                    this.addErrorMessage("任务执行失败，任务：" + this.getClass().getName() + ";公司编码：" + cmpyCode);
                }
            }
        } else {
            try {
                executeJob(context);
            } catch (JobExecutionException e) {
                log.error("任务执行失败，任务：" + this.getClass().getName() + ";公司编码：未指定");
                context.put(ScheduleMgr.CONTEXT_JOB_EXECUTED_DESC_KEY, "任务执行失败，任务：" + this.getClass().getName()
                        + ";公司编码：未指定");
                throw new JobExecutionException("任务执行失败，任务：" + this.getClass().getName() + ";公司编码：未指定");
            }
        }

        if (null != userState) {
            Context.removeThreadUser();
        }

        outputError(context);
    }

    /**
     * 输出错误信息
     * @param context Job执行上下文对象
     */
    private void outputError(JobExecutionContext context) {
        if (this.errorMessages != null) {
            StringBuilder messageBuilder = new StringBuilder("");
            for (String message : this.errorMessages) {
                messageBuilder.append(message + "。");
            }
            if (messageBuilder.length() > 0) {
                context.put(ScheduleMgr.CONTEXT_JOB_EXECUTED_DESC_KEY, messageBuilder.toString());
            }
        }
    }

    /**
     * 子类需实现的方法 任务执行
     * @param context - 上下文
     * @throws JobExecutionException 执行失败的错误对象
     */
    protected abstract void executeJob(JobExecutionContext context) throws JobExecutionException;

    /**
     * 增加错误信息
     * @param errorMessage 错误信息
     */
    protected final void addErrorMessage(String errorMessage) {
        if (this.errorMessages == null) {
            this.errorMessages = new ArrayList<String>();
        }
        this.errorMessages.add(errorMessage);
    }
}
