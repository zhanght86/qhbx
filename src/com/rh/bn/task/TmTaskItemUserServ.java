package com.rh.bn.task;

import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.org.UserBean;
import com.rh.core.org.mgr.UserMgr;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;

public class TmTaskItemUserServ extends CommonServ{
    /**
     * 根据用户ID修改部门名称
     * @param paramBean 查询条件
     * @param outBean 查询结果
     */
    public void afterQuery(ParamBean paramBean, OutBean outBean) {
        if (outBean.getCount() == 0) {
            return;
        }
        List<Bean> resultBeans = outBean.getDataList();
        for (int i = 0; i < resultBeans.size(); i++) {
            String userCode = resultBeans.get(i).getStr("ROLE_USER_CODE");
            String deptName = "";
            try {
                UserBean userInfo = UserMgr.getUser(userCode);
                deptName = userInfo.getDeptBean().getFullDeptNames();
            } catch (Exception e) {
                deptName = "(用户不存在)";
            }
            resultBeans.get(i).set("DEPT_NAMES", deptName);
        }
    }
}
