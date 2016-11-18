package com.rh.core.comm;

import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.org.mgr.UserMgr;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.ServDao;

/**
 * 隶属服务
 * 
 * @author chujie
 * 
 */
public class BelongServ extends CommonServ {

    /**
     * 根据用户编码查找下属
     * 
     * @param paramBean  参数
     * @return String
     */
    public Bean getBelongUserInStr(Bean paramBean) {
        Bean resBean = new Bean();
        String userCode = paramBean.getStr("userCode").length() > 0 ? paramBean
                .getStr("userCode") : Context.getUserBean().getCode();
        List<Bean> dataList = ServDao.finds("SY_COMM_BELONG", " and TO_USER='"
                + userCode + "'" + " and S_FLAG=1");
        StringBuffer users = new StringBuffer();
        if (dataList.size() > 0) {
            for (int i = 0; i < dataList.size(); i++) {
                if (i == dataList.size() - 1) {
                    users.append(dataList.get(i).getStr("S_USER"));
                } else {
                    users.append(dataList.get(i).getStr("S_USER") + ",");
                }
            }
        }
        return resBean.set("belongUserStr", users.toString());
    }

    /**
     * 根据用户编码查找下属
     * 
     * @param paramBean
     *            参数paramBean
     * @return Bean
     */
    public Bean getBelongUsers(Bean paramBean) {
        String userCode = Context.getUserBean().getCode();
        if (userCode.length() > 0) {
            List<Bean> dataList = ServDao.finds("SY_COMM_BELONG",
                    " and TO_USER='" + userCode + "'" + " and S_FLAG=1");
            if (dataList.size() > 0) {
                for (Bean belongBean : dataList) {
                    belongBean.set("S_USER__NAME", UserMgr.getUser(
                            belongBean.getStr("S_USER")).getName());
                }
            }
            Bean rtnBean = new Bean();
            rtnBean.set("_DATA_", dataList);
            return rtnBean;
        } else {
            return new Bean();
        }
    }
}
