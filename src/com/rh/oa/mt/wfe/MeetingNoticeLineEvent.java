package com.rh.oa.mt.wfe;

import java.util.Date;

import com.rh.core.base.Bean;
import com.rh.core.comm.ConfMgr;
import com.rh.core.comm.calendar.CalendarMgr;
import com.rh.core.comm.remind.RemindMgr;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServMgr;
import com.rh.core.util.DateUtils;
import com.rh.core.wfe.WfAct;
import com.rh.core.wfe.util.AbstractLineEvent;

/**
 * 会议通知线事件监听类.
 * @author wangchen
 */
public class MeetingNoticeLineEvent extends AbstractLineEvent {
    /**参会人.*/
    private static final String CONFEREE = "CONFEREE";
    /**被通知人.*/
    private static final String NOTIFIED = "NOTIFIED";

    /**
     * 顺序送下一个节点时触发此事件，判断下一节点绑定的人员类型，并在与会人员列表中存入一条送交信息.
     * @param preWfAct 前一个节点的实例.
     * @param nextWfAct 下一个节点的实例.
     * @param lineDef 线定义Bean.
     */
    public void forward(WfAct preWfAct, WfAct nextWfAct, Bean lineDef) {
        // 表单数据bean
        Bean dataBean = nextWfAct.getProcess().getServInstBean();
        // 流程实例bean
        Bean wfeBean = nextWfAct.getProcess().getProcInstBean();
        // 服务ID
        String servId = wfeBean.getStr("SERV_ID");
        // 与会人员数组
        String confereesPersons = dataBean.getStr("CONFEREES_CODES");
        // 被通知人数组
        String notifiedPersons = dataBean.getStr("NOTIFIED_CODES");
        if (nextWfAct.getNodeInstBean().getStr("TO_USER_ID") != null) {
            ParamBean paramBean = new ParamBean("OA_MT_CONFEREE", ServMgr.ACT_SAVE);
            String sendUser = nextWfAct.getNodeInstBean().getStr("TO_USER_ID");
            if (confereesPersons.indexOf(sendUser) >= 0) {
                paramBean.set("USER_TYPE", CONFEREE);
            } else if (notifiedPersons.indexOf(sendUser) >= 0) {
                paramBean.set("USER_TYPE", NOTIFIED);
            }
            paramBean.set("MEETING_ID", dataBean.getId());
            paramBean.set("USER_CODE", sendUser);
            if (!paramBean.isEmpty()) {
                ServMgr.act(paramBean);
            }
            //发出提醒
            sendRemind(dataBean, servId, sendUser);
            //保存日程
            addCalendar(dataBean, servId, sendUser);
        }
    }
    /**
     * 发送提醒
     * @param dataBean 表单数据bean
     * @param servId 服务ID
     * @param sendUser 用户ID.
     */
    protected void sendRemind(Bean dataBean, String servId, String sendUser) {
        //会议开始时间
        Date beginTime = DateUtils.getDateFromString(dataBean.getStr("BEGIN_TIME"), DateUtils.FORMAT_DATETIME_HM);
        Bean remindBean = new Bean();
        if (dataBean.getStr("CONFEREES_CODES").indexOf(sendUser) >= 0
                || dataBean.getStr("NOTIFIED_CODES").indexOf(sendUser) >= 0) {
            //从系统配置中获取提前提醒的时间
            Integer remindTime = Integer.valueOf(ConfMgr.getConf("OA_MT_MEETING_REMIND_TIME", ""));
            //从系统配置中获取提醒类型
            String remindType = ConfMgr.getConf("OA_MT_MEETING_REMIND_TYPE", "");
            //计算会议提前后的时间                
            Date executeTime = DateUtils.addMinutes(beginTime, -remindTime);
            remindBean.set("REM_TITLE", dataBean.getStr("TITLE"));
            remindBean.set("REM_CONTENT", dataBean.getStr("CONTENT"));
            remindBean.set("S_USER", sendUser);
            remindBean.set("EXECUTE_TIME", DateUtils.getStringFromDate(executeTime, DateUtils.FORMAT_DATETIME_HM));
            remindBean.set("TYPE", remindType);
            remindBean.set("S_EMGRENCY", dataBean.getStr("EMERGENCY"));            
            remindBean.set("SERV_ID", servId);
            remindBean.set("DATA_ID", dataBean.getStr("MEETING_ID"));
        }
        if (!remindBean.isEmpty()) {
            //添加提醒
            RemindMgr.add(remindBean, sendUser);
        }
    }
    /**
     * 添加日程
     * @param dataBean 表单数据bean
     * @param servId 服务ID
     * @param sendUser 用户ID.
     */
    protected void addCalendar(Bean dataBean, String servId, String sendUser) {
        //会议开始时间
        Date beginTime = DateUtils.getDateFromString(dataBean.getStr("BEGIN_TIME"), DateUtils.FORMAT_DATETIME_HM);
        //会议结束时间
        Date endTime = DateUtils.getDateFromString(dataBean.getStr("END_TIME"), DateUtils.FORMAT_DATETIME_HM);
        //保存日程
        Bean calendarBean = new Bean();                 
        calendarBean.set("CAL_TITLE", dataBean.getStr("TITLE"));
        calendarBean.set("CAL_CONTENT", dataBean.getStr("CONTENT"));
        calendarBean.set("S_USER", sendUser);
        calendarBean.set("CAL_TYPE", "MEETING");
        calendarBean.set("CAL_START_DATE", DateUtils.getStringFromDate(beginTime, DateUtils.FORMAT_DATE));
        calendarBean.set("CAL_START_TIME", DateUtils.getStringFromDate(beginTime, DateUtils.FORMAT_TIME));
        calendarBean.set("CAL_END_DATE", DateUtils.getStringFromDate(endTime, DateUtils.FORMAT_DATE));
        calendarBean.set("CAL_END_TIME", DateUtils.getStringFromDate(endTime, DateUtils.FORMAT_TIME));
        calendarBean.set("SERV_ID", servId);
        calendarBean.set("DATA_ID", dataBean.getStr("MEETING_ID"));           
        if (!calendarBean.isEmpty()) {
            //添加日程
            CalendarMgr.addCalendar(calendarBean);
        }
    }

    /**
     * 返回操作送下一个节点时触发此事件.
     * @param preWfAct 前一个节点的实例.
     * @param nextWfAct 下一个节点的实例.
     * @param lineDef 线定义Bean.
     */
    public void backward(WfAct preWfAct, WfAct nextWfAct, Bean lineDef) {

    }
}
