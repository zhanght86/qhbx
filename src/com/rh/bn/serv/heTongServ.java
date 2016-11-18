package com.rh.bn.serv;

import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.bean.SqlBean;

public class heTongServ {
    public OutBean getNodeCode(ParamBean paramBean){
        String nodeName = paramBean.getStr("S_WF_NODE");
        OutBean outBean = new OutBean();
        if(nodeName.length()!=0&&nodeName.split(",").length==1){
            Bean instBean = ServDao.find("SY_WFE_PROC_INST", paramBean.getStr("S_WF_INST"));
            List<Bean> nodeBeans = ServDao.finds("SY_WFE_NODE_DEF", new SqlBean().set("PROC_CODE", instBean.getStr("PROC_CODE")).set("NODE_NAME", nodeName));
            for (Bean nodeBean :nodeBeans) {
                outBean.set("NODE_CODE", nodeBean.getStr("NODE_CODE"));
            }
        }
        if(nodeName.length()==0){
            outBean.set("NODE_CODE", 2);
        }
        return outBean;
    }
}
