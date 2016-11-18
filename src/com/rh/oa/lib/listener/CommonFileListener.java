package com.rh.oa.lib.listener;

import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;

/**
 * 删除文件的同时，删除OA_LIB_FILE表的记录
 * @author yangjy
 *
 */
public class CommonFileListener {
    /**
     * 
     * @param paramBean 参数Bean
     * @param outBean 处理结果Bean
     */
    public void afterDelete(ParamBean paramBean, OutBean outBean) {
        List<Bean> deletedDatas = outBean.getDataList();
        for (Bean fileBean : deletedDatas) {
            String id = fileBean.getId();
            if (fileBean.getStr("SERV_ID").equals("OA_LIB_ITEM")) {
                LibFileListener.deleteItemFile(id);
            }
        }
    }
}
