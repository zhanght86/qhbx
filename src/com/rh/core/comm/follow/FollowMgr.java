/*
 * Copyright (c) 2013 Ruaho All rights reserved.
 */
package com.rh.core.comm.follow;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rh.core.base.Bean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServMgr;

/**
 * 关注管理
 * @author zhangjinxi
 * 
 */
public class FollowMgr {
    // 日志记录
    @SuppressWarnings("unused")
    private static Log log = LogFactory.getLog(FollowMgr.class);

    private static FollowMgr instance = new FollowMgr();

    // private static final String CACHE_TYPE_FOLLOW_STATISTICS = "SY_COMM_FOLLOW_STATISTICS";

    /**
     * Singleton
     * @return - Singleton instance
     */
    public static FollowMgr getInstance() {
        return instance;
    }

    /**
     * can not new instance
     */
    private FollowMgr() {
    }

    /**
     * 添加关注信息
     * @param userCode 关注人编码
     * @param servId 服务编码
     * @param dataId 数据项编码
     * @param dataDisName 数据项显示名称
     * @param dataOwner 数据所有者
     */
    public void follow(String userCode, String servId, String dataId, String dataDisName, String dataOwner) {
        /** 这里没有判断这条数据是否是重复添加 */
        boolean flag = true;
        flag = isFollow(userCode, servId, dataId);
        if (flag) {
            return;
        } else {
            Bean param = new Bean();
            param.set("USER_CODE", userCode);
            param.set("SERV_ID", servId);
            param.set("DATA_ID", dataId);
            param.set("DATA_DIS_NAME", dataDisName);
            param.set("DATA_OWNER", dataOwner);
            ServDao.save(ServMgr.SY_COMM_FOLLOW, param);
        }
    }

    /**
     * 取消关注的信息
     * @param userCode 关注人编码
     * @param servId 服务编码
     * @param dataId 数据项编码
     * @return 是否成功取消关注
     */
    public boolean unFollow(String userCode, String servId, String dataId) {
        if (isFollow(userCode, servId, dataId)) {
            boolean flag = false;
            Bean param = new Bean();
            param.set("USER_CODE", userCode);
            param.set("SERV_ID", servId);
            param.set("DATA_ID", dataId);
            int count = ServDao.deletes(ServMgr.SY_COMM_FOLLOW, param);
            if (count >= 1) {
                flag = true;
            }
            return flag;
        } else {
            return true;
        }
    }

    /**
     * 判断用户是否已关注信息
     * @param userCode 关注人编码
     * @param servId 服务编码
     * @param dataId 数据项编码
     * @return 是否已经关注(true:已关注，false:未关注)
     */
    private boolean isFollow(String userCode, String servId, String dataId) {
        boolean flag = false;
        Bean param = new Bean();
        param.set("USER_CODE", userCode);
        param.set("SERV_ID", servId);
        param.set("DATA_ID", dataId);
        Bean bean = ServDao.find(ServMgr.SY_COMM_FOLLOW, param);
        if (bean != null) {
            flag = true;
        }
        return flag;
    }

}
