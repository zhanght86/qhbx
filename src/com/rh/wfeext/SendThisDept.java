package com.rh.wfeext;

import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.org.DeptBean;
import com.rh.core.org.UserBean;
import com.rh.core.org.mgr.OrgMgr;
import com.rh.core.serv.ServDao;
import com.rh.core.wfe.WfAct;
import com.rh.core.wfe.def.WfNodeDef;
import com.rh.core.wfe.resource.ExtendBinder;
import com.rh.core.wfe.resource.ExtendBinderResult;

/**
 * 处理一个人负责多个部门时，送交本部门时的特殊处理
 * 需要送交到负责部门的人员
 * @author ruaho
 *
 */
public class SendThisDept  implements ExtendBinder {

    @Override
    public ExtendBinderResult run(WfAct currentWfAct, WfNodeDef nextNodeDef) {
        String deptCodes = nextNodeDef.getStr("NODE_DEPT_CODES");
        String roleCodes  =  nextNodeDef.getStr("NODE_ROLE_CODES");
        StringBuffer depts = new StringBuffer("");
        UserBean userBean =  Context.getUserBean();
        if(deptCodes.length()>0 ){
            if(deptCodes.equals("s0")){//本级部门
                List<Bean> deptList = ServDao.finds("SY_ORG_DEPT", " and S_FLAG='1' and DEPT_SRC_TYPE1 like '%"+userBean.getId()+"%'");
                for(Bean b:deptList){
                    depts.append(b.getStr("DEPT_CODE")).append(",");
                }
            }
        }
        ExtendBinderResult result = new ExtendBinderResult();
        if(depts.length() > 0){
            if(depts.indexOf(",")>-1){
            depts.deleteCharAt(depts.length()-1);
            }
            result.setDeptIDs(depts.toString());
            result.setBindRole(false);
            result.setAutoSelect(false);
        }else{
            if(deptCodes.equals("s0")){
                List<DeptBean> deptList = OrgMgr.getChildDepts(userBean.getCmpyCode(), userBean.getTDeptCode());
                depts.append(userBean.getTDeptCode());
                for(Bean d:deptList){
                    depts.append(",").append(d.getStr("DEPT_CODE"));
                }
            }
            result.setDeptIDs(depts.toString());
            result.setUserIDs(nextNodeDef.getStr("NODE_USER_CODES"));
            result.setRoleCodes(nextNodeDef.getStr("NODE_ROLE_CODES"));
            result.setBindRole(false);
            result.setAutoSelect(false);
        }
        return result;
    }
    
}
