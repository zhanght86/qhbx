package com.rh.core.serv.dict;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.rh.core.base.Context;
import com.rh.core.comm.schedule.job.InitializeContextJob;
import com.rh.core.util.Constant;

/**
 * 与任务调度配合，定期进行字典缓存的更新处理
 * 
 * @author jerry li
 * 
 */
public class DictCacheJob extends InitializeContextJob {
    
    /** 第一次执行标志 */
    private static boolean isFirst = true;

    private Logger log = Logger.getLogger(getClass());

    /**
     * 构造函数
     */
    public DictCacheJob() {
    }

    /**
     * 实现Job方法，进行定义调度处理
     * @param context 调度上下文信息
     * 
     * @throws JobExecutionException 当例外发生
     */
    public void executeJob(JobExecutionContext context) throws JobExecutionException {
        try {
            if (isFirst) {
                isFirst = false;
                //读取需要任务转载的缓存列表，在web.xml中配置
                String[] dicts = Context.appStr("CACHE_DICT_SCHED_LOAD").split(Constant.SEPARATOR);
                for (String dictCode : dicts) {
                    DictMgr.loadDictCache(dictCode);
                }
            } else {
                String info = DictMgr.rebuildCache();
                if (info.length() > 0) {
                    log.debug("DICT CACHE RELOAD : " + info);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void interrupt() {
        // TODO Auto-generated method stub
        
    }
}
