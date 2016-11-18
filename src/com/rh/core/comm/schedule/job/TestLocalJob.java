package com.rh.core.comm.schedule.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * test jvm job
 * @author liwei
 * 
 */
public class TestLocalJob extends InitializeContextJob {

    @Override
    public void interrupt() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void executeJob(JobExecutionContext context) throws JobExecutionException {
        System.out.println("[test job]local job execute....");
    }

}
