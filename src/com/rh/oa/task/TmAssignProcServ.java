package com.rh.oa.task;

import java.util.ArrayList;
import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.bean.SqlBean;

public class TmAssignProcServ extends CommonServ{
    private static final String BN_TM_ASSIGN_SERV = "BN_TM_ASSIGN";
    private static final String BN_TM_ASSIGN_PROC_SERV = "BN_TM_ASSIGN_PROC";
    @Override
  protected void afterSave(ParamBean paramBean, OutBean outBean) {
      // 任务分配单进展情况说明
      Bean oldBean = paramBean.getSaveOldData();
      if (!(oldBean.getStr("TASK_CONDITION").equals(outBean.getStr("TASK_CONDITION")))) {
          SqlBean sqlBean = new SqlBean();
          sqlBean.and("ID", outBean.getStr("ID"));
          List<Bean> assignProcListBean = new ArrayList<Bean>();
          assignProcListBean = ServDao.finds(BN_TM_ASSIGN_PROC_SERV, sqlBean);
          for (Bean bean : assignProcListBean) {
              if (!(outBean.getStr("TASK_CONDITION").equals(bean.getStr("TASK_CONDITION")))) {
                  return;
              }
          }
          SqlBean assignSqlBean = new SqlBean();
          assignSqlBean.setId(outBean.getStr("ID"));
          assignSqlBean.set("TASK_CONDITION", outBean.getStr("TASK_CONDITION"));
          // ServDao.update(BN_TM_TASK_SERV, taskSqlBean);
          ServDao.updates(BN_TM_ASSIGN_SERV, new Bean().set("TASK_CONDITION", outBean.getStr("TASK_CONDITION")),
                  new SqlBean().and("ID", outBean.getStr("ID")));

      }
  }

}
