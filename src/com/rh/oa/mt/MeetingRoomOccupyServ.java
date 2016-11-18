package com.rh.oa.mt;

import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.bean.SqlBean;
import com.rh.core.util.Constant;
import com.rh.core.util.DateUtils;
import com.rh.oa.mt.util.MeetingRoomConstant;

/**
 * 会议室占用列表服务
 * 
 * @author hdy
 * 
 */

public class MeetingRoomOccupyServ extends CommonServ {

	@Override
    protected void afterQuery(ParamBean paramBean, OutBean outBean) {
        List<Bean> outBeanList = outBean.getList(Constant.RTN_DATA);
        for (Bean m : outBeanList) {
            // 会议室状态
            String mrStatusFlag = m.getStr("STATUS");
            if (MeetingRoomConstant.MEETINGROOM_UNUSED.equals(mrStatusFlag)) {
                // 如果会议室不可用
                m.set(MeetingRoomConstant.MORNING, "GRAY");
                m.set(MeetingRoomConstant.AFTERNOON, "GRAY");
                m.set(MeetingRoomConstant.EVENING, "GRAY");
                continue;
            }
            
            //会议室占用状态
            String mrStatus = "";
            //占用会议室的会议室预定ID
            String morningIds = "", afternoonIds = "", eveningIds = "";

            // 取得指定会议室及指定日期的预定记录
            if (paramBean.getStr("NEW_DATE").isEmpty()) {
                paramBean.set("NEW_DATE", DateUtils.getDate());
            }
            List<Bean> bookingList = getBookingInMeetingRoom(m.getId(), paramBean.getStr("NEW_DATE"));
            for (Bean b : bookingList) {
                try {
                    String statusBit = MeetingRoomConstant.isWhichArea(paramBean.getStr("NEW_DATE"),
                            formatDate(b.getStr("START_TIME")), formatDate(b.getStr("END_TIME")));
                    mrStatus += statusBit;
                    if (statusBit.indexOf(MeetingRoomConstant.MORNING_OCCUPY) >= 0) {
                        morningIds += b.getId() + ",";
                    }
                    if (statusBit.indexOf(MeetingRoomConstant.AFTERNOON_OCCUPY) >= 0) {
                        afternoonIds += b.getId() + ",";
                    }
                    if (statusBit.indexOf(MeetingRoomConstant.EVENING_OCCUPY) >= 0) {
                        eveningIds += b.getId() + ",";
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }

            if (mrStatus.indexOf(MeetingRoomConstant.MORNING_OCCUPY) >= 0) {
                m.set(MeetingRoomConstant.MORNING, "RED," + morningIds.substring(0, morningIds.length() - 1));
            } else {
                m.set(MeetingRoomConstant.MORNING, "BLUE");
            }

            if (mrStatus.indexOf(MeetingRoomConstant.AFTERNOON_OCCUPY) >= 0) {
                m.set(MeetingRoomConstant.AFTERNOON, "RED," + afternoonIds.substring(0, afternoonIds.length() - 1));
            } else {
                m.set(MeetingRoomConstant.AFTERNOON, "BLUE");
            }
            
            if (mrStatus.indexOf(MeetingRoomConstant.EVENING_OCCUPY) >= 0) {
                m.set(MeetingRoomConstant.EVENING, "RED," + eveningIds.substring(0, eveningIds.length() - 1));
            } else {
                m.set(MeetingRoomConstant.EVENING, "BLUE");
            }
        }
    }
	
	
	   /**
     * 获得本机构，指定日期的预定数据
     * @param mrId 会议室ID
     * @param date 查询日期  例如：2012-12-21
     * @return 查询结果集
     */
    private List<Bean> getBookingInMeetingRoom(String mrId, String date) {
     // 确定会议预定查询参数
        SqlBean sql = new SqlBean().selects("BOOKING_ID,MEETING_TYPE,STATUS,BOOKING_LEVEL,START_TIME,END_TIME")
                .and("MR_ID", mrId).andLTE("START_TIME", date + " 23:59:59").andGTE("END_TIME", date + " 00:00:00")
                .andLT("STATUS", MeetingRoomConstant.BOOKING_STATUS_NOT_AGREE)
                .and("S_ODEPT", Context.getUserBean().getODeptCode());
        List<Bean> bookingList = ServDao.finds(MeetingRoomConstant.OA_MT_BOOKING_CODE, sql);
        return bookingList;
    }
    
    /**
     * 格式化所需时间格式
     * @param date 传入时间字符转
     * @return 返回格式化之后的时间字符转
     */
    private String formatDate(String date) {
        if (date.split(":").length == 2) {
            return date + ":00";
        }
        return date;
    }
}


