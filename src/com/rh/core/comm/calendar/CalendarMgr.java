package com.rh.core.comm.calendar;

import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.org.UserBean;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServUtils;
import com.rh.core.serv.bean.SqlBean;
import com.rh.core.util.Constant;

/**
 * 日程管理类
 * @author liuxinhe
 * 
 */
public class CalendarMgr {
    /** 日程服务ID **/
    public static final String CALENDAR_SERV_ID = "SY_COMM_CAL";

    /** 日程分配服务ID **/
    public static final String CALENDAR_USER_SERV_ID = "SY_COMM_CAL_USERS";
    
    /**会议回执服务ID*/
    private static final String OA_MT_MEETING = "OA_MT_MEETING_RETURN_NOTICE";

    /**
     * 添加日程
     * @param calendarBean 日程bean
     * 
     */
    public static void addCalendar(Bean calendarBean) {
        String userCode = null;
        String cmpyCode = null;
        UserBean userBean = Context.getUserBean();
        if (userBean != null) {
            userCode = userBean.getCode();
            cmpyCode = userBean.getCmpyCode();
        }
        Bean addBean = new Bean();
        addBean.set("CAL_TITLE", calendarBean.getStr("CAL_TITLE"));
        addBean.set("CAL_CONTENT", calendarBean.getStr("CAL_CONTENT"));
        addBean.set("CAL_TYPE", calendarBean.getStr("CAL_TYPE"));
        addBean.set("CAL_STATE", 2);
        String creator = calendarBean.getStr("S_USER");
        //如果传递创建者code，则添加创建者，没有默认为当前用户
        if (null == creator || creator.equals("")) {
        	addBean.set("S_USER", userCode);
        } else {
        	addBean.set("S_USER", creator);
        }
        addBean.set("CAL_START_DATE", calendarBean.getStr("CAL_START_DATE"));
        addBean.set("CAL_START_TIME", calendarBean.getStr("CAL_START_TIME"));
        addBean.set("CAL_END_DATE", calendarBean.getStr("CAL_END_DATE"));
        addBean.set("CAL_END_TIME", calendarBean.getStr("CAL_END_TIME"));
        addBean.set("SERV_ID", calendarBean.getStr("SERV_ID"));
        addBean.set("SERV_SRC_ID", calendarBean.getStr("SERV_SRC_ID"));
        addBean.set("DATA_ID", calendarBean.getStr("DATA_ID"));
        addBean.set("S_CMPY", cmpyCode);
        addBean.set("DONE_USERS", calendarBean.getStr("DONE_USERS"));

        calendarBean = ServDao.save(CALENDAR_SERV_ID, addBean);
        String doneUsers = addBean.getStr("DONE_USERS");
        if (doneUsers.length() > 0) {
            String[] doneUser = doneUsers.split(",");
            Bean doneUserBean = new Bean();
            for (String doneUserID : doneUser) {
               deleteUserCalendar(addBean.getStr("DATA_ID"), doneUserID);
        	   doneUserBean = new Bean();
               doneUserBean.set("USER_CODE", doneUserID);
               doneUserBean.set("CAL_ID", calendarBean.getId());
               doneUserBean.set("TYPE", calendarBean.getStr("CAL_TYPE"));
               doneUserBean.set("STATE", 2);
               doneUserBean.set("ORDER_NUM", 10);
               ServDao.save(CALENDAR_USER_SERV_ID, doneUserBean);
            }
        }
    }

    /**
     * 修改日程信息
     * @param calendarBean 日程bean
     * 
     */
    public static void modifyCalerdar(Bean calendarBean) {
        String userCode = null;
        String cmpyCode = null;
        UserBean userBean = Context.getUserBean();
        if (userBean != null) {
            userCode = userBean.getCode();
            cmpyCode = userBean.getCmpyCode();
        }
        Bean oldBean = ServDao.find(CALENDAR_SERV_ID, calendarBean.getId());
        String servId = calendarBean.getStr("SERV_ID");
        Bean servBean = ServUtils.getServDef(servId);
        calendarBean.set("CAL_TITLE", calendarBean.getStr("CAL_TITLE"));
        calendarBean.set("CAL_CONTENT", calendarBean.getStr("CAL_CONTENT"));
        calendarBean.set("CAL_TYPE", calendarBean.getStr("CAL_TYPE"));
        calendarBean.set("CAL_STATE", 2);
        calendarBean.set("S_USER", calendarBean.getStr("S_USER"));
        calendarBean.set("CAL_START_DATE", calendarBean.getStr("CAL_START_DATE"));
        calendarBean.set("CAL_START_TIME", calendarBean.getStr("CAL_START_TIME"));
        calendarBean.set("CAL_END_DATE", calendarBean.getStr("CAL_END_DATE"));
        calendarBean.set("CAL_END_TIME", calendarBean.getStr("CAL_END_TIME"));
        calendarBean.set("SERV_ID", servId);
        calendarBean.set("SERV_SRC_ID", servBean.getStr("SERV_SRC_ID"));
        calendarBean.set("DATA_ID", calendarBean.getStr("DATA_ID"));
        calendarBean.set("S_USER", userCode);
        calendarBean.set("S_CMPY", cmpyCode);
        calendarBean.set("DONE_USERS", calendarBean.getStr("DONE_USERS"));

        calendarBean = ServDao.save(CALENDAR_SERV_ID, calendarBean);
        String doneUsers = calendarBean.getStr("DONE_USERS");

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
                    // 若不为空则添加
                    for (int k = 0; k < doneUser.length; k++) {
                        if (doneUser[k].length() > 0) {
                            doneUserBean = new Bean();
                            doneUserBean.set("USER_ID", doneUser[k]);
                            doneUserBean.set("CAL_ID", calendarBean.getId());
                            doneUserBean.set("TYPE", calendarBean.getStr("CAL_TYPE"));
                            doneUserBean.set("STATE", 2);
                            doneUserBean.set("ORDER_NUM", 10);
                            ServDao.save(CALENDAR_USER_SERV_ID, doneUserBean);
                        }
                    }
                    // 若不为空则删除
                    String delteUsers = "";
                    for (int m = 0; m < oldDoneUser.length; m++) {
                        if (oldDoneUser[m].length() > 0) {
                            delteUsers = delteUsers + "'" + oldDoneUser[m] + "',"; 
                        }
                    }
                    String sql = " and user_code in(" + delteUsers + ")";
                    doneUserBean = new Bean();
                    doneUserBean.set("CAL_ID", calendarBean.getId());
                    doneUserBean.set(Constant.PARAM_WHERE, sql);
                    ServDao.delete(CALENDAR_USER_SERV_ID, doneUserBean);
                }
            } else {
                for (String doneUserID : doneUser) {
                    doneUserBean = new Bean();
                    doneUserBean.set("USER_ID", doneUserID);
                    doneUserBean.set("CAL_ID", calendarBean.getId());
                    doneUserBean.set("TYPE", calendarBean.getStr("CAL_TYPE"));
                    doneUserBean.set("STATE", 2);
                    doneUserBean.set("ORDER_NUM", 10);
                    ServDao.save(CALENDAR_USER_SERV_ID, doneUserBean);
                }
            }
        } else {
            // 删除日程归属人
            Bean doneUserBean = new Bean();
            doneUserBean.set("CAL_ID", calendarBean.getId());
            ServDao.delete(CALENDAR_USER_SERV_ID, doneUserBean);
        }
    }

    /**
     * 删除日程
     * @param paramBean 提醒消息bean
     * 
     */
    public static void deleteCalerdar(Bean paramBean) {
        ServDao.delete(CALENDAR_SERV_ID, paramBean);
    }
    
    public static OutBean deleteAll(Bean meetingBean){
    	if (null == meetingBean) {
            return new OutBean().setError();
        }
        //获取数据主键
        String dataId = meetingBean.getStr("DATA_ID");
        SqlBean sql = new SqlBean();
        sql.selects("CAL_ID").and("DATA_ID", dataId).and("SERV_ID", OA_MT_MEETING);
        List<Bean> calendarList = ServDao.finds(CALENDAR_SERV_ID, sql);
        //删除子表数据
        for (Bean b : calendarList) {
            Bean doneUserBean = new Bean();
            doneUserBean.set("CAL_ID", b.getId());
            ServDao.deletes(CALENDAR_USER_SERV_ID, doneUserBean);
        }
        ParamBean calendarBean = new ParamBean();
        calendarBean.setWhere(" and DATA_ID = '" + dataId + "'");
        ServDao.deletes(CALENDAR_SERV_ID, calendarBean);
        return new OutBean().setOk();
    }

    /**
     * 删除用户日程
     * @param dataId 数据id
     * @param userCode 用户id
     * @return 删除成功与否，成功返回true，失败返回false
     */
    public static void deleteUserCalendar(String dataId, String userCode){
    	SqlBean sql = new SqlBean();
        sql.selects("CAL_ID").and("DATA_ID", dataId).and("SERV_ID", OA_MT_MEETING);
        List<Bean> calendarList = ServDao.finds(CALENDAR_SERV_ID, sql);
        for (Bean b : calendarList) {
        	SqlBean calendarUser  = new SqlBean();
        	calendarUser.and("CAL_ID", b.getId()).and("USER_CODE", userCode);
        	List<Bean> calendarUserList = ServDao.finds(CALENDAR_USER_SERV_ID, calendarUser);
        	for (Bean c : calendarUserList) {
        		ServDao.deletes(CALENDAR_USER_SERV_ID, c);
        	}
        }
    }
}
