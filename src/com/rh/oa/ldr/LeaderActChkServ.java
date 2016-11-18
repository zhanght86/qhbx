package com.rh.oa.ldr;

import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServMgr;
import com.rh.core.util.Constant;

/**
 * 领导活动审核岗审核已送审的记录。领导活动审核岗为系统中的“领导活动审核”角色，编码为：OA_LDR_ACT_SH
 * @author yangjy
 *
 */
public class LeaderActChkServ extends CommonServ {
    /**
     * 领导活动审核通过
     * @param paramBean 参数Bean。包含送审核的记录ID
     * @return 处理结果outBean
     */
    public OutBean chkPass(ParamBean paramBean) {
        String strChkIds = paramBean.getStr("chkIds");
        String[] chkIds = strChkIds.split(",");
        for (String chkId : chkIds) {
            ParamBean bean = new ParamBean(LeaderActConstant.LDR_ACT, ServMgr.ACT_SAVE, chkId);
            bean.set("CHK_STATE", LeaderActConstant.CHK_STATE_PASS);
            bean.set("ACT_STATE", Constant.YES);
            ServMgr.act(bean);
        }
        return new OutBean().setOk(("有" + chkIds.length + "项审核通过。"));
    }

    /**
     * 领导活动审核不通过
     * @param paramBean 参数Bean。包含送审核的记录ID
     * @return 处理结果outBean
     */
    public OutBean chkNotPass(ParamBean paramBean) {
        String strChkIds = paramBean.getStr("chkIds");
        String[] chkIds = strChkIds.split(",");
        for (String chkId : chkIds) {
            ParamBean bean = new ParamBean(LeaderActConstant.LDR_ACT, ServMgr.ACT_SAVE, chkId);
            bean.set("CHK_STATE", LeaderActConstant.CHK_STATE_NOTPASS);
            ServMgr.act(bean);
        }
        return new OutBean().setOk("有" + chkIds.length + "项审核不通过。");
    }
}
