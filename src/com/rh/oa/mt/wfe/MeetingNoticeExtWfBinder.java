package com.rh.oa.mt.wfe;

import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServMgr;
import com.rh.core.util.Constant;
import com.rh.core.wfe.WfAct;
import com.rh.core.wfe.def.WfNodeDef;
import com.rh.core.wfe.resource.ExtendBinder;
import com.rh.core.wfe.resource.ExtendBinderResult;

/**
 * @author wangchen
 */
public class MeetingNoticeExtWfBinder implements ExtendBinder {

    public ExtendBinderResult run(WfAct currentWfAct, WfNodeDef nextNodeDef) {
        Bean dataBean = currentWfAct.getProcess().getServInstBean();
        
        // 查询已发送通知用户列表
        ParamBean noticedQueryBean = new ParamBean("OA_MT_CONFEREE", ServMgr.ACT_FINDS);
        noticedQueryBean.setSelect("USER_CODE");
        noticedQueryBean.set("MEETING_ID", dataBean.getId());
        List<Bean> confereesList = ServMgr.act(noticedQueryBean).getDataList();
        
        // 与会人员数组
        String confereesPersons = dataBean.getStr("CONFEREES_CODES");
        // 被通知人数组
        String notifiedPersons = dataBean.getStr("NOTIFIED_CODES");
        if (confereesPersons.length() > 0 && notifiedPersons.length() > 0) {
            confereesPersons = confereesPersons + Constant.SEPARATOR;
        }
        String sendUserStr = confereesPersons + notifiedPersons;
        //需要被通知用过数组
        String[] sendUsers = sendUserStr.split(Constant.SEPARATOR);
        ExtendBinderResult result = new ExtendBinderResult();
        //都发送过则为空
        if (confereesList.size() == sendUsers.length) {
            sendUserStr = Context.getUserBean().getCode();
            result.setAutoSelect(false);
        } else {
            result.setAutoSelect(true);  
        }       
        result.setUserIDs(sendUserStr);
        return result;
    }
}
