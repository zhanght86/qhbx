package com.rh.bn.serv;

import com.rh.core.base.Bean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.comment.CommentServ;
/**
 * 印章销毁服务类
 * @author Lidongdong
 *
 */
public class SealDestInfo extends CommentServ{
    /**
     * 查询之前的拦截方法，由子类重载
     * @param paramBean 参数信息
     */
    protected void beforeQuery(ParamBean paramBean) {  
       if( paramBean.getStr("_linkWhere").equals("ADD")){
            String sealListStr = paramBean.get("sealListStr").toString();          
            paramBean.set("serv", "BN_SEAL_BASIC_INFO");
            paramBean.set("_linkWhere", sealListStr);
        }
    }
    protected void beforeSave(ParamBean paramBean) { 
        Bean sealBean = ServDao.find("BN_SEAL_BASIC_INFO", paramBean.getStr("SEAL_ID"));
        if(!sealBean.isEmpty()){
            paramBean.set("SEAL_NAME", sealBean.getStr("SEAL_NAME"));
            paramBean.set("SEAL_STATE", sealBean.get("SEAL_STATE"));
        }
            
    }
}
