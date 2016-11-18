package com.rh.oa.gw;

import com.rh.core.base.Bean;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;

/**
 * 
 * @author yangjy 浏览公文数据
 * 
 */
public class GwViewServ extends CommonServ {
    
    /**
     * 显示文档的自定义页面，直接显示正文
     * @param paramBean 参数Bean
     * @return 成文模板列表
     */
    public OutBean doc(Bean paramBean) {
        boolean isMobile = false;
        String dataId = paramBean.getId();
        String servId = paramBean.getStr("SERV_ID");       
        OutBean out = new OutBean();
        out.set("dataId", dataId);
        out.set("servId", servId);
        if (isMobile) {
            out.setToDispatcher("/oa/gw/view/OA_GW_TMPL_VIEW_mb.jsp");
        } else {
            out.setToDispatcher("/oa/gw/view/OA_GW_TMPL_VIEW.jsp");
        }
        return out;
    }
    
}
