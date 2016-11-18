package com.rh.lw.ct.listener;

import com.rh.core.base.Bean;
import com.rh.core.plug.search.IndexListener;
import com.rh.core.plug.search.RhIndex;
import com.rh.oa.comm.AbstractIndexListener;

/**
 * 为合同创建索引的监听类
 * @author yangjy
 * 
 */
public class CtIndexListener extends AbstractIndexListener implements IndexListener {


    public void index(RhIndex iaMsg, Bean searchDef, Bean data) {
        // 访问URL
        iaMsg.setUrl(createAccessUrl(data.getStr("GW_TITLE"), "LW_CT_CONTRACT", data.getId()));

        // 增加授权
        grant(iaMsg, data);

        // 增加文件列表
        addFileList(iaMsg, data.getId(), "LW_CT_CONTRACT");

    }

}
