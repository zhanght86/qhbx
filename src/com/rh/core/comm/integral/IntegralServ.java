/*
 * Copyright (c) 2013 Ruaho All rights reserved.
 */
package com.rh.core.comm.integral;

import java.util.Date;
import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.comm.wenku.WenkuServ;
import com.rh.core.comm.zhidao.ZhidaoServ;
import com.rh.core.org.UserBean;
import com.rh.core.org.mgr.UserMgr;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServMgr;
import com.rh.core.util.DateUtils;

/**
 * 积分服务
 * @author liwei
 * 
 */
public class IntegralServ extends CommonServ {
    
    
    /**
     * 获取文库积分排行
     * @param paramBean - 参数bean 
     * @return - outbean
     */
    public OutBean wenkuTopStatistics(ParamBean paramBean) {
        //paramBean.set("servId", ServMgr.SY_COMM_WENKU_DOCUMENT);
    	paramBean.set("servId", WenkuServ.SY_COMM_WENKU);
        return topStatistics(paramBean);
    }
    
    /**
     * 获取知道积分排行
     * @param paramBean - 参数bean
     * @return - outbean
     */
    public OutBean zhidaoTopStatistics(ParamBean paramBean) {
        paramBean.set("servId", ZhidaoServ.SY_COMM_ZHIDAO);
        return topStatistics(paramBean);
    }
    /**
     * 获取知道积分排行(根据chnlId)
     * @param paramBean - 参数bean
     * @return - outbean
     */
    public OutBean zhidaoTopStatisticsByChnl(ParamBean paramBean) {
        paramBean.set("servId", ZhidaoServ.SY_COMM_ZHIDAO);
        return topStatisticsByChnl(paramBean);
    }

    /**
     * 获取积分排行
     * @param paramBean - 参数bean
     * @return - outbean
     */
    public OutBean topStatistics(ParamBean paramBean) {
        String type = paramBean.getStr("type");
        String serv = paramBean.getStr("servId");
        int count = paramBean.getInt("count");

        if (0 == count) {
            // default
            count = 5;
        }
        // 统计
        List<Bean> list = null;
        if (type.equals("week")) {
            Date start = DateUtils.getWeekFirstDay(new Date());
            list = IntegralMgr.getInstance().topStatistics(serv, start, count);
        } else if (type.equals("total")) {
            list = IntegralMgr.getInstance().getTop(serv, count);
        } else if (type.equals("month")) {
            Date start = DateUtils.getMonthFirstDay(new Date());
            list = IntegralMgr.getInstance().topStatistics(serv, start, count);
        }
        // 添加用户头像
        for (Bean b : list) {
            try {
                UserBean user = UserMgr.getUser(b.getStr("USER_ID"));
                b.set("USER", user);
            } catch (Exception e) {
                b.set("USER", new Bean());
            }
        }
        
        return new OutBean().setData(list);
    }
    
    /**
     * 根据chnlId获取积分排行
     * @param paramBean - 参数bean
     * @return - outbean
     */
    public OutBean topStatisticsByChnl(ParamBean paramBean) {
        String type = paramBean.getStr("type");
        String serv = paramBean.getStr("servId");
        int count = paramBean.getInt("count");
        String chnlId = paramBean.getStr("chnlId");

        if (0 == count) {
            // default
            count = 5;
        }
        // 统计
        List<Bean> list = null;
        if (type.equals("week")) {
            Date start = DateUtils.getWeekFirstDay(new Date());
            list = IntegralMgr.getInstance().topStatisticsByChnl(serv, start, count, chnlId);
        } else if (type.equals("total")) {
            list = IntegralMgr.getInstance().getTopByChnl(serv, count, chnlId);
        } else if (type.equals("month")) {
            Date start = DateUtils.getMonthFirstDay(new Date());
            list = IntegralMgr.getInstance().topStatisticsByChnl(serv, start, count, chnlId);
        }
        // 添加用户头像
        for (Bean b : list) {
            try {
                UserBean user = UserMgr.getUser(b.getStr("USER_ID"));
                //获取在线状态
//                Bean userState = UserMgr.getUserState(b.getStr("USER_ID"));
                user.set("USER_IMG", UserMgr.getUser(b.getStr("USER_ID")).getImg());
                b.set("USER", user);
               // b.set("UserState", userState)
            } catch (Exception e) {
                b.set("USER", new Bean());
            }
        }
        
        return new OutBean().setData(list);
    }

}
