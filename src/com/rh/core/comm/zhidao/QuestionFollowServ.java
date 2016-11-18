package com.rh.core.comm.zhidao;

import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServMgr;

/**
 * 获取用户头像
 * zhangjinxi
 */
public class QuestionFollowServ extends CommonServ {

    /**
     * @param paramBean - 参数bean
     * @return outBean - 输出bean
     */
    public OutBean getFollowList(ParamBean paramBean) {
        ParamBean queryBean = new ParamBean(ServMgr.SY_COMM_ZHIDAO_QUESTION_FOLLOW, ServMgr.ACT_QUERY);
        queryBean.setQuerySearchWhere("and DATA_ID='" + paramBean.get("DATA_ID") + "'");
        OutBean out = ServMgr.act(queryBean);
        return out;
    }
}
