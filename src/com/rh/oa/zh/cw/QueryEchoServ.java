package com.rh.oa.zh.cw;

import org.apache.commons.lang.StringUtils;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.org.UserBean;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.bean.SqlBean;

/**
 * 获取财务关联OA签报回显记录
 * @author ruaho_hdy
 *
 */
public class QueryEchoServ extends CommonServ {

    /** 费控关联oa签报 回显服务 */
    private static final String OA_FOR_CW_QUERY_ECHO = "OA_FOR_CW_QUERY_ECHO";

    /**
     * 
     * orderreload QueryEchoSer 关联OA审批单查询 中当关联后回显关联记录
     * @author:dengzhonglin 2014-10-21 OutBean
     * @param orderNum 费控单据号
     * @return out 关联oa的单据
     */
    public OutBean orderreload(ParamBean orderNum) {
        OutBean out = new OutBean();
        if (StringUtils.isNotBlank(orderNum.get("ORDER_NUM").toString())) {
            SqlBean sql = new SqlBean();
            UserBean user = Context.getUserBean();
            sql.and("USER_CODE", user.getCode()).and("USER_CODE", user.getCode());
            sql.and("USER_WORK_NUM", user.getStr("USER_WORK_NUM"));
            sql.and("ORDER_NUM", orderNum.get("ORDER_NUM"));
            sql.selects("DATA_IDS");
            Bean orders = ServDao.find(OA_FOR_CW_QUERY_ECHO, sql);
            out.set("DATA_IDS_OBJ", orders);
        }
        return out;
    }
}
