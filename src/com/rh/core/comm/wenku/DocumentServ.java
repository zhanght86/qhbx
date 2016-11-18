package com.rh.core.comm.wenku;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.base.start.MsgLisLoader;
import com.rh.core.comm.cms.mgr.ChannelMgr;
import com.rh.core.comm.integral.IntegralMgr;
import com.rh.core.org.UserBean;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServMgr;
import com.rh.core.util.msg.CommonMsg;
import com.rh.core.util.msg.MsgCenter;

/**
 *文库文档 
 * 
 * @author liwei
 * 
 */
public class DocumentServ extends WenkuServ {

    @Override
    public OutBean byid(ParamBean paramBean) {
        paramBean.set("serv", ServMgr.SY_COMM_WENKU_DOCUMENT);
        OutBean result = super.byid(paramBean);
        WenkuMgr.getInstance().setExtendValues(result);
        String smtime = result.getStr("S_MTIME");
        if (null != smtime && 10 < smtime.length()) {
            result.set("S_MTIME_DATE", smtime.substring(0, 10));
        }
        return result;
    }

    /**
     * 是否为重要文档管理员
     * @param paramBean - 参数bean
     * @return - outbean
     */
    public OutBean importantDocumentAdmin(ParamBean paramBean) {
       UserBean u = Context.getUserBean();
       boolean result = WenkuMgr.getInstance().importantDocumentAdmin(u);
       return new OutBean().set("isAdmin", result);
    }

    @Override
    protected void afterSave(ParamBean paramBean, OutBean outBean) {
        // 异步处理缩略图
        // AsyncMessage message = new AsyncMessage(outBean, AsyncMgr.HandlerName.DOCUMENT_SNAPSHOT_HANDLER);
        // AsyncMgr.getInstance().addMessage(message);
        CommonMsg msg = new CommonMsg(outBean, MsgLisLoader.DOCSNAPSHOT_MSG_TYPE);
        MsgCenter.getInstance().addMsg(msg);

        Bean upBean = new Bean();
        // TODO 栏目跨站点移动时,新闻所属SITE_ID
        if (outBean.isEmpty("SITE_ID")) {
            String siteId = ChannelMgr.getInstance().getSiteId(outBean.getStr("DOCUMENT_CHNL"));
            upBean.set("SITE_ID", siteId).setId(outBean.getId());
        }

        if (null != upBean.getId() && 0 < upBean.getId().length()) {
            ServDao.save(ServMgr.SY_COMM_WENKU_DOCUMENT, upBean);
        }

        // 添加文档，增加积分
        if (paramBean.getAddFlag()) {
            Bean currentUser = Context.getUserBean();
            IntegralMgr.getInstance().handle(currentUser.getId(), SY_COMM_WENKU, ServMgr.SY_COMM_WENKU_DOCUMENT,
                    outBean.getId(), outBean.getStr("DOCUMENT_TITLE"), ServMgr.SY_COMM_WENKU_DOCUMENT + "_ADD");
        }
    }

    /**
     * 下载量 +1
     * @param param - 参数bean
     * @return out bean
     */
    public OutBean increaseDownloadCounter(ParamBean param) {
        WenkuMgr.getInstance().increaseDownloadCounter(param);
        return new OutBean(param).setOk();
    }

    /**
     * 阅读量 +1
     * @param param - 参数bean
     * @return out bean
     */
    public OutBean increaseReadCounter(ParamBean param) {
        WenkuMgr.getInstance().increaseReadCounter(param);
        return new OutBean(param).setOk();
    }

}
