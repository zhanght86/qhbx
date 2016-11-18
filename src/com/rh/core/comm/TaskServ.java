package com.rh.core.comm;

import com.rh.core.base.Bean;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServDefBean;
import com.rh.core.serv.ServUtils;
import com.rh.core.util.Constant;

/**
 * 日程服务类
 * @author liuxinhe
 * 
 */
public class TaskServ extends CommonServ {
    /** 任务服务ID **/
    public static final String TASK_SERV_ID = "SY_COMM_TASK";
    /** 任务分配服务ID **/
    public static final String TASK_USER_SERV_ID = "SY_COMM_CAL_USERS";

    /**
     * 保存之后的操作
     * @param paramBean 参数Bean
     * @param outBean 参数Bean
     */
    protected void afterSave(ParamBean paramBean, OutBean outBean) {
        String servId = paramBean.getServId();
        ServDefBean servBean = ServUtils.getServDef(servId);
        String doneUsers = paramBean.getStr("DONE_USERS");
        if (paramBean.getAddFlag()) { // 保存强制添加标志或者没有主键，则自动为添加模式
            if (doneUsers.length() > 0) {
                String[] doneUser = doneUsers.split(",");
                Bean doneUserBean = new Bean();
                for (String doneUserID : doneUser) {
                    doneUserBean = new Bean();
                    doneUserBean.set("USER_CODE", doneUserID);
                    doneUserBean.set("CAL_ID", paramBean.getStr(servBean.getPKey()));
                    doneUserBean.set("TYPE", "TASK");
                    doneUserBean.set("STATE", 2);
                    doneUserBean.set("ORDER_NUM", 10);
                    ServDao.save(TASK_USER_SERV_ID, doneUserBean);
                }
            }
        } else {
            // 修改的时候首先判断，DONE_USERS是否变更
            if (paramBean.containsKey("DONE_USERS")) {

                Bean oldBean = paramBean.getSaveOldData();
                String oldDoneUsers = oldBean.getStr("DONE_USERS");
                // 分析变化的用户code，然后进行修改
                if (doneUsers.length() > 0) {
                    Bean doneUserBean = new Bean();
                    String[] doneUser = doneUsers.split(",");
                    if (oldDoneUsers.length() > 0) {
                        String[] oldDoneUser = oldDoneUsers.split(",");
                        for (int i = 0; i < doneUser.length; i++) {
                            for (int j = 0; j < oldDoneUser.length; j++) {
                                if (doneUser[i].equals(oldDoneUser[j])) {
                                    oldDoneUser[j] = "";
                                    doneUser[i] = "";
                                    break;
                                }
                            }
                        }
                        // 若不为空则添加
                        for (int k = 0; k < doneUser.length; k++) {
                            if (doneUser[k].length() > 0) {
                                doneUserBean = new Bean();
                                doneUserBean.set("USER_CODE", doneUser[k]);
                                doneUserBean.set("CAL_ID", paramBean.getId());
                                doneUserBean.set("TYPE", "TASK");
                                doneUserBean.set("STATE", 2);
                                doneUserBean.set("ORDER_NUM", 10);
                                ServDao.save(TASK_USER_SERV_ID, doneUserBean);
                            }
                        }
                        // 若不为空则删除
                        String delteUsers = "";
                        for (int m = 0; m < oldDoneUser.length; m++) {
                            if (oldDoneUser[m].length() > 0) {
                                delteUsers = delteUsers + ",'" + oldDoneUser[m] + "'";
                            }
                        }
                        if (delteUsers.length() > 1) {
                            delteUsers = delteUsers.substring(1);
                            String sql = " and user_code in(" + delteUsers + ")";
                            doneUserBean = new Bean();
                            doneUserBean.set("CAL_ID", paramBean.getId());
                            doneUserBean.set(Constant.PARAM_WHERE, sql);
                            ServDao.deletes(TASK_USER_SERV_ID, doneUserBean);
                        }

                    } else {
                        for (String doneUserID : doneUser) {
                            doneUserBean = new Bean();
                            doneUserBean.set("USER_CODE", doneUserID);
                            doneUserBean.set("CAL_ID", paramBean.getId());
                            doneUserBean.set("TYPE", "TASK");
                            doneUserBean.set("STATE", 2);
                            doneUserBean.set("ORDER_NUM", 10);
                            ServDao.save(TASK_USER_SERV_ID, doneUserBean);
                        }
                    }
                } else {
                    // 删除日程归属人
                    Bean doneUserBean = new Bean();
                    doneUserBean.set("CAL_ID", paramBean.getId());
                    ServDao.deletes(TASK_USER_SERV_ID, doneUserBean);
                }

            }
        }

    }

    /**
     * 删除之后的操作
     * @param paramBean 参数Bean
     * @param outBean 参数Bean
     */
    protected void afterDelete(ParamBean paramBean, OutBean outBean) {
        Bean doneUserBean = new Bean();
        doneUserBean.set("CAL_ID", paramBean.getId());
        ServDao.deletes(TASK_USER_SERV_ID, doneUserBean);

    }
}
