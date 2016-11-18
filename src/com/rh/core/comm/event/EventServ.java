package com.rh.core.comm.event;

import com.rh.core.comm.cms.CacheableServ;
import com.rh.core.serv.ServMgr;

/**
 * 
 * 事件(动态)服务
 * @author liwei
 * 
 */
public class EventServ extends CacheableServ {

    @Override
    protected String getCacheServId() {
        return ServMgr.SY_COMM_EVENT;
    }
}
