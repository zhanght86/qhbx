package com.rh.core.base.start;

import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.Trigger;

import com.rh.core.comm.ScheduleHelper;
import com.rh.core.comm.schedule.mgr.ScheduleMgr;
import com.rh.core.comm.schedule.serv.SchedJobServ;

/**
 * 任务计划装载类
 * @author liwei
 * 
 */
public class ScheduleLoader {

    /**
     * 任务计划管理器启动 启动失败抛出异常
     */
    public void start() {
        try {
            ScheduleMgr.getInstance().start();
        } catch (SchedulerException e) {
            throw new RuntimeException("schedule manager start failed.", e);
        }

        // 检查是否需要进行任务初始化
        new SchedJobServ().init();

        // 添加本地任务
        //
        try {
            JobDetail job = ScheduleHelper.buildJob("com.rh.core.serv.dict.DictCacheJob", "dictJob", "", null,
                    false);
            // 测试任务每一分钟执行一次
            // Trigger trigger = ScheduleHelper.buildTrigger("testTrigger", job.getKey(), "every 1 minute execute",
            // "0 0/1 * * * ?", null, null);
            
            Trigger trigger = ScheduleHelper.buildTrigger("5mTrigger", "every 5 minute execute", job.getKey(), null,
                    null, -1, 180);   //每5分钟300秒执行一次

            ScheduleMgr.getInstance().addLocalJob(job);
            ScheduleMgr.getInstance().addLocalTrigger(trigger);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 任务计划管理器停止 停止失败抛出异常
     */
    public void stop() {
        try {
            ScheduleMgr.getInstance().shutdown();
        } catch (SchedulerException e) {
            throw new RuntimeException("schedule magager stop failed.", e);
        }
    }

}
