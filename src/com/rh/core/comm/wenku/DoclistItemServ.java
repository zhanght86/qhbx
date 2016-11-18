/*
 * Copyright (c) 2013 Ruaho All rights reserved.
 */
package com.rh.core.comm.wenku;

import com.rh.core.base.Bean;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServMgr;

/**
 * 文辑
 * @author liwei
 * 
 */
public class DoclistItemServ extends CommonServ {
    
    private static final String DOCLIST_SERV = ServMgr.SY_COMM_WENKU_DOCLIST;
    
    private static final String DOCLIST_ITEM_SERV = ServMgr.SY_COMM_WENKU_DOCLIST_ITEM;
    
    /**
     * 更新文辑中包含的文档数量
     * @param paramBean 参数信息
     * @param outBean 输出信息
     */
    protected void afterSave(ParamBean paramBean, OutBean outBean) {
        String listId = paramBean.getStr("LIST_ID");
        int count = ServDao.count(DOCLIST_ITEM_SERV, new Bean().set("LIST_ID", listId));
        
        Bean updateBean = new Bean();
        updateBean.setId(listId).set("LIST_DOCUMENT_COUNTER", count);
        ServDao.update(DOCLIST_SERV, updateBean);
        outBean.setOk();
    }

}
