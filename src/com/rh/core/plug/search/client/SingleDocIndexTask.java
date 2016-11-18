package com.rh.core.plug.search.client;

import org.apache.commons.lang.time.StopWatch;

import com.rh.core.base.Bean;
import com.rh.core.plug.search.RhIndex;
import com.rh.core.serv.ServUtils;
import com.rh.core.util.Constant;
import com.rh.core.util.threadpool.ThreadTask;

/**
 * 在当前线程中为单个文件执行索引
 * @author yangjy
 * 
 */
public class SingleDocIndexTask extends ThreadTask {
    /** 搜索定义Bean **/
    private Bean searchDef = null;

    /** 数据Bean **/
    private Bean dataBean = null;

    private RhSearchClient rsc = null;

    /** 是否更新日志。 **/
    private boolean updateLogState = false;
    
    private int index = 0;

    /**
     * 
     * @param searchDef 索引定义Bean
     * @param dataBean 数据Bean
     * @param rsc RhSearchClient实例
     */
    public SingleDocIndexTask(Bean searchDef, Bean dataBean, RhSearchClient rsc) {
        this.searchDef = searchDef;
        this.dataBean = dataBean;
        this.rsc = rsc;
    }
    
    /**
     * @param i 增加任务索引号
     */
    public void setIndex(int i) {
        this.index = i;
    }

    @Override
    public void execute() {
        RhIndex rhIndex = null;
        StopWatch sw = new StopWatch();
        sw.start();
        log.info("----docIndex start:" + Thread.currentThread().getName() 
                + ", " + this.index + ", " + searchDef.getStr("SERV_ID") 
                + ",  " + dataBean.getId());
        try {
            rhIndex = ServUtils.indexMgs(searchDef, dataBean).getIndex();
            if (dataBean.getInt("S_FLAG") == Constant.NO_INT) {
                // 数据为已删除,则删除索引库中的记录。
                rsc.delIndex(rhIndex);
            } else {
                rsc.addIndex(rhIndex);
            }
            if (this.isUpdateLogState()) { // 如果需要更新日志则
                IndexLogMgr.updateStateToOk(searchDef.getStr("SERV_ID"), dataBean.getId());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            IndexLogMgr.addIndexLog(this.searchDef, this.dataBean, e);
        }

        log.info("----docIndex end:" + Thread.currentThread().getName() + ", " + sw.getTime());
    }

    /**
     * 
     * @return 是否更新日志表对应的记录
     */
    public boolean isUpdateLogState() {
        return updateLogState;
    }

    /**
     * 
     * @param updateLog 是否更新日志表对应的记录，默认为false。<br>
     *            正常情况下该属性的值都是false。如果为true表示用于对索引失败的数据重新做索引，重建索引后，要求修改失败记录的状态 。
     */
    public void setUpdateLogState(boolean updateLog) {
        this.updateLogState = updateLog;
    }
}
