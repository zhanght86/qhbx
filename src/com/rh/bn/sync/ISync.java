package com.rh.bn.sync;


/**
 * 同步接口
 * 
 * @author cuihf
 *
 */
public interface ISync {
    
    /**
     * 同步操作
     * 
     * @param beginTime 读取数据的起始时间
     */
    void sync(String beginTime);

}
