/*
 * Copyright (c) 2011 Ruaho All rights reserved.
 */
package com.rh.core.plug.search;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.rh.core.base.Bean;
import com.rh.core.base.BeanUtils;
import com.rh.core.base.Context;
import com.rh.core.base.db.QueryCallback;
import com.rh.core.base.db.Transaction;
import com.rh.core.comm.schedule.job.RhJob;
import com.rh.core.plug.search.client.RhSearchClient;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServDefBean;
import com.rh.core.serv.ServDefSearch;
import com.rh.core.serv.ServUtils;
import com.rh.core.serv.dict.DictMgr;
import com.rh.core.util.Constant;
import com.rh.core.util.Lang;
import com.rh.core.util.Strings;

/**
 * 与任务调度配合，定期进行索引处理，依据服务定义信息进行索引的抓取工作
 * 
 * @author Jerry Li
 * @version $Id$
 */
public class IndexIncrJob implements RhJob {
    /** log */
	private Log log = LogFactory.getLog(IndexJob.class);

	/**
	 * Quartz requires a public empty constructor so that the scheduler can instantiate the class whenever it
	 * needs.
	 */
	public IndexIncrJob() {
	}

	/**
	 * 实现Job方法，进行定义调度处理
	 * @param context 调度上下文信息
	 * 
	 * @throws JobExecutionException 当例外发生
	 */
    public void execute(JobExecutionContext context) throws JobExecutionException {
	    String sql = "select * from SY_SERV_SEARCH where SERV_ID in"
	            + "(select serv_id from SY_SERV where S_FLAG=1 and SERV_SEARCH_FLAG=1";
	    JobDataMap jobData = context.getJobDetail().getJobDataMap();
	    //过滤判断是否只处理外部数据源的索引（缺省），可以通过开关设置全部处理（同时包含内外部）
	    if (jobData.containsKey("scope")) {
	        String scope = jobData.getString("scope");
	        if (!scope.equals("all")) {
	            if (scope.equals("inner")) {
	                sql += " and SERV_DATA_SOURCE is not null";
	            } else {
	                String[] array = scope.split(Constant.SEPARATOR);
	                sql += " and SERV_ID in ('" + Lang.arrayJoin(array, "','") + "')";
	            }
	        }
	    }
	    int rows = 0;
	    if (jobData.containsKey("rows")) {
	        rows = jobData.getInt("rows");
	    }
	    sql += ")";
	    List<Bean> searchList = Context.getExecutor().query(sql);
	    for (final Bean search : searchList) {
	        if (search.isEmpty("SEARCH_TITLE")) {
	            continue;
	        }
	        final ServDefBean serv = ServUtils.getServDef(search.getStr("SERV_ID"));
	        StringBuilder select = new StringBuilder(serv.getPKey());
	        StringBuilder where = new StringBuilder(); //TODO 改为搜索定义的过滤条件
	        String order = search.getStr("SEARCH_ORDER").toUpperCase();
	        String lastData = search.getStr("SEARCH_LAST_DATA");
	        String lastItem = search.getStr("SEARCH_LAST_FIELD");
	        if (lastData.length() > 0) {
	            String preStr = "";
	            Bean item = serv.getItem(lastItem);
	            if (item.getStr("ITEM_FIELD_TYPE").equals(Constant.ITEM_FIELD_TYPE_TIME)) {
	                if (lastData.length() >= 20) {
	                    lastData = lastData.substring(0, 19);
	                }
	                lastData = "TIMESTAMP'" + lastData + "'";
	            } else if (item.getStr("ITEM_FIELD_TYPE").equals(Constant.ITEM_FIELD_TYPE_DATE)) {
	                lastData = "DATE'" + lastData + "'";
	            } else if (item.getStr("ITEM_FIELD_TYPE").equals(Constant.ITEM_FIELD_TYPE_STR)) {
	                lastData = "'" + lastData + "'";
	            }
	            String symbol;
	            if (order.indexOf("DESC") >= 0) { //支持正序索引以及倒叙索引
	                symbol = "<";
	            } else {
	                symbol = ">";
	            }
                where.append(" and ").append(lastItem).append(symbol).append(preStr).append(lastData);
	        }
	        select.append(",").append(BeanUtils.getFieldCodes(search.getStr("SEARCH_TITLE"))).append(",")
	            .append(BeanUtils.getFieldCodes(search.getStr("SEARCH_CONTENT")));
	        if (!search.isEmpty("SEARCH_INDEX")) {
	            select.append(",").append(search.getStr("SEARCH_INDEX"));
	        }
	        if (!search.isEmpty("SEARCH_TIME")) {
	            select.append(",").append(search.getStr("SEARCH_TIME"));
	        }
	        if (!search.isEmpty("SEARCH_USER")) {
	            select.append(",").append(search.getStr("SEARCH_USER"));
	        }
	        if (!search.isEmpty("SEARCH_DEPT")) {
	            select.append(",").append(search.getStr("SEARCH_DEPT"));
	        }
	        if (!search.isEmpty("SEARCH_CMPY")) {
	            select.append(",").append(search.getStr("SEARCH_CMPY"));
	        }
	        if (!search.isEmpty("SEARCH_ALL_CONDITION")) {
	            select.append(",").append(BeanUtils.getFieldCodes(search.getStr("SEARCH_ALL_CONDITION")));
	        }
	        if (!search.isEmpty("SEARCH_USER_SQL")) {
	            select.append(",").append(BeanUtils.getFieldCodes(search.getStr("SEARCH_USER_SQL")));
	        }
	           if (!search.isEmpty("SEARCH_GROUP_SQL")) {
	                select.append(",").append(BeanUtils.getFieldCodes(search.getStr("SEARCH_GROUP_SQL")));
	            }
	        if (!search.isEmpty("SEARCH_DEPT_SQL")) {
	            select.append(",").append(BeanUtils.getFieldCodes(search.getStr("SEARCH_DEPT_SQL")));
	        }
	        if (!search.isEmpty("SEARCH_ROLE_SQL")) {
	            select.append(",").append(BeanUtils.getFieldCodes(search.getStr("SEARCH_ROLE_SQL")));
	        }
	        if (!search.isEmpty("SEARCH_DEPT_ROLE_SQL")) {
	            select.append(",").append(BeanUtils.getFieldCodes(search.getStr("SEARCH_DEPT_ROLE_SQL")));
	        }
	        String filePathField = BeanUtils.getFieldCodes(search.getStr("SEARCH_FILE_SQL"));
	        if (filePathField.length() > 0) {
	            select.append(",").append(filePathField);
	        }
	        Bean param = new Bean();
	        param.set(Constant.PARAM_SELECT, Strings.removeSame(select.toString()));
	        param.set(Constant.PARAM_WHERE, where.toString());
	        param.set(Constant.PARAM_ORDER, order);
	        if (rows > 0) {
	            param.set(Constant.PARAM_ROWNUM, rows); //设定了每次取指定条目数
	        }
	        String ds = serv.getDataSource();
	        if (ds.length() > 0) {
	            Transaction.begin(ds);
	        }
	        search.set("SEARCH_LAST_COUNT", 0);
	        final RhSearchClient rsc = new RhSearchClient();
	        try {
    	        ServDao.finds(serv.getId(), param, new QueryCallback() {
    	            public void call(List<Bean> columns, Bean data) {
    	                try {
    	                    for (Bean column : columns) { //预处理字典数据
    	                        String name = column.getStr("NAME");
    	                        Bean item = serv.getItem(name);
    	                        if (item != null && !item.isEmpty("DICT_ID")) { //数据字典项
    	                            data.set(name, DictMgr.getFullNames(item.getStr("DICT_ID"), 
    	                                    data.getStr(name)));
    	                        }
    	                    }
    	                    rsc.addIndex(ServUtils.indexMgs(search, data).getIndex());
    	                    search.set("SEARCH_LAST_DATA", data.getStr(search.getStr("SEARCH_LAST_FIELD")));
    	                    search.set("SEARCH_LAST_COUNT", search.getInt("SEARCH_LAST_COUNT") + 1);
    	                } catch (Exception e) {
    	                    log.error(e.getMessage(), e);
    	                }
    	            }
    	        });
    	        
    	        rsc.commit();
	        } catch (Exception e) {
                log.error(e.getMessage(), e);
	        } finally {
	            if (!serv.isEmpty("SERV_DATA_SOURCE")) {
	                Transaction.end();
	            }
	        }
	        if (search.getInt("SEARCH_LAST_COUNT") > 0) { //执行了索引处理，动态更新最后索引时间
    	        //更新上次索引时间，便于增量索引
    	        ServDao servDef = new ServDao(ServDefSearch.SERV_ID_SEARCH, serv.getId());
    	        servDef.set("SEARCH_LAST_DATA", search.getStr("SEARCH_LAST_DATA"));
    	        servDef.set("SEARCH_LAST_COUNT", search.getStr("SEARCH_LAST_COUNT"));
    	        servDef.set("SEARCH_ALL_COUNT", search.getInt("SEARCH_ALL_COUNT") 
    	                + search.getInt("SEARCH_LAST_COUNT"));
    	        servDef.update();
	        }
	    }
	}

    @Override
    public void interrupt() {
        // TODO Auto-generated method stub
        
    }
}
