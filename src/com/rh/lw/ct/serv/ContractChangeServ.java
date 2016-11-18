package com.rh.lw.ct.serv;

import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.comm.FileServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.bean.SqlBean;

public class ContractChangeServ extends ContractServ{

    /*合同申请单  服务ID*/
    public static final String LW_CT_CONTRACT = "LW_CT_CONTRACT";
    
    public OutBean confirmChange(ParamBean paramBean){
        OutBean outBean = new OutBean();
        //获取主单ID
        String id = paramBean.getId();
        //获得 原合同 ID
        String CT_CHANGE_ID=paramBean.getStr("CT_CHANGE_ID");
        
        Bean contractBean=ServDao.find(LW_CT_CONTRACT, CT_CHANGE_ID);
        contractBean.set("CT_STATE", "1");
        ServDao.update(LW_CT_CONTRACT, contractBean);
        
        //查出原合同的自定义字段
       List<Bean> ct_FileBeans=ServDao.finds("SY_COMM_FILE", new SqlBean()
                                   .set("SERV_ID", LW_CT_CONTRACT).set("DATA_ID",CT_CHANGE_ID));
      
       for(Bean fileBean : ct_FileBeans){
           String OLD_FILE_ID=fileBean.getId();
           String NEW_SERV_ID=fileBean.getStr("SERV_ID");
           String NEW_DATA_ID=id;
           String NEW_FILE_CAT=fileBean.getStr("FILE_CAT");
           
           ParamBean fileParam=new ParamBean();
           fileParam.set("OLD_FILE_ID",OLD_FILE_ID);
           fileParam.set("NEW_SERV_ID",NEW_SERV_ID);
           fileParam.set("NEW_DATA_ID",NEW_DATA_ID);
           fileParam.set("NEW_FILE_CAT",NEW_FILE_CAT);
           
           FileServ fileServ=new FileServ();
           fileServ.copyFile(fileParam);
           
       }
       
//            把变更后合同的状态值设置为起草状态 0
//            ServDao.update(paramBean.getServId(), paramBean.set("CT_STATE", 0));
        //自动办结
//        SealUtil.finish(paramBean);
        return outBean;
    }
    
    
}
