/*
 * Copyright (c) 2011 Ruaho All rights reserved.
 */
package com.rh.core.plug.search;

import java.util.List;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.base.db.QueryCallback;
import com.rh.core.comm.schedule.job.RhJob;
import com.rh.core.serv.ServUtils;
import com.rh.core.util.Lang;

/**
 * 与任务调度配合，定期进行索引处理，依据服务定义信息进行索引的抓取工作
 * 
 * @author Jerry Li
 * @version $Id$
 */
public class IndexJob implements RhJob {
    /** log */
//	private Log log = LogFactory.getLog(IndexJob.class);

	/**
	 * Quartz requires a public empty constructor so that the scheduler can instantiate the class whenever it
	 * needs.
	 */
	public IndexJob() {
	}

	/**
	 * 实现Job方法，进行定义调度处理
	 * @param context 调度上下文信息
	 * 
	 * @throws JobExecutionException 当例外发生
	 */
    public void execute(JobExecutionContext context) throws JobExecutionException {
	    StringBuilder sql = new StringBuilder();
	    sql.append("select * from SY_SERV_SEARCH  where 1=1 ");
	    
	    JobDataMap jobData = context.getJobDetail().getJobDataMap();
	    //过滤判断是否只处理外部数据源的索引（缺省），可以通过开关设置全部处理（同时包含内外部）
	    if (jobData.containsKey("scope")) {
	        String scope = jobData.getString("scope");
	        if (!scope.equals("all")) {
	                String[] array = scope.split("/");
	                sql.append(" and SEARCH_ID in ('");
	                sql.append(Lang.arrayJoin(array, "','"));
	                sql.append("')");
	        }
	    }
	    List<Bean> searchList = Context.getExecutor().query(sql.toString(), new QueryCallback() {
            @Override
            public void call(List<Bean> columns, Bean dataBean) {
                String sql = "select * from SY_PLUG_SEARCH_LINK where S_FLAG=1 and SERV_ID='" 
                        + dataBean.getStr("SERV_ID") + "' order by LINK_ORDER";
                dataBean.set("SY_PLUG_SEARCH_LINK", Context.getExecutor().query(sql));
            }
	    });
	    int rows = 5000;
	    if (jobData.containsKey("rows")) {
	        rows = jobData.getInt("rows");
	    }
	    for (final Bean search : searchList) {
	        ServUtils.doIndex(search, rows);
	    } //end for
	}

    @Override
    public void interrupt() {
        // TODO Auto-generated method stub
        
    }
}
