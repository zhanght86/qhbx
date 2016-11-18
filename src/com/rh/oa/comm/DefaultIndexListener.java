package com.rh.oa.comm;

import com.rh.core.base.Bean;
import com.rh.core.plug.search.IndexListener;
import com.rh.core.plug.search.RhIndex;
import com.rh.core.serv.ServDefBean;
import com.rh.core.serv.ServUtils;

/**
 * 为OA 系统多数审批单创建索引的监听类。一般的审批单都可以用。
 * @author yangjy
 * 
 */
public class DefaultIndexListener extends AbstractIndexListener implements IndexListener {

    
    public void index(RhIndex iaMsg, Bean searchDef, Bean data) {
        final String servId = searchDef.getStr("SERV_ID");

        final ServDefBean sdb = ServUtils.getServDef(servId);
        String title = ServUtils.replaceSysAndData(sdb.getDataTitle(), data);
        
        // 生成访问URL。servId 不一定正确，例如公文就不能用这个ID。
        iaMsg.setUrl(createAccessUrl(title, servId, data.getId()));

        // 增加授权
        grant(iaMsg, data);

        // 增加文件列表
        addFileList(iaMsg, data.getId(), servId);
    }

}
