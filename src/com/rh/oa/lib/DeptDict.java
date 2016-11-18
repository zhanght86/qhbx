package com.rh.oa.lib;

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
public class DeptDict implements DictItems {
    /** 默认字典ID **/
    private static final String DICT_ID = "SY_ORG_DEPT_SUB";

    /** 指定机构级别 **/
    private static final String PODEPT_LEVEL = "PODEPT_LEVEL";
    
    /** 超过4级机构，显示四级机构的文库 **/
    private static final int MAX_LEVEL = 4;

    
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
            pid = getDefaultPID(paramBean);
            appendRootInfo(root, pid);
        } else {
            pid = paramBean.getStr("PID");
        }
        appendDept(child, pid, level);

        return root;
    }

    /**
     * 取得默认机构ID
     * @param paramBean 参数Bean
     * @return 默认机构ID
     */
    private String getDefaultPID(ParamBean paramBean) {
        String pid = "";
        if (paramBean.isNotEmpty(PODEPT_LEVEL)) {  
            // 指定了父机构的级别，则取与级别对应的上级机构，如财险总公司，分公司
            int deptLevel = paramBean.getInt(PODEPT_LEVEL);
            UserBean userBean = Context.getUserBean();
            int currentLevel = userBean.getODeptLevel();
            DeptBean odeptBean = userBean.getODeptBean();
            while (deptLevel < currentLevel) {
                currentLevel--;
                if (deptLevel == currentLevel) {
                    pid = odeptBean.getPcode();
                    break;
                } else {
                    //odeptBean = odeptBean.getODeptBean();
                    odeptBean = OrgMgr.getOdept(odeptBean.getPcode());
                }
            }
        }

        if (StringUtils.isEmpty(pid)) {
            UserBean userBean = Context.getUserBean();
            int currentLevel = userBean.getODeptLevel();
            if (currentLevel <= MAX_LEVEL) {
                pid = userBean.getODeptCode();
            } else {
                //超过指定机构以下的机构，只能看指定机构的文件。县支只能看地市的文件
                DeptBean odeptBean = userBean.getODeptBean();
                while (currentLevel > MAX_LEVEL) { 
                    currentLevel--;
                    if (currentLevel == MAX_LEVEL) {
                        pid = odeptBean.getPcode();
                        break;
                    } else {
                        odeptBean = OrgMgr.getOdept(odeptBean.getPcode());
                    }
                }                
            }
        }
        return pid;
    }

    /**
     * 
     * @param child 树子节点列表
     * @param pid 父节点
     * @param level 加载级别
     */
    private void appendDept(List<Bean> child, String pid, int level) {
        List<Bean> list = DictMgr.getTreeList(DICT_ID, pid, level);
        
        UserBean userBean = Context.getUserBean();
        int currentLevel = userBean.getODeptLevel();
        DeptBean odeptBean = OrgMgr.getDept(pid);
        boolean addSubOrg = false;
        //如果当前部门的所属机构等于当前用户所在机构，则显示子公司
        if (odeptBean.getODeptBean().getLevel() == currentLevel) {
            addSubOrg = true;
        }

        for (Bean bean : list) {
            if (addSubOrg || bean.getInt("DEPT_TYPE") == 1) { // 去掉子公司，只保留一级
                child.add(bean);
            }
        }
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
        root.set("DICT_ID", pid);
        root.set("DICT_NAME", deptName);
        root.set("DICT_TYPE", "2");
        root.set("LEAF", "2");
        root.set("OTYPE", "2");
        root.set("ID", pid);
        root.set("PID", "");
    }

    
    public String getDictId(ParamBean paramBean) {
        return DICT_ID;
    }
}
