package com.rh.oa.cn;

import com.rh.core.base.Bean;
import com.rh.core.plug.search.IndexListener;
import com.rh.core.plug.search.RhIndex;
import com.rh.oa.comm.AbstractIndexListener;

/**
 * 部门间工作联系函
 * @author yangjy
 * 
 */
public class CnIndexListener extends AbstractIndexListener implements IndexListener {

    
    public void index(RhIndex iaMsg, Bean searchDef, Bean data) {
        // 访问URL
        iaMsg.setUrl(createAccessUrl(data.getStr("CO_TITLE"), "OA_CN_CONCERT", data.getId()));

        // 增加授权
        grant(iaMsg, data);

        // 增加文件列表
        addFileList(iaMsg, data.getId(), "OA_CN_CONCERT");        
    }

}
