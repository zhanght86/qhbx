
    package com.rh.bn.task;

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
     * 任务分配群组维护字典
     * @author jiling
     */
    public class TaskReceiverDict implements DictItems{
        /**
         * 用户选择字典
         */
        private String userSelectDict = "SY_ORG_DEPT_USER_SUB";

        /** 是否显示群组选择方案 **/
        private boolean displaySendSchm = true;

        /**
         * 初始化参数
         * @param paramBean 参数Bean
         */
        private void initParam(ParamBean paramBean) {
            if (paramBean.isNotEmpty("userSelectDict")) {
                this.userSelectDict = paramBean.getStr("userSelectDict");
            }

            if (paramBean.isNotEmpty("displaySendSchm")) {
                displaySendSchm = paramBean.getBoolean("displaySendSchm");
            }
        }

        /**
         * @param paramBean 参数Bean
         * @return 反馈给前台的字典数据
         */
        public Bean getItems(ParamBean paramBean) {
            initParam(paramBean);

            Bean root = new Bean();
            appendDictInfo(root);

            List<Bean> child = new ArrayList<Bean>();
            root.set("CHILD", child);

            if (paramBean.isEmpty("PID")) {
                if (this.displaySendSchm) {
                    appendSendSchema(child);
                }
                appendDeptUser(child);
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
         * 增加本机构 和部门树
         * @param child 子节点列表
         */
        private void appendDeptUser(List<Bean> child) {
            Bean root = null;

            if (userSelectDict.equals("SY_ORG_DEPT_USER_SUB")) {
                UserBean user = Context.getUserBean();
                String pcode = user.getODeptBean().getPcode();
                if (StringUtils.isNotEmpty(pcode)) {
                    DeptBean odept = OrgMgr.getDept(pcode);
                    root = new Bean();
                    root.set("ID", odept.getId());
                    root.set("NAME", odept.getName());
                    root.set("CODE", odept.getId());
                    root.set("LEAF", "1");
                    root.set("isexpand", "false");
                }
            }

            List<Bean> list = DictMgr.getTreeList(userSelectDict, 2);
            if (root != null) {
                child.add(root);
                root.put("CHILD", list);
            } else {
                child.addAll(list);
            }
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

        /**
         * 
         * @param child 树子节点列表
         */
        private void appendSendSchema(List<Bean> child) {
            Bean schemaBean = new Bean();
            child.add(schemaBean);
            schemaBean.set("ID", "SendSchema");
            schemaBean.set("NAME", "选择发送机构");
            schemaBean.set("CODE", "SendSchema");
            schemaBean.set("LEAF", "2");
            schemaBean.set("isexpand", "false");

            List<Bean> childList = new ArrayList<Bean>();
            schemaBean.set("CHILD", childList);

            List<Bean> list = TaskGroupsMgr.queryTaskGroups();
            for (Bean bean : list) {
                Bean treeNode = new Bean();
                final String id = bean.getStr("GROUP_ID");
                treeNode.set("ID", id);
                treeNode.set("NAME", bean.getStr("GROUPS_NAME"));
                treeNode.set("CODE", id);
                schemaBean.set("LEAF", "1");
                childList.add(treeNode);
            }
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

        @Override
        public String getDictId(ParamBean paramBean) {
            return paramBean.getStr("userSelectDict");
        }

    }
