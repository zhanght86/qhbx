package com.rh.oa.mt;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServMgr;
import com.rh.core.serv.bean.SqlBean;
import com.rh.core.serv.dict.DictMgr;
import com.rh.core.util.Strings;

/**
 * 实现会议通知日程操作
 * @author ruaho_hdy
 *
 */
public class MeetingCalendarImp implements MeetingCalendar {

    /** 日程服务ID **/
    public static final String CALENDAR_SERV_ID = "SY_COMM_CAL";
    /**日程分配服务ID **/
    public static final String CALENDAR_USER_SERV_ID = "SY_COMM_CAL_USERS";
    /**会议日程类型*/
    private static final String CAL_TYPE_MEETING = "MEETING";
    /**会议回执服务ID*/
    private static final String OA_MT_MEETING = "OA_MT_MEETING_RETURN_NOTICE";
    /**会议室字典*/
    private static final String OA_MT_MEETINGROOM_DIC = "OA_MT_MEETINGROOM_DIC";
    /**会议室维护服务id*/
    private static final String OA_MT_MEETINGROOM = "OA_MT_MEETINGROOM";
    /**部门服务*/
    private static final String SY_ORG_DEPT = "SY_ORG_DEPT";
    
    public OutBean add(Bean meetingBean, String doneUsers) {
        ParamBean calendarBean = null;
        if (null != meetingBean) {
            calendarBean = new ParamBean();
            calendarBean.set("CAL_TITLE", meetingBean.getStr("TITLE"));
            //会议内容
            StringBuffer meetingContent = new StringBuffer("");
            meetingContent.append("预计在").append(meetingBean.getStr("BEGIN_TIME")).append("至")
                          .append(meetingBean.getStr("END_TIME"));
            String mrName = getMRDictName(meetingBean.getStr("PLACE")); //获取会议室名称
            if (!Strings.isEmpty(mrName)) {
                meetingContent.append("在").append(mrName);
            }
            meetingContent.append("召开").append(meetingBean.getStr("TITLE"));
            calendarBean.set("CAL_CONTENT", meetingContent.toString());
            calendarBean.set("CAL_TYPE", CAL_TYPE_MEETING);
            calendarBean.set("CAL_TYPE", CAL_TYPE_MEETING);
            calendarBean.set("CAL_START_DATE", "");
            calendarBean.set("CAL_START_TIME", meetingBean.getStr("BEGIN_TIME"));
            calendarBean.set("CAL_END_DATE", "");
            calendarBean.set("CAL_END_TIME", meetingBean.getStr("END_TIME"));
            calendarBean.set("SERV_ID", OA_MT_MEETING);
            calendarBean.set("SERV_SRC_ID", OA_MT_MEETING);
            calendarBean.set("DATA_ID", meetingBean.getId());
            calendarBean.set("DONE_USERS", doneUsers.length() == 0 ? Context.getUserBean().getCode() : doneUsers);
            return ServMgr.act(CALENDAR_SERV_ID, "addCalensar", calendarBean);
        }
        return new OutBean().setError();
    }

    public OutBean delete(Bean meetingBean) {
        ParamBean paramBean = new ParamBean();
        paramBean.set("DATA_ID", meetingBean.getId());
        return ServMgr.act(CALENDAR_SERV_ID, "deleteAll", paramBean);
    }
    
    /**
     * 返回会议室名称
     * @param meetingRoomId 会议室id
     * @return 会议室名称
     */
    private String getMRDictName(String meetingRoomId) {
        if (Strings.isEmpty(meetingRoomId)) {
            return "";
        } else {
            //获取会议室名称
            String fullName = DictMgr.getFullName(OA_MT_MEETINGROOM_DIC, meetingRoomId);
            //如果是跨级
            if (Strings.isEmpty(fullName)) {
                SqlBean sql = new SqlBean();
                sql.selects("NAME, S_ODEPT").and("MR_ID", meetingRoomId).and("S_FLAG", 1);
                Bean out = ServDao.find(OA_MT_MEETINGROOM, sql);
                if (null != out) {
                    fullName = DictMgr.getFullName(SY_ORG_DEPT, out.getStr("S_ODEPT"));
                    if (fullName.contains("/")) {
                        fullName = fullName.substring(fullName.lastIndexOf("/") + 1);
                        fullName = "[" + fullName + "]" + out.getStr("NAME");
                    }
                }
            }
            return fullName;
        }
    }
}
