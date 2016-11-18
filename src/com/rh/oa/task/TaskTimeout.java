package com.rh.oa.task;

import java.util.ArrayList;
import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.rh.core.base.Bean;
import com.rh.core.comm.schedule.job.RhJob;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.bean.SqlBean;
import com.rh.core.util.DateUtils;
/**
 * 
 *任务延期处理
 * 
 *
 */
public class TaskTimeout implements RhJob{
    private static final String BN_TM_TASK_SERV = "BN_TM_TASK";
    private static final String BN_TM_ASSIGN_SERV = "BN_TM_ASSIGN";
    private static final String TASK_STATE_TIMEOUT ="3";
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //查询当前时间
        DateUtils date=new DateUtils();
        String NowDate=date.getDate();
        //通过查询任务分配单去判断未处理的任务以及预计完成日期是否过期的记录
        SqlBean taskBean=new SqlBean();
        taskBean.and("TASK_END_DATE", "<", NowDate);
        taskBean.andNot("TASK_STATE", "5");
        List<Bean> taskListBean=new ArrayList<Bean>();
        taskListBean=ServDao.finds(BN_TM_TASK_SERV, taskBean);
        for(Bean bean:taskListBean){
            //修改预计时间过期的主单任务状态为延期
            SqlBean stateBean=new SqlBean();
            stateBean.setId(bean.getStr("TASK_ID"));
            stateBean.set("TASK_STATE", TASK_STATE_TIMEOUT);
            ServDao.update(BN_TM_TASK_SERV, stateBean);
          //修改预计时间过期的子单任务状态为延期
            SqlBean whereBean=new SqlBean();
            whereBean.and("TASK_ID", bean.getStr("TASK_ID"));
            Bean dataBean=new Bean();
            dataBean.set("TASK_STATE", TASK_STATE_TIMEOUT);
            
            ServDao.updates(BN_TM_ASSIGN_SERV, dataBean, whereBean);
        }
        
    }

    public void interrupt() {
        // TODO Auto-generated method stub
        
    }

}
