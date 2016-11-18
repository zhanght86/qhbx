package com.rh.oa.mt;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.org.UserBean;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.bean.SqlBean;
import com.rh.core.serv.dict.DictMgr;
import com.rh.core.util.Constant;
import com.rh.core.util.DateUtils;
import com.rh.core.util.JsonUtils;
import com.rh.core.util.Strings;

/**
 * 会议室占用情况列表
 * @author ruaho_hdy
 * 
 */
public class MROccupyServ extends CommonServ {
    /**会议预定开始时间点*/
    private static final int START_HOUR = 8;
    /**会议预定结束时间点*/
    private static final int END_HOUR = 20;
    /**每分钟占用像素宽度*/
    private static final float MINUTE_POS = 0.75f;

    @Override
    protected void afterQuery(ParamBean paramBean, OutBean outBean) {
        List<Bean> outList = outBean.getDataList();
        String date = paramBean.getStr("NEW_DATE");
        date = Strings.isEmpty(date) ? DateUtils.getDate() : date;
        List<Bean> bookingList = getMRBookingList(date);
        for (Bean b : outList) {
            List<Bean> list = findsBookingList(bookingList, b.getId(), date);
            b.set("OCCUPY_DETAIL", JsonUtils.toJson(list));
        }
    }

    /**
     * 获取指定会议室的会议预定信息
     * @param list 会议预定list
     * @param mRId 会议室id
     * @param date 指定日期，格式为：2013-01-01
     * @return 返回指定会议室的会议预定信息list
     */
    private List<Bean> findsBookingList(List<Bean> list, String mRId, String date) {
        
        List<Bean> outList = new ArrayList<Bean>();
        for (Bean booking : list) {
            if (booking.getStr("MR_ID").equals(mRId)) {
                boolean result = parseTime(booking, date);
                if (result) {
                    booking.set("isOccupy", true);
                    booking.set("USER_NAME", DictMgr.getFullName("SY_ORG_USER", booking.getStr("S_USER")));
                    outList.add(booking);
                }
            }
        }
        
        List<Bean> resultList = new ArrayList<Bean>();
        //
        int preStartHour = START_HOUR;
        int preStartMinute = 0;
        for (Bean bean : outList) {
            int startHour = bean.getInt("START_HOUR");
            int startMinute = bean.getInt("START_MINUTE");
            if (startHour == preStartHour) {
                if ((startMinute - preStartMinute) > 0) {
                    resultList.add(createIdleBean(preStartHour, preStartMinute, startHour, startMinute));
                }
            } else if (startHour > preStartHour) {
                resultList.add(createIdleBean(preStartHour, preStartMinute, startHour, startMinute));
            }
            resultList.add(bean);
            preStartHour = bean.getInt("END_HOUR");
            preStartMinute = bean.getInt("END_MINUTE");            
        }
        
        if (preStartHour < END_HOUR) {
            resultList.add(createIdleBean(preStartHour, preStartMinute, END_HOUR, 0));
        }
        
        return resultList;
    }
    
    /**
     * 填充每个色块的属性
     * @param startHour 开始时间的小时部分
     * @param startMinute 开始时间的分钟部分
     * @param endHour 结束时间小时部分
     * @param endMinute 结束时间分钟部分
     * @return 色块对象
     */
    private Bean createIdleBean(int startHour, int startMinute, int endHour, int endMinute) {
        Bean bean = new Bean();
        int startPos = this.addStartPos(bean, startHour, startMinute);
        int endPos = this.addEndPos(bean, endHour, endMinute);
        bean.set("left", startPos);
        bean.set("width", endPos - startPos);
        this.addBookTime(bean);   
        return bean;
    }

    /**
     * 判断会预定有效时间段
     * @param booking 会议室预定记录
     * @param date 指定日期
     * @return 如果预定记录时间有效，则返回true，否则返回false
     */
    private boolean parseTime(Bean booking, String date) {
        int startPos = 0; //默认当前色块在页面标尺位置
        final String startTime = booking.getStr("START_TIME");
        //获取日期部分
        final String startDate = startTime.substring(0, 10);
        // 如果预定开始日期，必今天早，则从8点开始，否则要计算小时
        if (startDate.compareTo(date) == 0) { // 开始时间为今天
            Calendar sDate = DateUtils.getCalendar(startTime);
            int startHour = sDate.get(Calendar.HOUR_OF_DAY);
            int startMinute = sDate.get(Calendar.MINUTE);
            //如果当前时间大于显示结束时间点，则直接返回
            if (startHour > END_HOUR) {
                return false;
            }
            //填充色块
            startPos = addStartPos(booking, startHour, startMinute);
        } else { // 开始时间早于今天
            //如果时间早于，则从开始时间点展示预订情况
            startPos = 0;
            booking.set("START_HOUR", START_HOUR);
            booking.set("START_MINUTE", 0);
        }
        booking.set("left", startPos);
        final String endTime = booking.getStr("END_TIME");
        final String endDate = endTime.substring(0, 10);
        int endPos = 0;
        if (endDate.compareTo(date) > 0) { // 结束时间晚于今天
            endPos = Math.round((END_HOUR - START_HOUR) * 60 * MINUTE_POS);
            booking.set("END_HOUR", END_HOUR);
            booking.set("END_MINUTE", 0);
        } else {
            Calendar eDate = DateUtils.getCalendar(endTime);
            int endHour = eDate.get(Calendar.HOUR_OF_DAY);
            int endMinute = eDate.get(Calendar.MINUTE);
            endPos = addEndPos(booking, endHour, endMinute);
        }
        booking.set("width", endPos - startPos);
        addBookTime(booking);
        return true;
    }

    /**
     * 添加预定时间显示标题
     * @param booking 当前色块对象
     */
    private void addBookTime(Bean booking) {
        StringBuilder bt = new StringBuilder();
        String startHour = booking.getStr("START_HOUR");
        bt.append(startHour.length() == 1 ? "0" + startHour : startHour);
        bt.append(":");
        String starMin = booking.getStr("START_MINUTE");
        bt.append(starMin.length() == 1 ? "0" + starMin : starMin);
        bt.append(" - ");
        String endHour = booking.getStr("END_HOUR");
        bt.append(endHour.length() == 1 ? "0" + endHour : endHour);
        bt.append(":");
        String endMin = booking.getStr("END_MINUTE");
        bt.append(endMin.length() == 1 ? "0" + endMin : endMin);

        booking.set("bookTime", bt.toString());
    }

    /**
     * 添加结束刻度尺位置
     * @param booking 当前色块对象
     * @param endHour 结束时间小时部分
     * @param endMinute 结束时间分钟部分
     * @return 刻度位置
     */
    private int addEndPos(Bean booking, int endHour, int endMinute) {
        int endPrio;
        if (endHour >= END_HOUR) { // 结束时间晚于20点
            endPrio = Math.round((END_HOUR - START_HOUR) * 60 * MINUTE_POS);
            booking.set("END_HOUR", END_HOUR);
            booking.set("END_MINUTE", "00");
        } else { // 结束时间早于20点
            endPrio = Math.round((endHour - START_HOUR) * 60 * MINUTE_POS);
            endPrio += Math.round(endMinute * MINUTE_POS);
            booking.set("END_HOUR", endHour > 9 ? endHour : "0" + endHour);
            booking.set("END_MINUTE", endMinute > 9 ? endMinute : "0" + endMinute);
        }
        return endPrio;
    }

    /**
     * 添加开始刻度位置
     * @param booking 当前色块对象
     * @param startHour 开始小时部分
     * @param startMinute 开始分钟部分
     * @return 开始刻度
     */
    private int addStartPos(Bean booking, int startHour, int startMinute) {
        int startPos;
        if (startHour < START_HOUR) {
            startHour = START_HOUR;
        }
        startPos = Math.round((startHour - START_HOUR) * 60 * MINUTE_POS);
        startPos += Math.round(startMinute * MINUTE_POS);
        booking.set("START_HOUR", startHour > 9 ? startHour : "0" + startHour);
        booking.set("START_MINUTE", startMinute > 9 ? startMinute : "0" + startMinute);
        return startPos;
    }

    /**
     * 
     * @param date 指定日期，格式为：2013-02-12
     * @return 指定日期的会议室预定记录
     */
    private List<Bean> getMRBookingList(String date) {
        UserBean ub = Context.getUserBean();
        SqlBean sql = new SqlBean();
        sql.and("S_FLAG", Constant.YES_INT).andLT("STATUS", 11);
        sql.and("S_ODEPT", ub.getODeptCode()).andLTE("START_TIME", date + " 23:59:59");
        sql.andGTE("END_TIME", date + " 00:00:00").asc("START_TIME");
        return ServDao.finds("OA_MT_BOOKING", sql);
    }
}
