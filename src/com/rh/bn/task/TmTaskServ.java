package com.rh.bn.task;

import java.util.ArrayList;
import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.base.TipException;
import com.rh.core.comm.todo.TodoBean;
import com.rh.core.comm.todo.TodoUtils;
import com.rh.core.org.UserBean;
import com.rh.core.org.mgr.UserMgr;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.bean.SqlBean;
import com.rh.core.serv.dict.DictMgr;

import com.rh.core.util.Constant;
import com.rh.core.util.DateUtils;

/**
 * 任务分配服务
 * @author jiling
 *
 */
public class TmTaskServ extends CommonServ {

    /** 任务反馈服务ID **/
    public static final String BN_TM_ASSIGN = "BN_TM_ASSIGN";
    /** 任务分配服务ID **/
    public static final String BN_TM_TASK = "BN_TM_TASK";
    /** 任务状态 **/
    public static final String TASK_STATE = "TASK_STATE";
    /** 任务初始状态 **/
    public static final String TASK_STATE_START = "1";
    /** 任务终止状态 **/
    public static final String TASK_STATE_END = "7";
    /** 任务结束状态 **/
    public static final String TASK_STATE_SURECOMPLETE = "6";

    /** 任务ID **/
    public static final String TASK_ID = "TASK_ID";
    /** 任务群组方案明细服务ID **/
    public static final String TASK_COMMON_GROUPS_ITEM_SERV = "TASK_COMMON_GROUPS_ITEM";

    @Override
    protected void beforeQuery(ParamBean paramBean) {
        super.beforeQuery(paramBean);
        //获取当前用户
        UserBean userBean = Context.getUserBean();
        if (userBean != null) {
            StringBuffer queryStr = new StringBuffer("");
            //按部门查找所属常用组
            queryStr.append(" and ((ITEM_TYPE='DEPT' AND ROLE_USER_CODE in ('" + userBean.getDeptCode() + "','" + userBean.getTDeptCode() + "'))");
            //按角色查找所属常用组
            if (userBean.getRoleCodeStr().length() > 0) {
                queryStr.append(" or (ITEM_TYPE='ROLE' AND ROLE_USER_CODE in ('" + userBean.getRoleCodeStr().replaceAll(",", "','") + "'))");
            }
            //按用户code查找所属常用组
            queryStr.append(" or (ITEM_TYPE='USER' AND ROLE_USER_CODE='" + userBean.getCode() + "'))");
            //获取常用组
            List<Bean> groupBeanList = ServDao.finds(TASK_COMMON_GROUPS_ITEM_SERV, queryStr.toString());
            //根据任务起草人、任务接收人拼写查询条件
            queryStr = new StringBuffer(" and (S_USER='" + userBean.getCode() + "' or instr(','||TASK_RECEIVER||',','," + userBean.getCode() + ",')>0 ");
            //拼写常用组串
            for (Bean groupBean : groupBeanList) {
                //判断是否重复
                if (queryStr.indexOf("'," + groupBean.getStr("GROUP_ID") + ",'") >= 0) {
                    continue;
                }
                queryStr.append(" or instr(','||TASK_RECEIVER||',','," + groupBean.getStr("GROUP_ID") + ",')>0 ");
            }
            queryStr.append(")");
            //设置查询条件
            paramBean.setWhere(queryStr.toString());
        } else {
            throw new TipException("用户未登录，请重新登录!");
        }
    }
    
    
    
    
    
    
    
    
    
    public Bean taskAssignStart(ParamBean paramBean) {

        // 获取数据字典（任务状态）字典项编码为"1"的字典项的名称
        Bean dictBean = DictMgr.getItem("BN_QS_TASK_STATE", TASK_STATE_START);
        String dictItemCode = dictBean.getStr("ITEM_CODE");
        // 当点击任务分配按钮时，主单任务状态就变为初始状态--已分配
        SqlBean taskBean = new SqlBean();
        taskBean.setId(paramBean.getStr("TASK_ID"));
        taskBean.set("TASK_STATE", dictBean.getStr("ITEM_CODE"));
        ServDao.update(BN_TM_TASK, taskBean);

        SqlBean assignSqlBean = new SqlBean();
        assignSqlBean.and("TASK_ID", paramBean.getStr("TASK_ID"));
        // 根据任务分配对象，创建相应任务反馈单
        Bean oldBean = paramBean.getSaveOldData();
        UserBean userBean = Context.getUserBean();
        // 获取任务分配对象
        String TASK_RECEIVER = paramBean.getStr("TASK_RECEIVER");
        if (!"".equals(TASK_RECEIVER)) {
            String[] results = TASK_RECEIVER.split(",");
            if (results != null && results.length > 0) {
                for (String str : results) {
                    UserBean usersBean = null ;
                    try{
                        usersBean= UserMgr.getUser(str);
                    }catch(Exception e){}
                    
                    if (usersBean != null) {
                        inserUsers(str,paramBean);
                    } else {
                        SqlBean groupSql = new SqlBean();
                        groupSql.and("GROUP_ID", str);
                        List<Bean> groupBeanList = ServDao.finds(TASK_COMMON_GROUPS_ITEM_SERV, groupSql);
                        for(int j = 0 ; j < groupBeanList.size(); j++){
                           Bean groupBean = groupBeanList.get(j);
                           String groupType = groupBean.getStr("ITEM_TYPE");
                           if(groupType.equals("ROLE")){
                              List<UserBean> userBeanRoleList = UserMgr.getUsersByRole(groupBean.getStr("ROLE_USER_CODE"));
                              for(int k = 0; k < userBeanRoleList.size(); k++){
                                  Bean userBeanRole = userBeanRoleList.get(k);
                                  inserUsers(userBeanRole.getId(),paramBean);
                              }
                           }
                           if(groupType.equals("DEPT")){
                               List<UserBean> userBeanDeptList = UserMgr.getUsersByDept(groupBean.getStr("ROLE_USER_CODE"));
                               for(int o = 0 ; o < userBeanDeptList.size(); o++){
                                   UserBean userBeanDept = userBeanDeptList.get(o);
                                   
                                   inserUsers(userBeanDept.getId(),paramBean);
                               }
                           }
                           if(groupType.equals("USER")){
                               String userID = groupBean.getStr("ROLE_USER_CODE");
                               inserUsers(userID,paramBean);
                           }
                        }
                    }
                }
            }
        }
        return new Bean().set(Constant.RTN_MSG, Constant.RTN_MSG_OK);
    }

    public Bean taskAssignEnd(ParamBean paramBean) {
        // 当点击终止任务的按钮则主单卡片和子单列表的状态均变为任务终止
        SqlBean taskBean = new SqlBean();
        taskBean.setId(paramBean.getStr("TASK_ID"));
        taskBean.set("TASK_STATE", TASK_STATE_END);
        ServDao.update(BN_TM_TASK, taskBean);

        SqlBean taskBean2 = new SqlBean();
        taskBean2.and("TASK_ID", paramBean.getStr("TASK_ID"));

        Bean dateBean = new Bean();
        dateBean.set("TASK_STATE", TASK_STATE_END);
        ServDao.updates(BN_TM_ASSIGN, dateBean, taskBean2);
        return new Bean().set(Constant.RTN_MSG, Constant.RTN_MSG_OK);
    }

    public Bean sureComplete(ParamBean paramBean) {
        // 当点击确认完成的按钮则主单卡片和子单列表的状态均变为已完成
        SqlBean taskBean = new SqlBean();
        taskBean.setId(paramBean.getStr("TASK_ID"));
        taskBean.set("TASK_STATE", TASK_STATE_SURECOMPLETE);
        ServDao.update(BN_TM_TASK, taskBean);

        SqlBean taskBean2 = new SqlBean();
        taskBean2.and("TASK_ID", paramBean.getStr("TASK_ID"));

        Bean dateBean = new Bean();
        dateBean.set("TASK_STATE", TASK_STATE_SURECOMPLETE);
        ServDao.updates(BN_TM_ASSIGN, dateBean, taskBean2);
        return new Bean().set(Constant.RTN_MSG, Constant.RTN_MSG_OK);
    }

    public void inserUsers(String str , ParamBean paramBean){
        SqlBean sqlBean = new SqlBean();
        sqlBean.and("TASK_ID", paramBean.getStr("TASK_ID"));
        sqlBean.and("TASK_USER", str);
        Bean assignBean = ServDao.find(BN_TM_ASSIGN, sqlBean);
        if (assignBean == null) {
            Bean linkData = new Bean();
            linkData.set("TASK_ID", paramBean.getStr("TASK_ID"));
            linkData.set("TASK_USER", str);
            // 当点击任务分配按钮时子单的状态为已分配
            linkData.set("TASK_STATE", TASK_STATE_START);
            ServDao.create(BN_TM_ASSIGN, linkData);
            // 送待办
            TodoBean todoBean = new TodoBean();
            todoBean.setSender(str);
            todoBean.setTitle(paramBean.getStr("TASK_NAME"));
            todoBean.setOwner(str);
            todoBean.setCode(BN_TM_TASK);
            todoBean.setObjectId1(paramBean.getStr("TASK_ID"));
            todoBean.setUrl(BN_TM_TASK + ".byid.do?data={_PK_:" + paramBean.getStr("TASK_ID") + "}");
            TodoUtils.insert(todoBean);
        }
    }
    
}
