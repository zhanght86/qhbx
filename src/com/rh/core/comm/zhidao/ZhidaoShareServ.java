package com.rh.core.comm.zhidao;

import com.rh.core.comm.share.ShareServ;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServMgr;

/**
 * 
 * 知道分享服务
 * @author zhangjinxi
 * 
 */
public class ZhidaoShareServ extends CommonServ {

    /**
     * 添加知道分享信息
     * @param paramBean - 传入参数
     * @return - 传出参数
     */
    public OutBean addShare(ParamBean paramBean) {
        ParamBean param = new ParamBean();
        param.set("TARGET_USER", paramBean.getStr("TARGET_USER"));
        param.set("SERV_ID", paramBean.getStr("SERV_ID"));
        param.set("DATA_ID", paramBean.getStr("DATA_ID"));
        param.set("SERV_GROUP", "SY_COMM_ZHIDAO");
        param.set("SHARE_CONTENT", paramBean.getStr("SHARE_CONTENT"));
        return new ShareServ().addShare(param);
    }
    
    /**
     * 统计某人被分享的次数
     * @param paramBean - userCode
     * @return - 总的分享次数
     */
    public int getShareCount(ParamBean paramBean) {
        String userCode = paramBean.getStr("userId");
        ParamBean param = new ParamBean();
        param.setServId(ServMgr.SY_COMM_SHARE);
        param.setAct(ServMgr.ACT_COUNT);
        param.setWhere(" and SERV_GROUP = 'SY_COMM_ZHIDAO' and SHARE_AUTHOR = '" + userCode + "'");
        return (Integer) ServMgr.act(param).getData();
    }
}