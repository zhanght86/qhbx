package com.rh.bn.serv;

import com.rh.core.base.Bean;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServMgr;

public class SealUseRecord extends CommonServ{
    public OutBean sealRecord(ParamBean paramBean){
        String servId = paramBean.getStr("SERV_ID");
        String sealId = paramBean.getStr("SEAL_ID");
        String title = paramBean.getStr("TITLE");
        String dataId = paramBean.getStr("DATA_ID");
        String fileId = paramBean.getStr("FILE_ID");
        Bean dataBean = ServDao.find(servId, dataId);
        Bean fileBean = ServDao.find("SY_COMM_FILE", fileId);
        Bean deptBean = ServDao.find("SY_ORG_DEPT",dataBean.getStr("S_DEPT"));
        ParamBean sealRecordBean = new ParamBean();
        sealRecordBean.set("USE_USERNAME", dataBean.get("S_USER"));
        sealRecordBean.set("USE_DEPTNAME", deptBean.get("ODEPT_CODE"));
        sealRecordBean.set("USE_TDEPTNAME", deptBean.get("TDEPT_CODE"));
        sealRecordBean.set("USE_ORGNAME", deptBean.get("DEPT_NAME"));
        sealRecordBean.set("USE_ORGCODE", deptBean.getId());
        sealRecordBean.set("FILE_NAME", fileBean.get("FILE_NAME"));
        sealRecordBean.set("FILE_ID", fileBean.getId());
        sealRecordBean.set("APPLY_TYPE", servId);
        sealRecordBean.set("APPLY_TITLE", title);
        sealRecordBean.set("APPLY_ID", dataId);
        sealRecordBean.set("SEAL_ID", sealId);
        sealRecordBean.set("USE_WAY", "1");
        OutBean outBean = ServMgr.act(paramBean);
        return outBean;
    }
}
