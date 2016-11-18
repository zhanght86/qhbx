/*
 * Copyright (c) 2013 Ruaho All rights reserved.
 */
package com.rh.core.comm.integral;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rh.core.base.Bean;
import com.rh.core.comm.CacheMgr;
import com.rh.core.org.UserBean;
import com.rh.core.org.mgr.UserMgr;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServMgr;
import com.rh.core.util.Constant;
import com.rh.core.util.DateUtils;

/**
 * 积分管理
 * @author liwei
 * 
 */
public class IntegralMgr {
    // 日志记录
    @SuppressWarnings("unused")
    private static Log log = LogFactory.getLog(IntegralMgr.class);

    private static IntegralMgr instance = new IntegralMgr();

    private static final String CACHE_TYPE_INTEGRAL_STATISTICS = "SY_COMM_INTEGRAL_STATISTICS";

    /**
     * Singleton
     * @return - Singleton instance
     */
    public static IntegralMgr getInstance() {
        return instance;
    }

    /**
     * can not new instance
     */
    private IntegralMgr() {
    }

    /**
     * 根据规则增加、减少用户积分.<br>
     * 当指定积分规则不存在或用户剩余积分不足时，我们将抛出<code>RuntimeException</code>异常
     * 
     * @param userId - 用户ID
     * @param servGroup - 分组ID
     * @param servId - 服务ID
     * @param dataId - 数据ID
     * @param dataDisName - 数据显示名称
     * @param actCode - 操作代码
     * 
     *            //去掉参数 activeTime - 处理有效周期ms (可控制在指定周期内重复操作是否支持积分重复处理) <br>
     *            -1: 有效期为永久，表示该记录只会引起一次积分的 增加/减少 0: 有效期为0, 表示该记录每次操作都会引起积分的 增加/减少
     */
    public void handle(String userId, String servGroup, String servId, String dataId,
            String dataDisName, String actCode) {
        // check is scored?
        if (isHandled(userId, servId, dataId, actCode)) {
            return;
        } 

        int total = 0;
        // step1 find integral rule
        Bean query = new Bean();
        // query.set("SERV_ID", servId);
        query.set("ACT_CODE", actCode);
        Bean rule = ServDao.find(ServMgr.SY_COMM_INTEGRAL_RULE, query);

        if (null == rule || rule.isEmpty()) {
            throw new RuntimeException("integral rule not found!");
        }

        // 检查每日上限
        int dailyMax = rule.get("DAILY_MAX", 0);
        if (0 < dailyMax) {
            ParamBean dailyQuery = new ParamBean(ServMgr.SY_COMM_INTEGRAL_SCORE, ServMgr.ACT_COUNT);
            dailyQuery.set(Constant.PARAM_SELECT, "SUM(INTEGRAL_VALUE) as COUNTER");
            StringBuilder wb = new StringBuilder();
            wb.append(" AND USER_ID = '" + userId + "'");
            wb.append(" AND SERV_ID = '" + servId + "'");
            wb.append(" AND ACT_CODE = '" + actCode + "'");
            String yesterday = DateUtils.getCertainDate(-1);
            String tomorrow = DateUtils.getCertainDate(1);
            wb.append(" AND S_ATIME > '" + yesterday + "' ");
            wb.append(" AND S_ATIME < '" + tomorrow + "' ");
            dailyQuery.set(Constant.PARAM_WHERE, wb.toString());
            Bean result = ServDao.find(ServMgr.SY_COMM_INTEGRAL_SCORE, dailyQuery);
            int todaySum = result.get("COUNTER", 0);

            int scoreValue = rule.getInt("RULE_SCORE");

            if ((todaySum + scoreValue) > dailyMax) {
                // 已达到今日上限限制，不继续处理
                return;
            }
        }

        // setp2 get current integral
        Bean userIntegral = ServDao.find(ServMgr.SY_COMM_INTEGRAL,
                new Bean().set("USER_ID", userId).set("SERV_ID", servId).set("SERV_GROUP", servGroup));
        if (null == userIntegral || userIntegral.isEmpty()) {
            userIntegral = new Bean();
            userIntegral.set("SERV_ID", servId);
            userIntegral.set("USER_ID", userId);
            userIntegral.set("SERV_GROUP", servGroup);
            userIntegral.set("INTEGRAL_VALUE", getDefaultIntegralValue(userId, servId));
        }

        // step3 update integral
        int scoreValue = rule.getInt("RULE_SCORE");
        int currentValue = userIntegral.getInt("INTEGRAL_VALUE");
        total = currentValue + scoreValue;
        if (0 > total) {
            throw new RuntimeException("user integral is not enough.");
        }
        userIntegral.set("INTEGRAL_VALUE", total);
        ServDao.save(ServMgr.SY_COMM_INTEGRAL, userIntegral);

        // step4 score log
        Bean score = new Bean();
        score.set("INTEGRAL_VALUE", currentValue);
        score.set("USER_ID", userId);
        score.set("INTEGRAL_SCORE", scoreValue);
        score.set("ACT_CODE", actCode);
        score.set("SERV_GROUP", servGroup);
        score.set("SERV_ID", servId);
        score.set("DATA_ID", dataId);
        score.set("DATA_DIS_NAME", dataDisName);
        ServDao.save(ServMgr.SY_COMM_INTEGRAL_SCORE, score);

    }

    /**
     * 是否已经完成积分处理
     * 
     * @param userId - 用户ID
     * @param servId - 服务ID
     * @param dataId - 数据ID
     * @param actCode - 操作代码
     * @return true:已完成积分处理, false:未完成
     */
    private boolean isHandled(String userId, String servId, String dataId, String actCode) {
        boolean isScored = false;
        Bean queryBean = new Bean();
        queryBean.set("USER_ID", userId);
        queryBean.set("SERV_ID", servId);
        queryBean.set("DATA_ID", dataId);
        queryBean.set("ACT_CODE", actCode);
        Bean score = ServDao.find(ServMgr.SY_COMM_INTEGRAL_SCORE, queryBean);
        if (null != score && !score.isEmpty()) {
            isScored = true;
        }
        return isScored;
    }

    /**
     * TODO： cache 获取用户系统积分总量<br>
     * 各个服务积分之和
     * 
     * @param userId - 用户ID
     * @return 总积分量
     */
    public int getUserTotalIntegral(String userId) {
        Bean queryBean = new Bean();
        queryBean.set("USER_ID", userId);
        List<Bean> list = ServDao.finds(ServMgr.SY_COMM_INTEGRAL, queryBean);
        int total = 0;
        if (null == list) {
            return total;
        }
        for (Bean integral : list) {
            total += integral.get("INTEGRAL_VALUE", 0);
        }
        return total;
    }

    /**
     * 获取用户现有积分
     * @param servGroup - 服务分组
     * @param userId - 用户ID
     * @return 现有积分
     */
    public int getUserIntegral(String servGroup, String userId) {
        Bean queryBean = new Bean();
        queryBean.set("SERV_ID", servGroup);
        queryBean.set("USER_ID", userId);
        Bean integral = ServDao.find(ServMgr.SY_COMM_INTEGRAL, queryBean);

        if (null == integral || integral.isEmpty()) {
            return 0;
        } else {
            return integral.get("INTEGRAL_VALUE", 0);
        }
    }

    /**
     * 查询积分排行最高的前<CODE>topN</CODE>条记录
     * @param servGroup - 服务ID
     * @param topN - topN
     * @return 查询结果
     */
    public List<Bean> getTop(String servGroup, int topN) {
        ParamBean param = new ParamBean(ServMgr.SY_COMM_INTEGRAL, ServMgr.ACT_FINDS);
        param.setWhere(" and SERV_GROUP = '" + servGroup + "'");
        param.setOrder("INTEGRAL_VALUE DESC");
        param.setShowNum(topN);
        
        //get data from cache first
        
        List<Bean> list = ServMgr.act(param).getDataList();
        // replace user name
        for (Bean bean : list) {
            UserBean user = UserMgr.getUser(bean.getStr("USER_ID"));
            bean.set("USER_NAME", user.getName());
        }
        return list;
    }
    
    /**
     * 查询某个chnlId下积分排行最高的前<CODE>topN</CODE>条记录
     * @param servGroup - 服务ID
     * @param topN - topN
     * @param chnlId - 专家领域ID
     * @return 查询结果
     */
    public List<Bean> getTopByChnl(String servGroup, int topN, String chnlId) {
        ParamBean param = new ParamBean(ServMgr.SY_COMM_ZHIDAO_SPEC_ALL_INTE, ServMgr.ACT_QUERY);
        param.setQueryExtWhere(" and SERV_ID = '" + servGroup + "'");
        param.setOrder("INTEGRAL_VALUE DESC");
        if (null != chnlId && 0 < chnlId.length()) {
            List<Bean> treeWhere = new ArrayList<Bean>();
            treeWhere.add(new Bean().set("DICT_ITEM", "CHNL_ID").set("DICT_VALUE", chnlId));
            param.set("_treeWhere", treeWhere);
        }
        param.setQueryPageShowNum(topN);
        param.setQueryPageNowPage(1);
        
        //排重，因为一个专家会有多个领域
        param.setSelect("distinct(USER_ID) ,INTEGRAL_VALUE");
        
        //get data from cache first
        
        List<Bean> list = ServMgr.act(param).getDataList();
        // replace user name
        for (Bean bean : list) {
            UserBean user = UserMgr.getUser(bean.getStr("USER_ID"));
            bean.set("USER_NAME", user.getName());
        }
        return list;
    }

    /**
     * TODO: cache 统计<CODE>date</CODE>至今为止积分增加最多的用户<br>
     * 统计数据会被系统缓存
     * @param servGroup - 服务ID
     * @param date - 起始时间
     * @param topN - 前几名
     * @return 统计结果
     */
    public List<Bean> topStatistics(String servGroup, Date date, int topN) {
        String dateStr = DateUtils.formatDate(date);
        ParamBean queryBean = new ParamBean();
        String where = " AND S_MTIME > '" + dateStr + "'";
        where += " AND SERV_GROUP='" + servGroup + "'  AND INTEGRAL_SCORE > 0 ";
        queryBean.setSelect("USER_ID, SUM(INTEGRAL_SCORE) as SCORE");
        queryBean.setWhere(where);
        queryBean.set(Constant.PARAM_GROUP, "USER_ID");
        queryBean.setOrder("SCORE DESC");
        queryBean.setShowNum(topN);
        queryBean.setServId(ServMgr.SY_COMM_INTEGRAL_SCORE);
        queryBean.setAct(ServMgr.ACT_FINDS);
        OutBean result = ServMgr.act(queryBean);
        List<Bean> list = result.getDataList();
        // replace user name
        for (Bean bean : list) {
            String userName = "default";
            try {
                UserBean user = UserMgr.getUser(bean.getStr("USER_ID"));
                userName = user.getName();
            } catch (Exception e) {
                userName = "无效的用户名";
            }
            bean.set("USER_NAME", userName);
        }
        return list;
    }
    
    /**
     * TODO: cache 统计<CODE>chnlId</CODE>分类下<CODE>date</CODE>至今为止积分增加最多的用户<br>
     * 统计数据会被系统缓存
     * @param servGroup - 服务ID
     * @param date - 起始时间
     * @param topN - 前几名
     * @param chnlId - 专家领域ID
     * @return 统计结果
     */
    public List<Bean> topStatisticsByChnl(String servGroup, Date date, int topN, String chnlId) {
        String dateStr = DateUtils.formatDate(date);
        ParamBean queryBean = new ParamBean();
        String where = " AND S_MTIME > '" + dateStr + "'";
        where += " AND SERV_ID='" + servGroup + "'  AND INTEGRAL_SCORE > 0 ";
        queryBean.setSelect("distinct(USER_ID), SUM(INTEGRAL_SCORE) as SCORE");
        queryBean.setQueryExtWhere(where);
//        queryBean.set(Constant.PARAM_GROUP, "USER_ID");
        queryBean.setGroupBy("USER_ID");
        queryBean.setOrder("SCORE DESC");
//        queryBean.setShowNum(topN);
        queryBean.setQueryPageShowNum(topN);
        queryBean.setQueryPageNowPage(1);
        queryBean.setServId(ServMgr.SY_COMM_ZHIDAO_SPEC_INTEGRAL);
        queryBean.setAct(ServMgr.ACT_QUERY);
        if (null != chnlId && 0 < chnlId.length()) {
            List<Bean> treeWhere = new ArrayList<Bean>();
            treeWhere.add(new Bean().set("DICT_ITEM", "CHNL_ID").set("DICT_VALUE", chnlId));
            queryBean.set("_treeWhere", treeWhere);
        }
        OutBean result = ServMgr.act(queryBean);
        List<Bean> list = result.getDataList();
        // replace user name
        for (Bean bean : list) {
            String userName = "default";
            try {
                UserBean user = UserMgr.getUser(bean.getStr("USER_ID"));
                userName = user.getName();
            } catch (Exception e) {
                userName = "无效的用户名";
            }
            bean.set("USER_NAME", userName);
        }
        return list;
    }

    /**
     * put channel bean in cache
     * @param cacheBean - channel bean
     */
    public void putInCache(Bean cacheBean) {
        String key = cacheBean.getId();
        CacheMgr.getInstance().set(key, cacheBean, CACHE_TYPE_INTEGRAL_STATISTICS, null, 60 * 60 * 12);
    }

    /**
     * get data from cache
     * @param key - channel id
     * @return - channel bean
     */
    public Bean getFromCache(String key) {
        Object obj = CacheMgr.getInstance().get(key, CACHE_TYPE_INTEGRAL_STATISTICS);
        if (null == obj) {
            return null;
        } else {
            return (Bean) obj;
        }
    }

    /**
     * delete channel bean from cache
     * @param key - channel id
     */
    public void deleteFromCache(String key) {
        CacheMgr.getInstance().remove(key, CACHE_TYPE_INTEGRAL_STATISTICS);
    }

    /**
     * clear all cache
     */
    public void clearCache() {
        CacheMgr.getInstance().clearCache(CACHE_TYPE_INTEGRAL_STATISTICS);
    }

    /**
     * 获取默认积分
     * @param userId - 用户Id
     * @param servId - 服务Id
     * @return 系统默认积分
     */
    private int getDefaultIntegralValue(String userId, String servId) {
        return 0;
    }

}
