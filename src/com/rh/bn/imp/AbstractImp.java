package com.rh.bn.imp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rh.core.base.Bean;
import com.rh.core.serv.ServDao;
import com.rh.core.util.DateUtils;

import jxl.Cell;
import jxl.Sheet;

/**
 * excel导入处理抽象类
 * @author tanyh 20161107
 *
 */
public abstract class AbstractImp {

	/** 日志列表 **/
	protected List<Bean> logList = new ArrayList<Bean>();
	/** 日志服务ID **/
	protected String LOG_SERV_ID = "BN_IMP_LOG";
	
	/** 存放已处理过的数据，避免相同的数据进行多次查询，如：部门、机构信息等 **/
	protected Map<String, String> sameData = new HashMap<String, String>();
	
	public String impData(Sheet sheet) {
		//取得行数  
        int rows = sheet.getRows();
        List<Bean> dataList = new ArrayList<Bean>();
        for (int i = 1; i < rows; i ++) {
        	Bean data = prepareData(sheet.getRow(i), i);
        	if (data != null && !data.isEmpty()) {
        		dataList.add(data);
        	}
        }
        // 批量保存数据
        if (dataList.size() > 0) {
        	ServDao.creates(getServId(), dataList);
        }
        // 记录导入日志
        if (logList.size() > 0) {
        	ServDao.creates(LOG_SERV_ID, logList);
        	// 重置日志列表对象
        	logList = new ArrayList<Bean>();
        }
		return "导入成功" + dataList.size() + "行；导入失败" + (rows - 1 - dataList.size()) + "行";
	}
	
	/**
	 * 预处理excel行数据
	 * @param cell
	 * @return Bean 行数据bean对象
	 */
	protected abstract Bean prepareData(Cell[] cell, int index);
	
	/**
	 * 获取服务ID
	 * @return String
	 */
	protected abstract String getServId();
	
	/**
	 * 获取导入操作名称，如：用户导入、部门导入等
	 * @return String
	 */
	protected abstract String getOptName();
	
	/**
	 * 构造日志对象
	 * @param dataId 数据ID
	 * @param dataName 数据名称
	 * @param desc 日志描述
	 * @return Bean 日志对象
	 */
	protected Bean createLogBean(String dataId, String dataName, String desc) {
		Bean log = new Bean();
		log.set("DATA_ID", dataId);
		log.set("DATA_NAME", dataName);
		log.set("SERV_ID", getServId());
		log.set("LOG_TIME", DateUtils.getDatetimeTS());
		log.set("LOG_DESC", desc);
		log.set("LOG_OPT", getOptName());
		return log;
	}
}
