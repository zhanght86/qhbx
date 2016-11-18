package com.rh.core.util;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.rh.core.base.Bean;
import com.rh.core.comm.schedule.job.InitializeContextJob;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServMgr;
import com.rh.core.serv.bean.SqlBean;
import com.rh.core.serv.dict.DictMgr;

/**
 * 1，年度字典自增
 * 需要将字典SY_YEAR配置成外部字典，
 * 
 * 2，初始化工作日信息， 在流程中有用到
 * 
 * @author anan
 *
 */
public class YearAdd  extends InitializeContextJob implements Job  {
	
	@Override
    public void executeJob(JobExecutionContext context) throws JobExecutionException {
        //JobDataMap jobData = context.getJobDetail().getJobDataMap();
        
        yearAdd();
        

    }    
    

	/**
	 * 初始化工作日
	 * @param nextYear 
	 */
	private void initWorkDay(int nextYear) {
		ParamBean param = new ParamBean();
		
		param.set("YEAR", nextYear);
		
		ServMgr.act(ServMgr.SY_COMM_WORK_DAY, "initWorkDay", param);
	}


	/**
	 * 年度自增
	 */
	public void yearAdd() {
		int currentYear = DateUtils.getYear();
		int newYear = currentYear + 1;

		addNewYear(currentYear); //当年的，如果没加，则自动加
		
		addNewYear(newYear);  //明年的， 
		
        initWorkDay(newYear); //初始化工作日, 下一年度的
	}
	
	/**
	 * 
	 * @param newYear 年度
	 */
	private void addNewYear(int newYear) {
		SqlBean newYearBean = new SqlBean();
		newYearBean.and("ITEM_CODE", newYear);
		newYearBean.and("ITEM_NAME", newYear);
		newYearBean.and("DICT_ID", "SY_YEAR");
		
		int count = ServDao.count(ServMgr.SY_SERV_DICT_ITEM, newYearBean);
 
		if (count == 0) {
			Bean newBean = new Bean();
			newBean.set("ITEM_CODE", newYear);
			newBean.set("ITEM_NAME", newYear);
			newBean.set("DICT_ID", "SY_YEAR");
			newBean.set("ITEM_ORDER", newYear);
			newBean.set("ITEM_FLAG", 1);
			newBean.set("ITEM_LEAF", 2);
			newBean.set("S_PUBLIC", Constant.YES_INT);
			newBean.set("ITEM_LEVEL", 0);

			ServDao.create(ServMgr.SY_SERV_DICT_ITEM, newBean);
			
			//添加完成之后刷新缓存
			DictMgr.clearCache("SY_YEAR", null);
		}
	}


	@Override
	public void interrupt() {
		// TODO 自动生成的方法存根
		
	}
}
