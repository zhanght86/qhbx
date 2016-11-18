package com.rh.bn.util;

import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.bean.SqlBean;
import com.rh.core.wfe.serv.ProcDefServ;

public class SealUtil {
    /**
     * 自动办结
     * @param paramBean
     */
    public static void finish(ParamBean paramBean) {
        ParamBean wfBean = new ParamBean();
        Bean bean = ServDao.find(paramBean.getServId(), paramBean.getId());
        wfBean.set("PI_ID",bean.get("S_WF_INST"));
        //查询节点实例ID
        List<Bean> nodeBeans = ServDao.finds("SY_WFE_NODE_INST", new SqlBean().set("PI_ID", bean.get("S_WF_INST")).set("INST_IF_RUNNING", 1).set("NODE_NAME", bean.get("S_WF_NODE")));
        if(nodeBeans.size()==1){
            wfBean.set("NI_ID",nodeBeans.get(0).get("NI_ID"));
            wfBean.set("INST_IF_RUNNING",1);
        }else{
            return;
        }
        new ProcDefServ().finish(wfBean);
    }
}
