package com.rh.bn.serv;

import java.util.ArrayList;
import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;

/**
 * 用印申请单扩展服务类
 * @author tanyh20150727
 *
 */
public class SealUsageApplyServ extends CommonServ{
    
    /** 用印文件关联印章列表服务 **/
    private final String SEAL_LIST_SERV = "BN_SEAL_USE_LIST";
    /** 印章盖章记录服务 **/
    private final String SEAL_RECORD_SERV = "BN_FILE_SEAL_RECORD";
    /** 用印服务 **/
    private final String SEAL_USAGE_APPLY = "BN_SEAL_USAGE_APPLY";
    /**
     * 确认文件已盖章，主要是针对实物盖章
     * @param paramBean
     * @return
     */
    public OutBean confirmSeal(ParamBean paramBean) {
        OutBean outBean = new OutBean();
        // 审批单ID
        String applyId = paramBean.getStr("APPLY_ID");
        // 获取印章信息
        List<Bean> sealList = ServDao.finds(SEAL_LIST_SERV, " and APPLY_ID='" + applyId + "' and USE_STATUS=0");
        List<Bean> recordList = new ArrayList<Bean>();
        if (sealList != null && sealList.size() > 0) {
            for (Bean seal : sealList) {
                // 实物章，则入盖章记录表
                if (seal.getInt("SEAL_TYPE") == 2) {
                    Bean record = new Bean();
                    record.set("SEAL_ID", seal.getStr("SEAL_ID"));
                    record.set("SEAL_NAME", seal.getStr("SEAL_NAME"));
                    record.set("SEAL_TYPE", 2);
                    record.set("DATA_ID", applyId);
                    record.set("FILE_ID", seal.getStr("FILE_ID"));
                    record.set("SERV_ID", SEAL_USAGE_APPLY);
                    recordList.add(record);
                }
                // 更新状态为已盖章
                seal.set("USE_STATUS", 1);
                ServDao.update(SEAL_LIST_SERV, seal);
            }
        }
        // 批量存入记录表
        if (recordList.size() > 0) {
            ServDao.creates(SEAL_RECORD_SERV, recordList);
        }
        outBean.set("IS_OK", "true");
        return outBean;
    }
}
