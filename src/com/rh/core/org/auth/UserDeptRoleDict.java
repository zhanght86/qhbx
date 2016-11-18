package com.rh.core.org.auth;

import java.util.ArrayList;
import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServMgr;
import com.rh.core.serv.dict.DictItems;
import com.rh.core.serv.dict.DictMgr;

/**
 * 
 * @author yangjy
 * 
 */
public class UserDeptRoleDict implements DictItems {

    /**
     * 用户选择字典
     */
    private String userSelectDict = "SY_ORG_DEPT_USER_SUB";

    @Override
    public Bean getItems(ParamBean paramBean) {
        Bean root = new Bean();
        appendDictInfo(root);
        List<Bean> child = new ArrayList<Bean>();
        root.set("CHILD", child);

        if (paramBean.isEmpty("PID")) {
            appendRoles(child);
            appendDictDefaultItems(child);
        } else {
            final String pid = paramBean.getStr("PID");
            int level = paramBean.getInt("LEVEL");
            if (level == 0) {
                level = 1;
            }
            appendDeptUser(child, pid, level);
        }

        return root;
    }
    
    /**
     * 
     * @param child 增加字典默认数据项
     */
    private void appendDictDefaultItems(List<Bean> child) {
        List<Bean> list = DictMgr.getTreeList(userSelectDict, 2);
        child.addAll(list);
    }
    
    /**
     * 
     * @param child 树子节点列表
     */
    private void appendRoles(List<Bean> child) {
        Bean schemaBean = new Bean();
        child.add(schemaBean);
        schemaBean.set("ID", "Roles");
        schemaBean.set("NAME", "选择角色");
        schemaBean.set("CODE", "Roles");
        schemaBean.set("LEAF", "2");
        schemaBean.set("isexpand", "false");
        
        List<Bean> childList = DictMgr.getTreeList(ServMgr.SY_ORG_ROLE);
        schemaBean.set("CHILD", childList);
    }

    /**
     * 
     * @param child 树子节点列表
     * @param pid 父节点
     * @param level 加载级别
     */
    private void appendDeptUser(List<Bean> child, String pid, int level) {
        List<Bean> list = DictMgr.getTreeList(userSelectDict, pid, level);
        child.addAll(list);
    }

    @Override
    public String getDictId(ParamBean paramBean) {
        return userSelectDict;
    }

    /**
     * 增加字典基本信息
     * @param root 数据Bean
     */
    private void appendDictInfo(Bean root) {
        root.set("DICT_CHILD_ID", "ZhuanfaDict");
        root.set("DICT_DIS_LAYER", "0");
        root.set("DICT_DIS_ID", "ZhuanfaDict");
        root.set("DICT_NAME", "组织机构");
        root.set("DICT_TYPE", "2");
    }

}
