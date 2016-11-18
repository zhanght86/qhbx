package com.rh.oa.cd;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.comm.ConfMgr;
import com.rh.core.org.mgr.UserMgr;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.util.Constant;
import com.rh.core.util.DateUtils;
import com.rh.oa.cd.util.CdUtils;

/**
 * 催办的后台线程，处理自动起草催办单
 * 
 * @author cuihf
 * 
 */
public class RemdJob extends AbstractCdJob {

    /**
     * 获取催督办类型
     * @return 催办类型
     */
    protected int getAutoType() {
        return CdUtils.TYPE_AUTO_REMD;
    }

    /**
     * 创建催办单
     * @param autoBean 自动催办或督办的配置信息
     * @param dataBean 相关信息
     */
    protected void create(Bean autoBean, Bean dataBean) {
        /**
         * 目前仅处理公文的催办；如需支持其他服务的催办单或督办单，需增加相应的接口，由各自服务实现，告知该程序催办的数据项
         */
        RemdServ remdServ = new RemdServ();
        ParamBean paramBean = new ParamBean();
        paramBean.set(Constant.PARAM_SERV_ID, "OA_CD_REMIND");
        paramBean.set("ACPT_USER", autoBean.getStr("ACPT_USER"));
        paramBean.set("ACPT_DEPT", autoBean.getStr("ACPT_DEPT"));
        paramBean.set("ACPT_TDEPT", autoBean.getStr("ACPT_TDEPT"));
        paramBean.set("ACPT_PHONE", "");
        if (autoBean.getStr("AUTO_TITLE").length() > 0) {
            paramBean.set("REMD_TITLE", autoBean.getStr("AUTO_TITLE"));
        } else {
            paramBean.set("REMD_TITLE", ConfMgr.getConf("OA_CD_TITLE_REMD", "") + dataBean.getStr("GW_TITLE"));
        }
        paramBean.set("REMD_REASON", dataBean.getStr("GW_TITLE"));
        paramBean.set("REMD_CODE", ConfMgr.getConf("OA_CD_CODE_REMD", ""));
        paramBean.set("REMD_YEAR", DateUtils.getYear());
        paramBean.set("REMD_NUM", remdServ.getMaxCode(paramBean).getStr("REMD_NUM"));
        paramBean.set("DEADLINE", autoBean.getStr("AUTO_DATE"));
        paramBean.set("OVER_DAYS", DateUtils.selectDateDiff(DateUtils.getDate(), autoBean.getStr("AUTO_DATE")));
        if (autoBean.getStr("DRAFE_USER").length() > 0) {
            Context.setThreadUser(UserMgr.getUserState(autoBean.getStr("DRAFE_USER")));
            paramBean.set("S_USER", autoBean.getStr("DRAFE_USER"));
        } else {
            Context.setThreadUser(UserMgr.getUserState(dataBean.getStr("S_USER")));
            paramBean.set("S_USER", dataBean.getStr("S_USER"));
        }
        paramBean.set("S_ATIME", DateUtils.getDate());

        paramBean.set("REMD_STATUS", 0);
        OutBean outBean = remdServ.save(paramBean);
        remdServ.sendTodo(new ParamBean(outBean));
        //System.out.println("自动创建催办单");
    }

    
    public void interrupt() {
        // TODO Auto-generated method stub
        
    }

}
