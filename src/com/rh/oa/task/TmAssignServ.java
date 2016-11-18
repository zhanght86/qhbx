package com.rh.oa.task;





import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.comm.ConfMgr;
import com.rh.core.comm.todo.TodoBean;
import com.rh.core.comm.todo.TodoUtils;
import com.rh.core.org.UserBean;
import com.rh.core.org.mgr.UserMgr;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.bean.SqlBean;
import com.rh.core.util.Constant;
import com.rh.core.util.DateUtils;
import com.rh.oa.cd.RemdServ;

public class TmAssignServ extends CommonServ {

    private static final String BN_TM_TASK_SERV = "BN_TM_TASK";
    private static final String BN_TM_ASSIGN_SERV = "BN_TM_ASSIGN";
    private static final String TASK_STATE_ASSIGNED ="1";
    private static final String TASK_STATE_CARRYING ="2";
    private static final String TASK_STATE_COMPLETE ="5";
    private static final String TASK_STATE_REMIND ="4";

    public Bean insertDB(ParamBean paramBean){
        SqlBean assignBean = new SqlBean();
        assignBean.setId(paramBean.getStr("TASK_ID"));
        Bean taskBean = ServDao.find("BN_TM_TASK", assignBean);
        RemdServ remdServ = new RemdServ();
        ParamBean saveBean = new ParamBean();
        UserBean userBean = UserMgr.getUser(paramBean.getStr("ACPT_USER"));
        // 未找到被催办用户信息，则抛出错误提示
        if (userBean == null || userBean.isEmpty()) {
            return new Bean().set(Constant.RTN_MSG, "未找到被催办用户信息");
        }
        saveBean.set(Constant.PARAM_SERV_ID, "OA_CD_REMIND");
        saveBean.set("ACPT_USER", paramBean.getStr("ACPT_USER"));
        saveBean.set("ACPT_DEPT", userBean.getDeptCode());
        saveBean.set("ACPT_TDEPT", userBean.getTDeptCode());
        saveBean.set("ACPT_PHONE", "");
        saveBean.set("REMD_TITLE", taskBean.getStr("TASK_NAME"));
        saveBean.set("REMD_REASON", "任务处理超期");
        saveBean.set("REMD_CODE", ConfMgr.getConf("OA_CD_CODE_REMD", ""));
        saveBean.set("REMD_YEAR", DateUtils.getYear());
        saveBean.set("REMD_NUM", remdServ.getMaxCode(saveBean).getStr("REMD_NUM"));
        saveBean.set("DEADLINE", taskBean.getStr("TASK_END_DATE"));
        saveBean.set("REMD_STATUS", 1);
        saveBean.setId(ServDao.create("OA_CD_REMIND", saveBean).getId());
        saveBean.set("REMD_ID", saveBean.getId());
        //OutBean outBean = remdServ.save(saveBean);
        remdServ.sendTodo(saveBean);
        // 点击催办只更新当前被催办人的状态变为催办中
        SqlBean taskBean2 = new SqlBean();
        taskBean2.setId(paramBean.getStr("ASSIGN_ID"));
        taskBean2.set("TASK_STATE", TASK_STATE_REMIND);
        ServDao.update(BN_TM_ASSIGN_SERV, taskBean2);
        return new Bean().set(Constant.RTN_MSG, Constant.RTN_MSG_OK);
    }
    //点击开始进行子单状态变为进行中
    public Bean beginFeedBack(ParamBean paramBean){
        
        SqlBean assignStateBean=new SqlBean();
        assignStateBean.setId(paramBean.getStr("ASSIGN_ID"));
        assignStateBean.set("TASK_STATE", TASK_STATE_CARRYING);
        ServDao.update(BN_TM_ASSIGN_SERV, assignStateBean);
        
        SqlBean taskStateBean=new SqlBean();
        taskStateBean.setId(paramBean.getStr("TASK_ID"));
        taskStateBean.set("TASK_STATE", TASK_STATE_CARRYING);
        ServDao.update(BN_TM_TASK_SERV, taskStateBean);
        return new Bean().set(Constant.RTN_MSG, Constant.RTN_MSG_OK);
    }
    //点击完成任务子单状态变为已处理
    public Bean completeFeedBack(ParamBean paramBean){
        SqlBean assignStateBean=new SqlBean();
        assignStateBean.setId(paramBean.getStr("ASSIGN_ID"));
        assignStateBean.set("TASK_STATE", TASK_STATE_COMPLETE);
        ServDao.update(BN_TM_ASSIGN_SERV, assignStateBean);
        //所有反馈状态都变为已处理则主单状态变为已处理
//        String TASK_ID=paramBean.getStr("TASK_ID");
        SqlBean assignInfoBean=new SqlBean();
        assignInfoBean.and("TASK_ID", paramBean.getStr("TASK_ID"));
        // 根据任务ID获取子任务数量
        int assignCounts = ServDao.count(BN_TM_ASSIGN_SERV, assignInfoBean);
        assignInfoBean.and("TASK_STATE", TASK_STATE_COMPLETE);
        // 如果所有子任务均已处理完成，则修改任务状态为已处理
        if (assignCounts == ServDao.count(BN_TM_ASSIGN_SERV, assignInfoBean)) {
            SqlBean taskStateBean=new SqlBean();
            taskStateBean.setId(paramBean.getStr("TASK_ID"));
            taskStateBean.set("TASK_STATE", TASK_STATE_COMPLETE);
            ServDao.update(BN_TM_TASK_SERV, taskStateBean); 
        }
        
        //消除待办事项
        TodoBean todoBean=new TodoBean();
        todoBean.setObjectId1(paramBean.getStr("TASK_ID"));
        todoBean.setOwner(Context.getUserBean().getCode());
        TodoUtils.destroys(todoBean);
        
        return new Bean().set(Constant.RTN_MSG, Constant.RTN_MSG_OK);
    }
    //点击重新处理就向当前任务接收人发送待办记录
    public Bean sendToDoBean(ParamBean paramBean){
        TodoBean todoBean=new TodoBean();
        UserBean userBean = Context.getUserBean();
        todoBean.setSender(userBean.getUser());
        todoBean.setTitle(paramBean.getStr("TASK_NAME"));
        todoBean.setOwner(paramBean.getStr("TASK_USER"));
        todoBean.setCode(BN_TM_TASK_SERV);
        todoBean.setObjectId1(paramBean.getStr("TASK_ID"));
        todoBean.setUrl(BN_TM_TASK_SERV + ".byid.do?data={_PK_:" + paramBean.getStr("TASK_ID") + "}");
        TodoUtils.insert(todoBean);
        //重新处理任务之后状态变为已分配
        SqlBean sqlBean=new SqlBean();
        sqlBean.setId(paramBean.getStr("ASSIGN_ID"));
        sqlBean.set("TASK_STATE", TASK_STATE_ASSIGNED);
        ServDao.update(BN_TM_ASSIGN_SERV, sqlBean);
        
        SqlBean sqlBean2=new SqlBean();
        sqlBean2.setId(paramBean.getStr("TASK_ID"));
        sqlBean2.set("TASK_STATE", TASK_STATE_ASSIGNED);
        ServDao.update(BN_TM_TASK_SERV, sqlBean2);
        return new Bean().set(Constant.RTN_MSG, Constant.RTN_MSG_OK);
    }
    
}
