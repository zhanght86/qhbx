/*
 * Copyright (c) 2013 Ruaho All rights reserved.
 */
package com.rh.core.comm.score;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServMgr;
import com.rh.core.util.RequestUtils;

/**
 * 评分管理
 * @author liwei
 * 
 */
public class ScoreMgr {
    // 日志记录
    @SuppressWarnings("unused")
    private static Log log = LogFactory.getLog(ScoreMgr.class);

    private static ScoreMgr instance = new ScoreMgr();

    /**
     * Singleton
     * @return - Singleton instance
     */
    public static ScoreMgr getInstance() {
        return instance;
    }

    /**
     * can not new instance
     */
    private ScoreMgr() {
    }
    
    private static final String SCORE_SERV = ServMgr.SY_COMM_SCORE;
    
    private static final String SCORE_VOTE_SERV = ServMgr.SY_COMM_SCORE_VOTE;
    
    
    /**
     * 获取投票评分信息
     * @param servId - 服务ID
     * @param dataId - 数据ID
     * @param user - 评分用户
     * @return 是否已打分
     * 
     */
    public String isVoted(String servId, String dataId, String user) {
        String voted = isUserVoted(servId, dataId, user);
        return voted;
    }

    /**
     * 打分
     * @param servId - 服务ID
     * @param dataId - 数据ID
     * @param score - 分数
     * @return out bean
     */
    public Bean voteScore(String servId, String dataId, int score) {
        //增加评分记录
        //添加用户对该条数据的详细评分记录
        Bean vote = new Bean();
        vote.set("VOTE_SCORE", score);
        vote.set("SERV_ID", servId);
        vote.set("DATA_ID", dataId);
        vote.set("VOTE_IP", RequestUtils.getIpAddr(Context.getRequest()));
        if (null != Context.getUserBean() && null != Context.getUserBean().getId()) {
            vote.set("VOTE_USER", Context.getUserBean().getCode());
        }
        ServDao.save(SCORE_VOTE_SERV, vote);
        
        //更新评分数据
        //更新某条数据的总分、评分人数等
        Bean scoreBean = ServDao.find(SCORE_SERV, new Bean().set("SERV_ID", servId).set("DATA_ID", dataId));
        if (null == scoreBean || scoreBean.isEmpty()) {
            scoreBean = new Bean();
            scoreBean.set("SERV_ID", servId);
            scoreBean.set("DATA_ID", dataId);
            if (null != Context.getUserBean() && null != Context.getUserBean().getId()) {
                scoreBean.set("S_USER", Context.getUserBean().getCode());
            }
        }
        
        scoreBean.set("SCORE_SCORER_COUNTER", scoreBean.get("SCORE_SCORER_COUNTER", 0) + 1);
        scoreBean.set("SCORE_TOTAL", scoreBean.get("SCORE_TOTAL", 0) + score);
        scoreBean = ServDao.save(SCORE_SERV, scoreBean);
        return scoreBean;
    }
    
    
//    public Bean getScore(String servId, String dataId) {
//        Bean oldBean = ServDao.find(servId, dataId);
//        //获取所有评分数据
//        Bean scoreBean = new Bean().set("DATA_ID", dataId)
//                                   .set("SERV_ID", servId);
//        List<Bean> scoreBeans = ServDao.finds(SCORE_VOTE_SERV, scoreBean);
//        float total = 0.0f;
//        for (int i = 0; i < scoreBeans.size(); i++) {
//            Bean b = scoreBeans.get(i);
//            total += b.getInt("VOTE_SCORE");
//        }
//        
//        Bean outBean = new Bean();
//        outBean.set("SCORER_COUNTER", scoreBeans.size());
//        outBean.set("SCORE_TOTAL", total);
//    }
    

    /**
     * 当前访问者是否已进行打分
     * @param servId - 服务ID 
     * @param dataId - 数据ID
     * @param userId - 用户
     * @return - this user voted?
     */
    private String isUserVoted(String servId, String dataId, String userId) {
        String ip = RequestUtils.getIpAddr(Context.getRequest());
//        UserBean user = Context.getUserBean();
        String result = "";
        // 验证IP
        result = ipVoted(servId, dataId, ip);
        if (!("no_ip_voted".equals(result))) {
            return result;
        }
        // 验证用户
        result = userVoted(servId, dataId, userId);
        return result;
    }

    /**
     * vate ip check, if this ip voted we return true
     * @param servId - 服务ID
     * @param dataId - 数据ID
     * @param user - user
     * @return this user voted?
     */
    private String userVoted(String servId, String dataId, String user) {
        Bean vote = new Bean();
        vote.set("SERV_ID", servId);
        vote.set("DATA_ID", dataId);
        vote.set("VOTE_USER", user);
        int exits = ServDao.count(SCORE_VOTE_SERV, vote);
        if (0 == exits) {
            return "no_user_voted";
        } else {
            Bean param = new Bean();
            param.set("SERV_ID", servId);
            param.set("DATA_ID", dataId);
            param.set("VOTE_USER", user);
            Bean bean = ServDao.find("SY_COMM_SCORE_VOTE", param);
            String voteScore = bean.getStr("VOTE_SCORE");
            return voteScore;
        }
    }

    /**
     * vate ip check, if this ip voted we return true
     * @param servId - 服务ID
     * @param dataId - 数据ID
     * @param ip - ip addr
     * @return this ip voted?
     */
    private String ipVoted(String servId, String dataId, String ip) {
        Bean vote = new Bean();
        vote.set("SERV_ID", servId);
        vote.set("DATA_ID", dataId);
        vote.set("VOTE_IP", ip);
        int exits = ServDao.count(SCORE_VOTE_SERV, vote);
        if (0 == exits) {
            //没有此ip的评分
            return "no_ip_voted";
        } else {
            //有此ip的评分 SY_COMM_SCORE_VOTE
            Bean param = new Bean();
            param.set("SERV_ID", servId);
            param.set("DATA_ID", dataId);
            param.set("VOTE_IP", ip);
            Bean bean = ServDao.find("SY_COMM_SCORE_VOTE", param);
            String voteScore = bean.getStr("VOTE_SCORE");
            return voteScore;
        }
    }

}
