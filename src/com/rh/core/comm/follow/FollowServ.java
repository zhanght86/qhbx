/*
 * Copyright (c) 2013 Ruaho All rights reserved.
 */
package com.rh.core.comm.follow;

import com.rh.core.base.Context;
import com.rh.core.base.TipException;
import com.rh.core.comm.CacheMgr;
import com.rh.core.comm.cms.CacheableServ;
import com.rh.core.org.UserBean;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServMgr;

/**
 * 关注服务
 * @author zhangjinxi
 *
 */
public class FollowServ extends CacheableServ {
    
    /**
     * 增加一条关注信息
     * @param param - 传入参数
     * @return - 返回参数
     */
    public OutBean addFollow(ParamBean param) {
        UserBean userBean = Context.getUserBean();
        String userCode = userBean.getCode();
        String servId = param.getStr("SERV_ID");
        String dataId = param.getStr("DATA_ID");
        String dataDisName = param.getStr("DATA_DIS_NAME");
        String dataOwner = param.getStr("DATA_OWNER");
        if (userCode.equals(dataId)) {
            throw new TipException("不能关注");
        }
        FollowMgr.getInstance().follow(userCode, servId, dataId, dataDisName, dataOwner);
        OutBean outBean = new OutBean();
        outBean.setMsg("关注成功！");
        outBean.setOk();
        //清除缓存
        CacheMgr.getInstance().clearCache(getCacheType());
        return outBean;
    }
    
    /**
     * 删除一条关注信息
     * @param param - 传入参数
     * @return - 返回参数
     */
    public OutBean deleteFollow(ParamBean param) {
        UserBean userBean = Context.getUserBean();
        String userCode = userBean.getCode();
        String servId = param.getStr("SERV_ID");
        String dataId = param.getStr("DATA_ID");
        boolean flag = FollowMgr.getInstance().unFollow(userCode, servId, dataId);
        OutBean outBean = new OutBean();
        if (flag) {
            outBean.setOk();
        }
        //将人物信息从缓存中清除
        CacheMgr.getInstance().clearCache(getCacheType());
       /* String paramKey = getKey(param);
        ParamBean queryBean = new ParamBean();
        queryBean.setServId("SY_COMM_FOLLOW");
        queryBean.setQueryExtWhere(" and DATA_ID='"+param.getStr("DATA_ID")+"' and USER_CODE='"+param.getStr("USER_CODE")+"'");
        queryBean.setAct("query");
        outBean = ServMgr.act(queryBean);
        
        // put in cache
        putInCache(paramKey, outBean);*/
        return outBean;
    }

    @Override
    protected String getCacheServId() {
        return ServMgr.SY_COMM_FOLLOW;
    }
    
}
