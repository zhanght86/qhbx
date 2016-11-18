package com.rh.bn.serv;

import com.rh.core.base.Bean;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;

/**
 * 文件盖章记录服务类
 * @author tanyh 20150710
 *
 */
public class FileSealServ extends CommonServ{

    /** 印章信息服务 **/
    private static final String SEAL_SERV = "BN_SEAL_BASIC_INFO";
    /** 盖章记录服务 **/
    private static final String SEAL_RECORD_SERV = "BN_FILE_SEAL_RECORD";
    
    /**
     * 保存盖章记录
     * @param paramBean 前端传递的数据对象
     * @return outBean 返回对象
     */
    public OutBean saveSealRecord(ParamBean paramBean){
        OutBean outBean = new OutBean();
        // 获取判断参数isSeal，为yes则表示为盖章成功后，插入盖章记录
        if ("yes".equals(paramBean.getStr("isSeal"))) {
            // 根据印章ID，获取印章信息
            if (!"".equals(paramBean.getStr("SEAL_ID"))) {
                Bean sealBean = ServDao.find(SEAL_SERV, paramBean.getStr("SEAL_ID"));
                paramBean.set("SEAL_NAME", sealBean.getStr("SEAL_NAME"));
            }
        }
        Bean dataBean = ServDao.create(SEAL_RECORD_SERV, paramBean);
        if (dataBean != null && dataBean.getId().length() > 0) {
            outBean.set("SUCCESS", "true");
        } else {
            outBean.set("SUCCESS", "false");
        }
        return outBean;
    }
}
