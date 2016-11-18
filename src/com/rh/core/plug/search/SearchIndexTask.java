/*
 * Copyright (c) 2012 Ruaho All rights reserved.
 */
package com.rh.core.plug.search;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import com.rh.core.base.Bean;
import com.rh.core.plug.search.client.BatchIndexTask;
import com.rh.core.plug.search.client.IndexTaskStatus;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDefBean;
import com.rh.core.serv.ServUtils;
import com.rh.core.serv.dict.DictMgr;
import com.rh.core.util.Constant;
import com.rh.core.util.DateUtils;
import com.rh.core.util.threadpool.RhThreadPool;

/**
 * 执行索引创建任务 
 *  @author zhl
 */
public class SearchIndexTask extends CommonServ {
    //任务服务
	private  static final String SY_SEARCH_INDEX_TASK = "SY_SEARCH_INDEX_TASK";
	private  static final String TASK_NAME = "TASK_NAME";
	private  static final String PLAN_END_TIME = "PLAN_END_TIME";
	private  static final String END_TIME = "END_TIME";
	private  static final String COUNT = "COUNT";
	private  static final String RUN_STATE = "RUN_STATE";
	private  static final String CURRENT_TIME = "CURRENT_TIME";
	/**
     * 执行索引任务
     * @param param servid[,] endTime, running
     * @return value
     */
	public OutBean runIndexTask(ParamBean param) {
		OutBean outBean = new OutBean();
		String searchId = param.getStr(TASK_NAME);
		String planendtime = param.getStr(PLAN_END_TIME);
		//true:运行,false:停止
		String run = param.get(RUN_STATE, "true");
		if (!searchId.equals("")) {
            synchronized (CURRENT_TIME) {
                if (!BatchIndexTask.isRunning()) {
                    if (run == "true") {
                        try {
                            BatchIndexTask task = BatchIndexTask.createInstance(searchId);
                            RhThreadPool.getDefaultPool().execute(task);
                            Thread.sleep(500);
                        } catch (Exception e) {
                            log.error(e);
                        }
                    }
                }
            }
	        if (!planendtime.equals("")) {
	            String[] sIds = searchId.split(",");
	            for (String sId : sIds) {
	                IndexTaskStatus.setEndTime(sId, planendtime);
                    if (!IndexTaskStatus.isRunning(sId)) { // 设置
                        IndexTaskStatus.setRunning(sId, true);
                    }
	            }
	        }
	    }
		return outBean.setOk();
	}
	
	/**
	 * 重载查询方法
	 * @param paramBean 查询条件
	 * @return 查询结果
	 */
	public OutBean query(ParamBean paramBean) {
        OutBean outBean = new OutBean();
        List<Bean> dataList = createDataList(getTaskStorage());
        final ServDefBean serv = ServUtils.getServDef(SY_SEARCH_INDEX_TASK);
        //构建行模型
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
        } 
		outBean.setCount(dataList.size());
		outBean.setData(dataList);
		outBean.setPage(1);
		outBean.setCols(cols);
        return outBean;
    }
  
	/**
	 * 创建列表数据,添加指定字典的__NAME
	 * @param taskStorage 数据bean
	 * @return 返回列表数据
	 */
    private List<Bean> createDataList(List<Bean> taskStorage) {
    	List<Bean> dataList = new ArrayList<Bean>();
        for (Bean bean : taskStorage) {
            String status = bean.getStr(RUN_STATE);
            String statusDisplay = DictMgr.getFullName("SY_TASK_STATE", status);
            bean.set("RUN_STATE__NAME", statusDisplay);
            dataList.add(bean);
        }
        return dataList;
    }
    /**
     * 获取任务数据
     * @return 任务列表
     */
    private List<Bean> getTaskStorage() {
    	List<Bean> taskStorage = new ArrayList<Bean>();
    	//获取所有任务
    	Object[] tasks = IndexTaskStatus.getTaskNames();
    	 for (Object taskName : tasks) {
    		 String name = (String) taskName;
    		 Bean bean = new Bean();
    		 bean.set(TASK_NAME, name);
    		 bean.set(RUN_STATE, IndexTaskStatus.isRunning(name));
    		 bean.set(COUNT, IndexTaskStatus.getCountVal(name));
    		 bean.set(PLAN_END_TIME, IndexTaskStatus.getEndTime(name));
    		 bean.set(END_TIME, IndexTaskStatus.getFinishTime(name));
    		 bean.set(CURRENT_TIME, DateUtils.getDatetime());
    		 taskStorage.add(bean);
    	 }
    	return taskStorage;
    }
}
