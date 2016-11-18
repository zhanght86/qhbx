package com.rh.bn.linktransfer.serv;

import com.rh.core.serv.CommonServ;
import com.rh.core.serv.ParamBean;
import com.rh.core.util.Constant;

/**
 * 百年人寿内网新闻扩展服务类
 * @author Tanyh 20151222
 *
 */
public class BnNewsServ extends CommonServ{

    @Override
    protected void beforeQuery(ParamBean paramBean) {
        StringBuffer where  = new StringBuffer("");
        //新闻栏目
        if (paramBean.isNotEmpty("TYPE_")) {
            where.append(" and TYPE_='" + paramBean.getStr("TYPE_") + "'");
        }
        //栏目所属机构
        if (paramBean.isNotEmpty("GROUPID")) {
            where.append(" and GROUPID=" + paramBean.getStr("GROUPID"));
        }
        if (paramBean.isNotEmpty(Constant.PARAM_WHERE)) { // 获取ParamBean里的_WHERE_
            where.append(paramBean.getStr(Constant.PARAM_WHERE));
        }
        paramBean.set(Constant.PARAM_WHERE, where);
    }
}
