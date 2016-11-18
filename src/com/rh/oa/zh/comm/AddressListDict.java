package com.rh.oa.zh.comm;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.org.DeptBean;
import com.rh.core.org.UserBean;
import com.rh.core.org.mgr.OrgMgr;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.dict.DictItems;
import com.rh.core.serv.dict.DictMgr;

/**
 * 
 * @author yangjy
 * 
 */
public class AddressListDict implements DictItems {

    private static final String DICT_ID = "SY_ORG_DEPT_ALL";

    public Bean getItems(ParamBean paramBean) {
        Bean root = new Bean();

        List<Bean> child = new ArrayList<Bean>();
        root.set("CHILD", child);
        int level = paramBean.getInt("LEVEL");
        if (level == 0) {
            level = 1;
        }
        String pid = "";
        if (paramBean.isEmpty("PID")) {
            if (paramBean.isNotEmpty("FIR_PID")) {
                UserBean userBean = Context.getUserBean();
                pid = userBean.getParentODeptCode();
            } 

            if (StringUtils.isEmpty(pid)) {
                UserBean userBean = Context.getUserBean();
                pid = userBean.getODeptCode();
            }
            appendRootInfo(root, pid);
        } else {
            pid = paramBean.getStr("PID");
        }
        appendDeptUser(child, pid, level);

        return root;
    }

    /**
     * 
     * @param child 树子节点列表
     * @param pid 父节点
     * @param level 加载级别
     */
    private void appendDeptUser(List<Bean> child, String pid, int level) {
        List<Bean> list = DictMgr.getTreeList(DICT_ID, pid, level);
        child.addAll(list);
    }

    /**
     * 增加字典基本信息
     * @param root 数据Bean
     * @param pid 父节点id
     */
    private void appendRootInfo(Bean root, String pid) {
        DeptBean deptBean = OrgMgr.getDept(pid);
        String deptName = "";
        if (deptBean == null) {
            deptName = "组织机构";
        } else {
            deptName = deptBean.getName();
        }
        root.set("DICT_CHILD_ID", "");
        root.set("DICT_ID", DICT_ID);
        root.set("DICT_NAME", deptName);
        root.set("DICT_TYPE", "2");
        root.set("LEAF", "2");
        root.set("OTYPE", "2");
        root.set("PID", "");
    }

    public String getDictId(ParamBean paramBean) {
        return DICT_ID;
    }
}
