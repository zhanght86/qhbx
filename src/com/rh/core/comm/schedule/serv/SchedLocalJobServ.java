/*
 * Copyright (c) 2013 Ruaho All rights reserved.
 */
package com.rh.core.comm.schedule.serv;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;

import com.rh.core.base.Bean;
import com.rh.core.comm.schedule.mgr.ScheduleMgr;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDefBean;
import com.rh.core.serv.ServMgr;
import com.rh.core.serv.ServUtils;
import com.rh.core.serv.dict.DictMgr;
import com.rh.core.util.Constant;

/**
 * 计划调度服务类
 * @author liwei
 */
public class SchedLocalJobServ extends SchedBaseServ {

    /** log */
    private static Log log = LogFactory.getLog(SchedLocalJobServ.class);


    /**
     * @see com.rh.core.serv.CommonServ#query(com.rh.core.base.Bean)
     * @param param bean
     * @return result bean
     */
    public OutBean query(ParamBean param) {
        OutBean outBean = new OutBean();
        Set<JobKey> allJobs;
        List<Bean> dataList = new ArrayList<Bean>();
        try {
            allJobs = ScheduleMgr.getInstance().getAllLocalJobs();
            for (JobKey jobKey : allJobs) {
                JobDetail job = ScheduleMgr.getInstance().getLocalJob(jobKey);
                Bean jobBean = new Bean();
                jobBean.set(JOB_GROUP, jobKey.getGroup());
                jobBean.set(JOB_PK, jobKey.getName());
                jobBean.set(JOB, job.getJobClass());
                jobBean.set(JOB_DESC, job.getDescription());
                String status = ScheduleMgr.getInstance().getLocalJobStatus(jobKey);
                jobBean.set("JOB_STATE", status);
                String statusDisplay = DictMgr.getFullName("SY_COMM_SCHED_STATE", status);
                jobBean.set("JOB_STATE__NAME", statusDisplay);
                
                dataList.add(jobBean);
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

        final ServDefBean serv = ServUtils.getServDef(ServMgr.SY_COMM_LOCAL_SCHED);
        final LinkedHashMap<String, Bean> cols = new LinkedHashMap<String, Bean>();
       
        LinkedHashMap<String, Bean> items = serv.getAllItems();
        boolean bKey = true;
        for (String key : items.keySet()) {
            Bean item = items.get(key);
            int listFlag = item.getInt("ITEM_LIST_FLAG");
            if (bKey && item.getStr("ITEM_CODE").equals(serv.getPKey())) { // 主键无论是否列表显示都输出
                if (listFlag == Constant.ITEM_LIST_FLAG_HIDDEN) { // 如果定义为隐藏有数据，则提供给前端时设为不显示
                    listFlag = Constant.ITEM_LIST_FLAG_NO;
                }
                addCols(cols, item, listFlag);
                bKey = false;
            } else if (listFlag != Constant.ITEM_LIST_FLAG_NO) {
                if (item.getInt("ITEM_TYPE") == Constant.ITEM_TYPE_TABLE
                        || item.getInt("ITEM_TYPE") == Constant.ITEM_TYPE_VIEW) {
                }
                if (listFlag == Constant.ITEM_LIST_FLAG_HIDDEN) { // 如果定义为隐藏有数据，则提供给前端时设为不显示
                    listFlag = Constant.ITEM_LIST_FLAG_NO;
                }
                addCols(cols, item, listFlag);
            }
            
        } // end for

        outBean.setData(dataList);
        outBean.setCount(dataList.size());
        outBean.setPage(1);
        outBean.setCols(cols);
        return outBean;
    }

    /**
     * 添加一个任务
     * @param param 参数bean，包含job信息
     * @return 添加结果
     */
    public OutBean save(ParamBean param) {
        return new OutBean().setError(" not implemented yet");
    }

    /**
     * 删除指定job
     * @param param 参数Bean， 包含JobName
     * @return 删除结果
     */
    public OutBean delete(ParamBean param) {
        return new OutBean().setError(" not implemented yet");
    }

    /**
     * 查询指定job
     * @param param 参数Bean， 包含JobName
     * @return 查询结果
     */
    public OutBean byid(ParamBean param) {
        OutBean outBean = new OutBean();
        outBean.set(JOB_PK, param.getId());
        return outBean;
    }

    /**
     * 暂停job
     * @param param 参数Bean，包含jobName
     * @return 处理结果
     */
    public OutBean pauseJob(ParamBean param) {
        return new OutBean().setError(" not implemented yet");
    }

    /**
     * 恢复job
     * @param param 参数Bean， 包含job name
     * @return 处理结果
     */
    public OutBean resumeJob(ParamBean param) {
        return new OutBean().setError(" not implemented yet");
    }

    /**
     * 终止任务
     * @param param 参数Bean，包含jobName
     * @return 处理结果
     */
    public OutBean interruptJobs(ParamBean param) {
        return new OutBean().setError(" not implemented yet");
    }

}
