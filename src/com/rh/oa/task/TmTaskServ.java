package com.rh.oa.task;



import java.util.ArrayList;
import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.comm.todo.TodoBean;
import com.rh.core.comm.todo.TodoUtils;
import com.rh.core.org.UserBean;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.bean.SqlBean;
import com.rh.core.serv.dict.DictMgr;


import com.rh.core.util.Constant;
import com.rh.core.util.DateUtils;


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

    public Bean taskAssignStart(ParamBean paramBean) {
        //当点击任务分配按钮时，状态就变为初始状态--已分配
       
            Bean dictBean = DictMgr.getItem("BN_QS_TASK_STATE", TASK_STATE_START);
            String dictItemCode = dictBean.getStr("ITEM_CODE");
//            paramBean.set("TASK_STATE", dictItemCode);
            SqlBean taskBean=new SqlBean(); 
            taskBean.setId(paramBean.getStr("TASK_ID"));
            taskBean.set("TASK_STATE",dictBean.getStr("ITEM_CODE"));
            ServDao.update(BN_TM_TASK, taskBean);
            //当点击任务分配按钮时批量修改子单的状态为已分配
            SqlBean assignSqlBean=new SqlBean();
            assignSqlBean.and("TASK_ID", paramBean.getStr("TASK_ID"));
      
            
        // 根据任务分配对象，创建相应任务反馈单
        Bean oldBean=paramBean.getSaveOldData();
        UserBean userBean = Context.getUserBean();
        // 获取任务分配对象
        String TASK_RECEIVER = paramBean.getStr("TASK_RECEIVER");
        if (!"".equals(TASK_RECEIVER)) {
            String[] users = TASK_RECEIVER.split(",");
            if (users != null && users.length > 0) {
                for (String userCode : users) {
                    SqlBean sqlBean = new SqlBean();
                    sqlBean.and("TASK_ID", paramBean.getStr("TASK_ID"));
                    sqlBean.and("TASK_USER", userCode);
                    Bean assignBean = ServDao.find(BN_TM_ASSIGN, sqlBean);
                    if(assignBean==null){
                        Bean linkData=new Bean();
                        linkData.set("TASK_ID", paramBean.getStr("TASK_ID"));
                        linkData.set("TASK_USER", userCode);
                        linkData.set("TASK_STATE", TASK_STATE_START);
//                        linkData.set("TASK_STATE", DictMgr.getItem("BN_QS_TASK_STATE",TASK_STATE).getStr("ITEM_CODE"));
                        ServDao.create(BN_TM_ASSIGN, linkData);
//                        送待办
                        TodoBean todoBean=new TodoBean();
                        todoBean.setSender(userBean.getUser());
                        todoBean.setTitle(paramBean.getStr("TASK_NAME"));
                        todoBean.setOwner(userCode);
                        todoBean.setCode(BN_TM_TASK);
                        todoBean.setObjectId1(paramBean.getStr("TASK_ID"));
                        todoBean.setUrl(BN_TM_TASK + ".byid.do?data={_PK_:" + paramBean.getStr("TASK_ID") + "}");
                        TodoUtils.insert(todoBean);
                        
                    }
                }
            }
        }
        return new Bean().set(Constant.RTN_MSG, Constant.RTN_MSG_OK);
    }
    public Bean taskAssignEnd(ParamBean paramBean) {
        //当点击终止任务的按钮则主单卡片和子单列表的状态均变为任务终止
        SqlBean taskBean=new SqlBean();
        taskBean.setId(paramBean.getStr("TASK_ID"));
        taskBean.set("TASK_STATE", TASK_STATE_END);
        ServDao.update(BN_TM_TASK, taskBean);
        
        
        SqlBean taskBean2=new SqlBean();
        taskBean2.and("TASK_ID", paramBean.getStr("TASK_ID"));
        
       Bean dateBean = new Bean();
       dateBean.set("TASK_STATE", TASK_STATE_END);
        ServDao.updates(BN_TM_ASSIGN, dateBean,taskBean2);
        return new Bean().set(Constant.RTN_MSG, Constant.RTN_MSG_OK);
    }
    public Bean sureComplete(ParamBean paramBean) {
        //当点击确认完成的按钮则主单卡片和子单列表的状态均变为已完成
        SqlBean taskBean=new SqlBean();
        taskBean.setId(paramBean.getStr("TASK_ID"));
        taskBean.set("TASK_STATE", TASK_STATE_SURECOMPLETE);
        ServDao.update(BN_TM_TASK, taskBean);
        
        
        SqlBean taskBean2=new SqlBean();
        taskBean2.and("TASK_ID", paramBean.getStr("TASK_ID"));
        
       Bean dateBean = new Bean();
       dateBean.set("TASK_STATE", TASK_STATE_SURECOMPLETE);
        ServDao.updates(BN_TM_ASSIGN, dateBean,taskBean2);
        return new Bean().set(Constant.RTN_MSG, Constant.RTN_MSG_OK);
    }
    
}

