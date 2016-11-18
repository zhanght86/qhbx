package com.rh.core.comm.share;

import com.rh.core.comm.cms.CacheableServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServMgr;

/**
 * 
 * 分享服务
 * @author zhangjinxi
 * 
 */
public class ShareServ extends CacheableServ {

    /**
     * 添加分享信息
     * @param paramBean - 传入参数
     * @return - 传出参数
     */
    public OutBean addShare(ParamBean paramBean) {
        String targetUsers = paramBean.getStr("TARGET_USER");
        String servId = paramBean.getStr("SERV_ID");
        String dataId = paramBean.getStr("DATA_ID");
        String servGroup = paramBean.getStr("SERV_GROUP");
        String shareContent = paramBean.getStr("SHARE_CONTENT");
        
        //查询提问或者回答的作者
        ParamBean authorParam = new ParamBean();
        authorParam.setServId(servId);
        authorParam.setAct(ServMgr.ACT_BYID);
        authorParam.setId(dataId);
        String shareAuthor = ServMgr.act(authorParam).getStr("S_USER");
        
        //向SY_COMM_SHARE_ITEM服务中存入数据
        ParamBean itemParam = new ParamBean();
        itemParam.set("SHARE_CONTENT", shareContent);
        itemParam.set("SHARE_AUTHOR", shareAuthor);
        itemParam.set("SERV_GROUP", servGroup);
        itemParam.set("SERV_ID", servId);
        itemParam.set("DATA_ID", dataId);
        String itemId = ServMgr.act(ServMgr.SY_COMM_SHARE_ITEM, ServMgr.ACT_SAVE, itemParam).getId();
        
        //向SY_COMM_SHARE服务中存入数据
        String[] targetUser = targetUsers.split(",");
        if (targetUser.length > 0) {
            for (int i = 0; i < targetUser.length; i++) {
                ParamBean shareParam = new ParamBean();
                shareParam.set("ITEM_ID", itemId);
                shareParam.set("SERV_GROUP", servGroup);
                shareParam.set("SERV_ID", servId);
                shareParam.set("DATA_ID", dataId);
                shareParam.set("TARGET_USER", targetUser[i]);
                ServMgr.act(ServMgr.SY_COMM_SHARE, ServMgr.ACT_SAVE, shareParam);
            }
        }
        return new OutBean();
    }
    
    
    /**
     * 删除分享信息
     * @param paramBean - 传入参数
     * @return - 传出参数
     */
    public OutBean deleteShare(ParamBean paramBean) {
        return new OutBean();
    }


    @Override
    protected String getCacheServId() {
        return ServMgr.SY_COMM_SHARE;
    }
}
