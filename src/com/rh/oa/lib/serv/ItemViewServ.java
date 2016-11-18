package com.rh.oa.lib.serv;

import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.org.DeptBean;
import com.rh.core.org.UserBean;
import com.rh.core.org.mgr.OrgMgr;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.ParamBean;

/**
 * 文库查询
 * @author yangjy
 * 
 */
public class ItemViewServ extends CommonServ {

    @Override
    protected void beforeQuery(ParamBean paramBean) {
        super.beforeQuery(paramBean);
        // 浏览范围设置
        UserBean user = Context.getUserBean();

        int level = user.getODeptLevel();
        if (level >= 1) {
            level = level - 1;
        }

        // 增加按照浏览范围过滤数据的代码
        StringBuilder where = new StringBuilder();
        where.append(" and bitand(FILE_SCOPE ,");
        where.append((int) Math.pow(2, level));
        where.append(") > 0");
        if (paramBean.getQueryExtWhere().length() > 0) {
            where.insert(0, paramBean.getQueryExtWhere());
        }

        // 指定只能查询一个机构内的数据
        if (paramBean.isNotEmpty("_treeWhere")) {
            List<Bean> list = paramBean.getList("_treeWhere");
            Bean bean = list.get(0);
            String deptCode = bean.getStr("DICT_VALUE");
            DeptBean dept = OrgMgr.getDept(deptCode);
            if (dept.getType() == 2) { // 如果指定部门为机构，则指定查询范围为本机构
                where.append(" and S_ODEPT = '").append(deptCode).append("'");
                paramBean.remove("_treeWhere");
            }
        }

        paramBean.setQueryExtWhere(where.toString());
    }

}
