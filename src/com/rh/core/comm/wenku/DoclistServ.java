/*
 * Copyright (c) 2013 Ruaho All rights reserved.
 */
package com.rh.core.comm.wenku;

import java.math.BigDecimal;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.comm.cms.mgr.ReqhisMgr;
import com.rh.core.comm.integral.IntegralMgr;
import com.rh.core.comm.score.ScoreMgr;
import com.rh.core.org.UserBean;
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
public class DoclistServ extends CommonServ {
    private static final String DOCLIST_SERV = ServMgr.SY_COMM_WENKU_DOCLIST;

    /**
     * 查询文辑
     * @param paramBean 参数Bean
     * @return 查询结果
     */
    public OutBean byid(ParamBean paramBean) {
        paramBean.set("serv", ServMgr.SY_COMM_WENKU_DOCLIST);
        OutBean outBean = super.byid(paramBean);
        // TODO 配置化
        //outBean.set("TMPL_ID", "3nMOC20Vt91XKV7w45Orxn");
        outBean.set("TMPL_ID", Context.getSyConf("SY_WENKU_WENJI_TMPL", "3alE7Vs2Jf2q10DBanQYjWtI"));
        return outBean;
    }

    /**
     * 文辑获取投票评分信息
     * @param paramBean - param bean
     * @return out bean
     */
    public Bean show(Bean paramBean) {
        Bean doclist = ServDao.find(DOCLIST_SERV, paramBean);
        UserBean user = Context.getUserBean();
        String voted = ScoreMgr.getInstance().isVoted(DOCLIST_SERV, paramBean.getId(), user.getCode());
        // 是否已投票
        if (!("no_ip_voted".equals(voted)) && !("no_user_voted".equals(voted))) {
            doclist.set("VOTED", 1);
            doclist.set("VOTED_SCORE", voted);
            // String vote_ip =
        } else {
            doclist.set("VOTED", 2);
        }
        return doclist;
    }

    /**
     * 打分
     * @param paramBean - param bean
     * @return out bean
     */
    public Bean voteScore(ParamBean paramBean) {
        OutBean outBean = new OutBean();
        String doclistId = paramBean.getId();
        
        int score = paramBean.get("VOTE_SCORE", 0);
        Bean scoreBean = ScoreMgr.getInstance().voteScore(DOCLIST_SERV, doclistId, score);
        int total = scoreBean.get("SCORE_TOTAL", 0);
        int counter = scoreBean.get("SCORE_SCORER_COUNTER", 1);

        BigDecimal b = new BigDecimal(total / counter);
        float avg = b.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
        outBean.set("avg", avg + "");
        outBean.set("counter", counter);
        
        // 增加文档作者积分
        OutBean doclist = byid(paramBean);
        String owner = outBean.getStr("S_USER");
        IntegralMgr.getInstance().handle(owner, WenkuServ.SY_COMM_WENKU, ServMgr.SY_COMM_WENKU_DOCLIST,
                doclist.getId(), doclist.getStr("LIST_TITLE"), "SY_COMM_WENKU_DOCLIST_ADD");

        outBean.setOk("评分成功");
        return outBean;
    }

    /**
     * 阅读量 +1
     * @param param - 参数bean
     * @return out bean
     */
    public Bean increaseReadCounter(Bean param) {
        String key = "LIST_READ_COUNTER";
        Bean doclist = ServDao.find(ServMgr.SY_COMM_WENKU_DOCLIST, param);
        doclist.set(key, doclist.get(key, 0) + 1);
        ServDao.update(ServMgr.SY_COMM_WENKU_DOCLIST, doclist);

        // 更新阅读历史
        String act = "read";
        ReqhisMgr.save(ServMgr.SY_COMM_WENKU_DOCLIST, doclist.getId(), act, doclist.getStr("LIST_TITLE"),
                doclist.getStr("S_USER"));

        OutBean outBean = new OutBean(param);
        outBean.setOk();
        return outBean;
    }
    

    @Override
    protected void afterSave(ParamBean paramBean, OutBean outBean) {
        // 增加积分
        String owner = outBean.getStr("S_USER");
        IntegralMgr.getInstance().handle(owner, WenkuServ.SY_COMM_WENKU, ServMgr.SY_COMM_WENKU_DOCLIST,
                outBean.getId(), outBean.getStr("LIST_TITLE"), "SY_COMM_WENKU_DOCLIST_ADD");
        
    }

}
