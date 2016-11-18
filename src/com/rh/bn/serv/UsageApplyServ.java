package com.rh.bn.serv;

import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;

public class UsageApplyServ extends CommonServ{
    protected void beforeSave(ParamBean paramBean) {
        if (paramBean.getId().length() == 0) { //如果缺省没有KEY，则为添加模式
            paramBean.set("circle_flag", true);
        }else if(paramBean.getAddFlag()) { //保存强制添加标志或者没有主键，则自动为添加模式
            paramBean.set("circle_flag", true); 
        }else{
            paramBean.set("circle_flag", false);
        }
    }
    /**
     * 保存之后的拦截方法，由子类重载
     * @param paramBean 参数信息
     *      可以通过paramBean获取数据库中的原始数据信息：
     *          Bean oldBean = paramBean.getOldData();
     *      可以通过方法getFullData(paramBean)获取数据库原始数据加上修改数据的完整的数据信息：
     *          Bean fullBean = getFullData(paramBean);
     *      可以通过paramBean.getBoolean(Constant.PARAM_ADD_FLAG)是否为true判断是否为添加模式
     * @param outBean 输出信息
     */
    protected void afterSave(ParamBean paramBean, OutBean outBean) {
        if(paramBean.getBoolean("circle_flag")){
            paramBean.set("SEAL_ID", "");
            paramBean.set("SERV_ID", paramBean.getServId());
            paramBean.set("DATA_ID", outBean.getId());
            paramBean.set("TITLE", paramBean.getStr("APPLY_TITLE"));
            paramBean.set("CIRCLE_STATUS", "2");
            BnUtils.inSealCircle(paramBean);
        }
    }
}
