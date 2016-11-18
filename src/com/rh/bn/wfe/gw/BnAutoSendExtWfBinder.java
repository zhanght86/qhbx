package com.rh.bn.wfe.gw;

import com.rh.core.base.Bean;
import com.rh.core.base.TipException;
import com.rh.core.util.JsonUtils;
import com.rh.core.wfe.WfAct;
import com.rh.core.wfe.def.WfNodeDef;
import com.rh.core.wfe.resource.ExtendBinder;
import com.rh.core.wfe.resource.ExtendBinderResult;

/**
 * 百年人寿项目根据页面数据项设定的审批人员，依次送交审核
 * @author Tanyh 20151021
 *
 */
public class BnAutoSendExtWfBinder implements ExtendBinder{


    public ExtendBinderResult run(WfAct currentWfAct, WfNodeDef nextNodeDef) {
        //获取审批单数据
        Bean dataBean = currentWfAct.getProcess().getServInstBean();
        //获取处理人扩展配置
        String extCls = nextNodeDef.getStr("NODE_EXTEND_CLASS");
        String configStr = ""; //工作流扩展配置
        String[] classes = extCls.split(",,");
        if (classes.length == 2) {
            configStr = classes[1];
        }
        Bean configBean = JsonUtils.toBean(configStr);
        //获取审批人字段编码
        if(configBean == null || configBean.isEmpty() || configBean.getStr("APPROVE_COLUMN").length() <= 0) {
            throw new TipException("未配置审批人字段，请检查工作流设置");
        }
        String approveColumn = configBean.getStr("APPROVE_COLUMN");
        String approveUsers = dataBean.getStr(approveColumn);
        if (approveUsers.length() <= 0) {
            throw new TipException("未选择审批人");
        }
        String draftUser = dataBean.getStr("S_USER");
        ExtendBinderResult result = new ExtendBinderResult();
        String[] approvers = approveUsers.split(",");
        String nextUser = "";
        //boolean canEnd = false;//是否可以办结，只有起草人可办结
        //当前用户为起草人，则取第一个审批人
        if (currentWfAct.getNodeInstBean().getStr("TO_USER_ID").equals(draftUser)) {
            nextUser = approvers[0];
        } else {
            //根据当前人在审批人列表中的位置，获取下一个审批人
            for (int i = 0; i < approvers.length - 1; i ++) {
                if (currentWfAct.getNodeInstBean().getStr("TO_USER_ID").equals(approvers[i])) {
                    nextUser = approvers[i + 1];
                    break;
                }
            }
            //当前人是最后一个审批人，则送交给起草人
            if (nextUser.length() <= 0) {
                nextUser = draftUser;
            }
        }
        /**
         * 以下代码存在线程安全问题，将被注视，仅允许起草人在该节点办结流程的功能移到CardJS中。cuihaifeng 2016-04-25
         */
        //起草人可办结，则设置办结按钮
        /**
        if (nextUser.equals(draftUser)) {
            nextNodeDef.set("PROC_END_FLAG", "1");
            nextNodeDef.set("PROC_END_NAME", "办结");
        } else {
            nextNodeDef.set("PROC_END_FLAG", "2");
            nextNodeDef.set("PROC_END_NAME", "");
        }
        */
        result.setDeptIDs("");
        result.setRoleCodes("");
        result.setUserIDs(nextUser);
        result.setBindRole(false);
        return result;
    }

}
