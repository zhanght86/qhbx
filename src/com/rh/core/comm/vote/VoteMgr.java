/*
 * Copyright (c) 2013 Ruaho All rights reserved.
 */
package com.rh.core.comm.vote;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServMgr;
import com.rh.core.util.RequestUtils;

/**
 * 投票记录管理
 * @author liwei
 * 
 */
public class VoteMgr {
    // 日志记录
    @SuppressWarnings("unused")
    private static Log log = LogFactory.getLog(VoteMgr.class);

    private static VoteMgr instance = new VoteMgr();

    /**
     * Singleton
     * @return - Singleton instance
     */
    public static VoteMgr getInstance() {
        return instance;
    }

    /**
     * can not new instance
     */
    private VoteMgr() {
    }
    
    private static final String VOTE_SERV = ServMgr.SY_COMM_VOTE;
    
    
    /**
     * 获取投票评分信息
     * @param servId - 服务ID
     * @param dataId - 数据ID
     * @param user - 评分用户
     * @return 是否已打分
     * 
     */
    public boolean isVoted(String servId, String dataId, String user) {
        boolean voted = isUserVoted(servId, dataId, user);
        return voted;
    }

    /**
     * 投票
     * @param servId - 服务ID
     * @param dataId - 数据ID
     * @param voteValue - 投票值
     */
    public void vote(String servId, String dataId, int voteValue) {
        //增加投票记录
        //添加用户对该条数据的详细投票记录
        Bean vote = new Bean();
        vote.set("VOTE_VALUE", voteValue);
        vote.set("SERV_ID", servId);
        vote.set("DATA_ID", dataId);
        vote.set("VOTE_IP", RequestUtils.getIpAddr(Context.getRequest()));
        if (null != Context.getUserBean() && null != Context.getUserBean().getId()) {
            vote.set("VOTE_USER", Context.getUserBean().getCode());
        }
        ServDao.save(VOTE_SERV, vote);
    }
    

    /**
     * 当前访问者是否已进行打分
     * @param servId - 服务ID 
     * @param dataId - 数据ID
     * @param userId - 用户
     * @return - this user voted?
     */
    private boolean isUserVoted(String servId, String dataId, String userId) {
        String ip = RequestUtils.getIpAddr(Context.getRequest());
//        UserBean user = Context.getUserBean();
        boolean result = false;
        // 验证IP
        result = ipVoted(servId, dataId, ip);
        if (!result) {
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
    private boolean userVoted(String servId, String dataId, String user) {
        Bean vote = new Bean();
        vote.set("SERV_ID", servId);
        vote.set("DATA_ID", dataId);
        vote.set("VOTE_USER", user);
        int exits = ServDao.count(VOTE_SERV, vote);
        if (0 == exits) {
            return false;
        }
        return true;
    }

    /**
     * vate ip check, if this ip voted we return true
     * @param servId - 服务ID
     * @param dataId - 数据ID
     * @param ip - ip addr
     * @return this ip voted?
     */
    private boolean ipVoted(String servId, String dataId, String ip) {
        Bean vote = new Bean();
        vote.set("SERV_ID", servId);
        vote.set("DATA_ID", dataId);
        vote.set("VOTE_IP", ip);
        int exits = ServDao.count(VOTE_SERV, vote);
        if (0 == exits) {
            return false;
        }
        return true;
    }

}
