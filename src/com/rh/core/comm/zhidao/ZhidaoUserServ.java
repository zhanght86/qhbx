package com.rh.core.comm.zhidao;

import com.rh.core.base.Bean;
import com.rh.core.comm.cms.CacheableServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServMgr;
import com.rh.core.util.Lang;

/**
 * 知道用户服务类
 * 
 */
public class ZhidaoUserServ extends CacheableServ {

//    @Override
//    protected void beforeSave(ParamBean paramBean) {
//       String userId =  paramBean.getStr("USER_ID");
//       ParamBean queryBean = new ParamBean();
//       queryBean.set("USER_ID", userId); 
//       Bean exits = ServDao.find(ServMgr.SY_COMM_ZHIDAO_USER, queryBean);
//       
//       if (null == exits) {
//           String uuid = Lang.getUUID();
//           paramBean.setId(uuid);
//           paramBean.set("_ADD_", true);
//       } else {
//           paramBean.setId(exits.getId());
//           paramBean.set("_ADD_", false);
//       }
//    }

    
    
    @Override
    public OutBean save(ParamBean paramBean) {
        String userId =  paramBean.getStr("USER_ID");
        ParamBean queryBean = new ParamBean();
        queryBean.set("USER_ID", userId); 
        Bean exits = ServDao.find(ServMgr.SY_COMM_ZHIDAO_USER, queryBean);
        if (null == exits) {
            String uuid = Lang.getUUID();
            paramBean.setId(uuid);
            paramBean.set("_ADD_", true);
        } else {
            paramBean.setId(exits.getId());
            paramBean.set("_ADD_", false);
        }
        return super.save(paramBean);
    }



    @Override
    protected String getCacheServId() {
        return ServMgr.SY_COMM_ZHIDAO_USER;
    }
}
