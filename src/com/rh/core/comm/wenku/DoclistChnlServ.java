/*
 * Copyright (c) 2013 Ruaho All rights reserved.
 */
package com.rh.core.comm.wenku;

import com.rh.core.comm.cms.serv.ChannelServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;

/**
 * 文辑分类 服务
 * @author liwei
 * 
 */
public class DoclistChnlServ extends ChannelServ {

    /**
     * 查询文辑
     * @param paramBean 参数Bean
     * @return 查询结果
     */
    public OutBean byid(ParamBean paramBean) {
        OutBean outBean = super.byid(paramBean);
        //TODO 配置化
        outBean.set("TMPL_ID", "0up4eHFGh0KaZqs6pR96Y0S");
        return outBean;
    }

}
