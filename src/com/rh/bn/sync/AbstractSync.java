package com.rh.bn.sync;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rh.core.base.Bean;
import com.rh.core.base.db.Transaction;
import com.rh.core.serv.ServDao;

/**
 * 数据同步抽象类
 * @author Tanyh 20150807
 *
 */
public abstract class AbstractSync implements ISync{
    private static Log log = LogFactory.getLog(AbstractSync.class);
    
    /**
     * 获取数据
     * @param beginTime 开始时间
     * @return List<Bean> 数据结果集
     */
    protected abstract List<Bean> getDataBeanList(String beginTime);
    /**
     * 预处理数据
     * @param data 预处理的数据
     * @return Bean 处理后的数据
     */
    protected abstract Bean prepareData(Bean data);
    /**
     * 是否需要进行删除操作
     * @return boolean 是否需要删除
     */
    protected abstract boolean isDeleted();
    /**
     * 获取服务ID
     * @return String 返回服务ID
     */
    protected abstract String getImplServId();
    /**
     * 写入日志表中
     * @param dataBean 数据载体
     * @param syncFlag 同步结果标识
     * @param syncDesc 同步结果
     */
    protected abstract void logDataSyncState(Bean dataBean, int syncFlag, String syncDesc);
    
    /**
     * 同步数据
     * @param beginTime 开始时间
     */
    public void sync(String beginTime) {
        // 获取数据
        List<Bean> dataBeanList = getDataBeanList(beginTime);
        syncDataList(dataBeanList);
    }
    
    /**
     * 处理同步数据
     * @param dataBeanList 数据集
     */
    private void syncDataList(List<Bean> dataBeanList) {
        if (dataBeanList != null) {
            for (Bean dataBean : dataBeanList) {
                String syncDesc = "";
                int syncFlag = 0;
                try {
                    Transaction.begin();
                    // 是否先删原有的数据
                    if (isDeleted()) {
                        removeData(dataBean);
                    }
                    // 新增、更新的数据则入库
                    if (dataBean.getInt("OPERATE_TYPE") == 1 || dataBean.getInt("OPERATE_TYPE") == 2) {
                        createData(dataBean);
                    }
                    Transaction.commit();
                } catch (Exception e) {
                    log.error("同步数据失败。", e);
                    syncFlag = 1;
                    syncDesc = e.getMessage();
                } finally {
                    Transaction.end();
                    // 将处理完数据，不论成功或失败，写入日志表
                    logDataSyncState(dataBean, syncFlag, syncDesc);
                }
            }
        }
    }
    
    /**
     * 清除原有数据
     * @param dataBean 待清除的数据载体
     * @return boolean 是否清除成功
     */
    protected final boolean removeData(Bean dataBean) {
        return ServDao.destroy(getImplServId(), dataBean);
    }
    
    /**
     * 插入数据
     * @param dataBean 数据载体
     */
    protected final void createData(Bean dataBean) {
        // 预处理数据
        Bean newData = prepareData(dataBean);
        if (newData != null && !newData.isEmpty()) {
            // 插入数据
            ServDao.create(getImplServId(), newData);
        }
    }
    
    protected final String getLogServId(){
        return "BN_SYNC_DATA_LOG";
    }
}
