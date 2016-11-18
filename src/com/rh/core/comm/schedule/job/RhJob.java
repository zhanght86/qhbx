package com.rh.core.comm.schedule.job;

import org.quartz.InterruptableJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author liwei 计划任务job
 */
public interface RhJob  extends InterruptableJob {

    /**
     * 任务执行 <p>
     * 由触发器触发执行
     * 
     * @param context - job context
     * @throws JobExecutionException if there is an exception while executing the job.
     */
    void execute(JobExecutionContext context)
            throws JobExecutionException;

    /**
     * 任务停止<p>
     * 当接收到停止任务指令后，该函数触发执行
     *  
     */
    void interrupt();
}
