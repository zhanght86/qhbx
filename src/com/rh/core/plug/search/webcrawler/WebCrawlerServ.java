/*
 * Copyright (c) 2011 Ruaho All rights reserved.
 */
package com.rh.core.plug.search.webcrawler;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrServerException;

import com.rh.core.base.Bean;
import com.rh.core.comm.schedule.serv.SchedJobServ;
import com.rh.core.plug.search.client.RhSearchClient;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServMgr;
import com.rh.core.util.Constant;

/**
 * @author liwei
 * 
 */
public class WebCrawlerServ extends CommonServ {

    /**
     * 保存之后,生成对应的任务
     * @param paramBean 参数信息
     * @param outBean 输出信息
     */
    protected void afterSave(ParamBean paramBean, OutBean outBean) {
        String jobCode = buildJobCode(paramBean);
        Bean queryBean = new Bean();
        queryBean.setId(jobCode);
        Bean exits = ServDao.find(ServMgr.SY_COMM_SCHED, queryBean);
        ParamBean job = new ParamBean();
        job.set("JOB_CODE", jobCode);
        job.setId(jobCode);
        if (null == exits || exits.isEmpty()) {
            job.setAddFlag(true);
        } else {
            job.setAddFlag(false);
        }
        job.set("JOB_CLASS_NAME", "com.rh.core.plug.search.webcrawler.aperture.WebCrawlerJob");
        job.set("JOB_GROUP", "DEFAULT");
        job.set("DESCRIPTION", "网站抓取:" + outBean.getStr("NAME"));
        job.set("JOB_DATA", "site=" + outBean.getId());
        job.set("REQUESTS_RECOVERY", 2);
        new SchedJobServ().save(job);
    }

    /**
     * clear single web site index
     * @param param - param
     * @return out bean
     */
    public Bean clearSingleIndex(Bean param) {
        String query = "site_id_strfield:" + param.getId();
        try {
            RhSearchClient rsc = new RhSearchClient();
            rsc.delIndexByQuery(query);
        } catch (SolrServerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        OutBean outBean = new OutBean();
        outBean.setOk();
        return outBean;
    }

    /**
     * clear all web index
     * @param param - param
     * @return out bean
     */
    public Bean clearIndex(Bean param) {
        String siteIds = param.getId();
        String[] idArray = siteIds.split(Constant.SEPARATOR);
        for (String id : idArray) {
            clearSingleIndex(new Bean().setId(id));
        }
        OutBean outBean = new OutBean();
        outBean.setOk();
        return outBean;
    }

    /**
     * build job code
     * @param paramBean - param bean
     * @return job code
     */
    private String buildJobCode(Bean paramBean) {
        return paramBean.getId();
    }

}
