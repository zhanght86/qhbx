package com.rh.oa.lib;

import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.plug.search.IndexListener;
import com.rh.core.plug.search.RhIndex;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServDefBean;
import com.rh.core.serv.ServUtils;
import com.rh.oa.comm.AbstractIndexListener;

/**
 * 为文库数据创建索引。
 * @author yangjy
 * 
 */
public class LibIndexListener extends AbstractIndexListener implements IndexListener {

    
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
    
    @Override
    protected void grant(RhIndex iaMsg, Bean data) {
        ParamBean param = new ParamBean();
        param.set("OBJ_ID", data.getId());
        
        //@TODO 取授权信息
        List<Bean> acls = ServDao.finds("OA_LIB_ACL", param);
        for (Bean acl : acls) {
            
        }
        
        //@TODO 增加授权范围 机构级别
        
    }
    
    @Override
    protected void addFileList(RhIndex iaMsg, String dataId, String servId) {
        //@TODO 文件新增、删除修改ITEM时间
        
        //@TODO 授权信息变更修改ITEM时间
        
        //@TODO 取得文件列表 文件是否可以加访问URL
        
        
    }

}
