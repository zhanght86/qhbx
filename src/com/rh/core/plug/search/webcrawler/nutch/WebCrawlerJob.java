/*
 * Copyright (c) 2012 Ruaho All rights reserved.
 */
package com.rh.core.plug.search.webcrawler.nutch;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.rh.core.comm.schedule.job.RhJob;
import com.rh.core.plug.search.client.RhWebCrawlerClient;

/**
 * 调度webcrawler
 */
public class WebCrawlerJob implements RhJob {
	/** log */
	// private Log log = LogFactory.getLog(IndexJob.class);

	/**
	 * Quartz requires a public empty constructor so that the scheduler can instantiate the class whenever it needs.
	 */
	public WebCrawlerJob() {
	}

	/**
	 * 通知web crawler服务对指定网站进行抓取
	 * @param context 调度上下文信息
	 * 
	 * @throws JobExecutionException 当例外发生
	 */
	public void execute(JobExecutionContext context) throws JobExecutionException {
//		int depth = 10;
//		int topN = 10;
//		int threads = 10;
		
		//get crawl conf
		 JobDataMap jobData = context.getJobDetail().getJobDataMap();
		 int depth = jobData.getIntValue("depth");
		 int topN = jobData.getIntValue("topN");
		 int threads = jobData.getIntValue("threads");
		
		// set current time be id
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss_SSS");
		String id = sdf.format(calendar.getTime());

		try {
			// upload crawler's config
			RhWebCrawlerClient.getInstance().uploadConfig();

			// crawl
			RhWebCrawlerClient.getInstance().crawl(id, depth, topN, threads);

			// commit index data
			RhWebCrawlerClient.getInstance().commit(id);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JobExecutionException("job failed", e);
		}
	}

    @Override
    public void interrupt() {
        // TODO Auto-generated method stub
        
    }
}
