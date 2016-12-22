package com.rh.wfeext;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.org.DeptBean;
import com.rh.core.org.UserBean;
import com.rh.core.org.mgr.OrgMgr;
import com.rh.core.org.mgr.UserMgr;
import com.rh.core.serv.ServDao;
import com.rh.core.wfe.WfAct;
import com.rh.core.wfe.def.WfNodeDef;
import com.rh.core.wfe.resource.ExtendBinder;
import com.rh.core.wfe.resource.ExtendBinderResult;

/**
 * 处理一个人负责多个部门的事务
 * 例如一个人是多个部门的部门负责人
 * 1、送本部门部门负责人
 * 2、送其他部门部门负责人
 * 3、部门负责人会签
 * 部门上设置了部门负责人，按照配置的送交，如果没有配置，则按照流程上配置的送交
 * @author ayf
 *
 */
public class SendDeptLeader  implements ExtendBinder {

    @Override
    public ExtendBinderResult run(WfAct currentWfAct, WfNodeDef nextNodeDef) {
        String deptCodes = nextNodeDef.getStr("NODE_DEPT_CODES");
        String roleCodes  =  nextNodeDef.getStr("NODE_ROLE_CODES");
        StringBuffer users = new StringBuffer("");
        UserBean userBean =  Context.getUserBean();
        if(deptCodes.length()>0){
            if(deptCodes.equals("s0")){//本级部门
                 users.append(Context.getUserBean().getTDeptBean().getSrcType1());
            }else{//其他部门下指定角色人
              //查询出所有部门上配置部门负责人的人员
                List<Bean> deptList = ServDao.finds("SY_ORG_DEPT", " and S_FLAG = '1' and DEPT_CODE in ('"+deptCodes.replace(",", "','")+"')and DEPT_SRC_TYPE1 is not NULL ");
                for(Bean b:deptList){
                    users.append(b.getStr("DEPT_SRC_TYPE1")).append(",");
                }
                //查询出该角色的人员
                List<Bean> usersInRoleAndDept = UserMgr.getUserListByDept(deptCodes, roleCodes);
                for(Bean a:usersInRoleAndDept){
                    users.append(a.getStr("DEPT_SRC_TYPE1")).append(",");
                }
            }
        }else{//没有配置部门
            //查询出所有部门上配置部门负责人的人员
            List<Bean> deptList = ServDao.finds("SY_ORG_DEPT", " and S_FLAG = '1' and DEPT_SRC_TYPE1 is not NULL ");
            for(Bean b:deptList){
                users.append(b.getStr("DEPT_SRC_TYPE1")).append(",");
            }
            //查询出该角色的人员
            List<Bean> usersInRole = UserMgr.getUserListByRole(roleCodes, "zhbx");
            for(Bean c:usersInRole){
                users.append(c.getStr("DEPT_SRC_TYPE1")).append(",");
            }
        }
        ExtendBinderResult result = new ExtendBinderResult();
        if(users.length() > 0){//去掉重复
            if(users.indexOf(",")>-1){
                users.delete(users.length()-1, users.length());
            }
            String[] userArray = users.toString().split(",");
            Set<String> dealUsers = new HashSet<String>();
            for(int i =0;i<userArray.length;i++){
                dealUsers.add(userArray[i]);
            }
            users = new StringBuffer("");
            Iterator<String> it = dealUsers.iterator();
            while(it.hasNext()){
                users.append(it.next()).append(",");
            } 
            result.setUserIDs(users.toString());
            result.setBindRole(false);
            result.setAutoSelect(false);
        }else{//读取流程上的配置，效果和流程上的一样
            StringBuffer depts = new StringBuffer();
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
