/*
 * Copyright (c) 2013 Ruaho All rights reserved.
 */
package com.rh.core.plug.search.webcrawler.aperture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.semanticdesktop.aperture.crawler.Crawler;

import com.rh.core.base.Bean;
import com.rh.core.comm.schedule.job.RhJob;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServMgr;

/**
 * 调度webcrawler
 */
public class WebCrawlerJob implements RhJob {
    /** log */
    private Log log = LogFactory.getLog(WebCrawlerJob.class);

    private Crawler crawler = null;

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
        // get crawl conf
        JobDataMap jobData = context.getJobDetail().getJobDataMap();
        String site = jobData.getString("site");
        // 10MB
        long maxBytes = 10485760;

        Bean siteBean = ServDao.find(ServMgr.SY_PLUG_SEARCH_WEBCRAW, site);
        if (null == siteBean || siteBean.isEmpty()) {
            log.warn(" the site conf not found:" + site);
            throw new JobExecutionException(" the site conf not found:" + site);
        }

        String url = siteBean.getStr("CRAWL_URL");
        if (null == url || 0 == url.length()) {
            url = siteBean.getStr("HOST");
        }
        int depth = siteBean.get("CRAWL_DEPTH", 0);

        List<String> includeList = new ArrayList<String>();
        List<String> excludeList = new ArrayList<String>();
        String filterText = siteBean.getStr("URL_FILTER");
        String[] filters = filterText.split("\n");
        for (String filter : filters) {
            if (null == filter || 1 >= filter.length()) {
                continue;
            }
            String patten = filter.substring(1);
            if (patten.startsWith("^")) {
                patten = patten.substring(1);
            }
            if (filter.startsWith("+")) {
                includeList.add(patten);
            } else if (filter.startsWith("-")) {
                excludeList.add(patten);
            } else {
                throw new JobExecutionException(" unknow filter patten: " + filter);
            }
        }

        // categorys mapping set up
        Map<String, String> categoryMapping = new HashMap<String, String>();
        String catMappingStr = siteBean.getStr("CATEGORYS_CONF");
        String[] rows = catMappingStr.split("<category name=");
        for (String row : rows) {
            String[] catConf = row.split("reg-exp=");
            if (2 != catConf.length) {
                continue;
                // throw new JobExecutionException(" category mapping error: " + row);
            }
            String patten = catConf[1].trim();
            if (patten.endsWith("/>")) {
                patten = patten.substring(0, patten.length() - 2);
            }
            String name = catConf[0].trim().replace("\"", "");
            patten = patten.trim().replace("\"", "");
            categoryMapping.put(name, patten);
        }

        CrawlDataSource cds = new CrawlDataSource();
        cds.setCategoryMapping(categoryMapping);
        cds.setIncludeList(includeList);
        cds.setExcludeList(excludeList);
        
        cds.setTitlePreTag(siteBean.getStr("TITLE_PRE_TAG"));
        cds.setTitlePostTag(siteBean.getStr("TITLE_POST_TAG"));
        cds.setTitleRegexp(siteBean.getStr("TITLE_REGEXP"));
        
        cds.setContentPreTag(siteBean.getStr("CONTENT_PRE_TAG"));
        cds.setContentPostTag(siteBean.getStr("CONTENT_POST_TAG"));
        cds.setContentRegexp(siteBean.getStr("CONTENT_REGEXP"));
        
        cds.setId(siteBean.getId());

        RuahoWebCrawler webCrawler = new RuahoWebCrawler();
        crawler = webCrawler.getCrawler(url, depth, maxBytes, cds);

        crawler.crawl();
    }

    @Override
    public void interrupt() {
        crawler.stop();
        log.info("web crawler job stoped.");
    }
}
