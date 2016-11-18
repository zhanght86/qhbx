package com.rh.bn.sync.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.rh.bn.sync.impl.oa.QbAttachmentSync;
import com.rh.bn.sync.impl.oa.QbDataSync;
import com.rh.bn.sync.impl.oa.QbMindSync;
import com.rh.core.comm.schedule.job.InitializeContextJob;

/**
 * 同步百年人寿旧签报系统数据后台任务类
 * @author Tanyh 20150808
 *
 */
public class QbSyncJob extends InitializeContextJob{


    public void interrupt() {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void executeJob(JobExecutionContext arg0) throws JobExecutionException {
        //开始同步签报数据
        (new QbDataSync()).sync("");
        //同步意见
        (new QbMindSync()).sync("");
        //同步附件
        (new QbAttachmentSync()).sync("");
    }

}
