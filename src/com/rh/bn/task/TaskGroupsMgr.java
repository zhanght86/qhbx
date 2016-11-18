package com.rh.bn.task;

import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.org.UserBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServMgr;
import com.rh.core.serv.bean.SqlBean;
import com.rh.core.util.Constant;

public class TaskGroupsMgr {
    /**
     * 
     * @return 查询群组
     */
    public static List<Bean> queryTaskGroups() {
        UserBean userBean = Context.getUserBean();
        SqlBean sql = new SqlBean();
        sql.appendWhere(" AND (S_PUBLIC = 1 OR S_USER = ? ) ", userBean.getCode());
        sql.and("S_FLAG", Constant.YES_INT);
        sql.and("O_DEPT", userBean.getODeptCode());
        sql.orders("GROUP_ORDER, GROUPS_NAME");

        return ServDao.finds(ServMgr.BN_TM_TASK_COMMON_GROUPS, sql);
    }

}
