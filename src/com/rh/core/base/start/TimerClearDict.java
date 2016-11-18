package com.rh.core.base.start;

import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rh.core.base.Context;
import com.rh.core.serv.ServDefBean;
import com.rh.core.serv.ServUtils;
import com.rh.core.serv.dict.DictMgr;
import com.rh.core.util.Constant;

/**
 * 定时更新移动系统的系统字典
 * @author wangchen
 * 
 */
public class TimerClearDict extends TimerTask {

    /** log. */
    private static Log log = LogFactory.getLog(TimerLoader.class);
    
    @Override
    public void run() {
        try {
            
            //一、清除基础字典
            String[] dicts = Context.appStr("CACHE_DICT_SCHED_LOAD").split(Constant.SEPARATOR);
            for (String dictCode : dicts) {
                DictMgr.clearCache(dictCode);
            }
            //二、清除系统配置字典
            //1、私有
            ServDefBean priConfServ = ServUtils.getServDef("SY_COMM_CONFIG");
            priConfServ.clearDictCache();
            //2、公有
            ServDefBean pubConfServ = ServUtils.getServDef("SY_COMM_CONFIG_PUBLIC");
            pubConfServ.clearDictCache();
            
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}