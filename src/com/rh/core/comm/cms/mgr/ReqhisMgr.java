package com.rh.core.comm.cms.mgr;


import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.org.UserBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServMgr;
import com.rh.core.util.Constant;
import com.rh.core.util.RequestUtils;

/**
 * request history manager
 * @author liwei
 * 
 */
public class ReqhisMgr {

    /**
     * 计算请求的人数，跟请求数不一样
     * @param servId - 服务
     * @param dataId - 数据项
     * @param action - 操作
     * @return 这个服务的此数据项的这种操作的总人数
     */
    public static int countUser(String servId, String dataId, String action) {
        int count = 0;
        Bean queryBean = new Bean();
        queryBean.set("ACT_CODE", action);
        queryBean.set("SERV_ID", servId);
        queryBean.set("DATA_ID", dataId);
        queryBean.set(Constant.PARAM_SELECT, "count(distinct user_code)");
        List<Bean> list = ServDao.finds(ServMgr.SY_COMM_REQ_HIS, queryBean);
        //List<Bean> list = ServDao.finds(ServMgr.SY_COMM_REQ_HIS, queryBean);
        count = list.get(0).getInt("COUNT(DISTINCTUSER_CODE)");
        return count;
    }
    
    /**
     * 计算请求的次数，跟请求人数不一样
     * @param servId - 服务
     * @param dataId - 数据项
     * @param action - 操作
     * @return 这个服务的此项数据项的这种操作的总操作数
     */
    public static int countReq(String servId, String dataId, String action) {
        int count = 0;
        Bean queryBean = new Bean();
        queryBean.set("ACT_CODE", action);
        queryBean.set("SERV_ID", servId);
        queryBean.set("DATA_ID", dataId);
        queryBean.set(Constant.PARAM_SELECT, "sum (COUNTER)");
        List<Bean> list = ServDao.finds(ServMgr.SY_COMM_REQ_HIS, queryBean);
        count = list.get(0).getInt("SUM(COUNTER)");
        return count;
    }
    
    /**
     * 取得最新的一条数据
     * @param servId - 服务
     * @param dataId - 数据项
     * @param action - 操作
     * @return 这个服务的此项数据项的这种操作的最新/最后一次操作的信息
     */
    public static Bean newReq(String servId, String dataId, String action) {
        ParamBean queryBean = new ParamBean();
        String where = " and ACT_CODE = '" + action + "' and SERV_ID = '" + servId + "' and DATA_ID = '" + dataId + "'";
        queryBean.setServId(ServMgr.SY_COMM_REQ_HIS);
        queryBean.setQueryExtWhere(where);
        queryBean.setQueryPageNowPage(1);
        queryBean.setQueryPageShowNum(1);
        queryBean.setOrder("S_MTIME DESC");
        queryBean.setAct(ServMgr.ACT_QUERY);
        List<Bean> beanList = ServMgr.act(queryBean).getDataList();
        Bean bean = new Bean();
        if (beanList.size() > 0) {
            bean = beanList.get(0);
        }
        return bean;
    }

    /**
     * 保存请求历史
     * @param servId - 服务
     * @param dataId - 数据项
     * @param action - 操作
     * @param dataDisName - 数据项显示名称
     * @param dataOwner - 数据项作者(目标数据的S_USER)
     */
    public static void save(String servId, String dataId, String action, String dataDisName, String dataOwner) {
        // 更新请求历史
        String ip = RequestUtils.getIpAddr(Context.getRequest());
        UserBean user = Context.getUserBean();
        ParamBean queryBean = new ParamBean();
        queryBean.set("USER_CODE", user.getCode());
        queryBean.set("ACT_CODE", action);
        queryBean.set("SERV_ID", servId);
        queryBean.set("DATA_ID", dataId);

        Bean currentBean = ServDao.find(ServMgr.SY_COMM_REQ_HIS, queryBean);
        if (null == currentBean) {
            currentBean = new Bean();
            currentBean.set("SERV_ID", servId);
            currentBean.set("DATA_ID", dataId);
            currentBean.set("USER_CODE", user.getCode());
            currentBean.set("ACT_CODE", action);
        }
        currentBean.set("USER_IP", ip);
        currentBean.set("DATA_DIS_NAME", dataDisName);
        if (null != dataOwner && 0 < dataOwner.length()) {
            currentBean.set("DATA_OWNER", dataOwner);
        }
        int count = currentBean.get("COUNTER", 0);
        currentBean.set("COUNTER", count + 1);
        ServDao.save(ServMgr.SY_COMM_REQ_HIS, currentBean);
    }

}
