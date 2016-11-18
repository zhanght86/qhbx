package com.rh.oa.mt;

import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.comment.CommentServ;
import com.rh.core.util.DateUtils;

/**
 * 我的会议统计
 * @author hdy
 *
 */
public class MyMeetingCount extends CommentServ {
    
    /* 我的会议统计服务ID*/
    private final String oaMtMtMeetingCount = "OA_MT_MYMEETING_COUNT";
    
    @Override
    protected void beforeQuery(ParamBean paramBean) {
        //若是[OA_MT_MYMEETING_COUNT]
        if (paramBean.getServId().equals(oaMtMtMeetingCount)) {
            getMyMtList(paramBean);
        } 
    }
    
    
    @Override
    protected void afterQuery(ParamBean paramBean, OutBean outBean) {
        //若是[OA_MT_MYMEETING_COUNT]
        if (paramBean.getServId().equals(oaMtMtMeetingCount)) {
            //获取当前时间 yyyy-MM-dd HH:mm
            //String date = "2013-01-24 22:06";
            String date = DateUtils.getCustomDateTime(DateUtils.FORMAT_DATETIME_HM);
            //获取数据对象集合
            List<Bean> outBeanList = outBean.getDataList();
            for (Bean b : outBeanList) {
                b.set("STATUS", getStatus(date, b.getStr("BEGIN_TIME"), b.getStr("END_TIME")));
            }
        }
        
    }
    
    
    /**
     * 获取[我的回会议]List
     * @author hdy
     * @param paramBean 参数
     * @return outBean
     */
    private ParamBean getMyMtList(ParamBean paramBean) {
        String date = "";
        //在[与会人员记录表]中找到属于自己的会议ids
        String childWhere = "(select MEETING_ID from OA_MT_CONFEREE where USER_CODE = '@USER_CODE@')";
        //如果是查看本周的会议
        if (!paramBean.getStr("WEEK").isEmpty()) {
            //获取本周的星期一日期 yyyy-MM-dd
            date = DateUtils.getFirstDateOfWeek();
            //获取本周周日的日期 yyyy-MM-dd
            String lastDate = DateUtils.getDateAdded(6, date);
            //查询语句的where条件
            String where = " and ((substr(BEGIN_TIME,1,10) > '" + date + "' and substr(BEGIN_TIME,1,10) < '"
                    + lastDate + "') or (substr(BEGIN_TIME,1,10) <= '" + date + "' and substr(END_TIME,1,10) >= '" 
                    + date + "')) and S_FLAG = '1' and MEETING_ID in " + childWhere;
            paramBean.setQueryExtWhere(where).setShowNum(5); //设置查询记录数
        //如果是查看本月的会议
        } else if (!paramBean.getStr("MONTH").isEmpty()) {
            //获取当前年月yyyy-MM
            date = DateUtils.getYearMonth();
            paramBean.setQueryExtWhere(" and substr(BEGIN_TIME,1,7) <= '" + date + "' and substr(END_TIME,1,7) >= '"
                    + date + "' and S_FLAG = '1' and MEETING_ID in " + childWhere);
            paramBean.setShowNum(5);
        //查询当前日期
        } else if (!paramBean.getStr("TODAY").isEmpty()) {
            //获取日期字符串 yyyy-MM-dd
            //date = "2013-01-24";
            date = DateUtils.getDate();
            paramBean.setQueryExtWhere(" and substr(BEGIN_TIME,1,10) <= '" + date + "' and substr(END_TIME,1,10) >= '"
                    + date + "' and S_FLAG = '1' and MEETING_ID in " + childWhere);
            paramBean.setShowNum(5);
        //没有条件，默认查询我所有会议
        } else {
            paramBean.setQueryExtWhere(" and S_FLAG = '1' and MEETING_ID in " + childWhere);
        }
        paramBean.setOrder("BEGIN_TIME"); //设置排序字段
        return paramBean;
    }

    
    /**
     * 重置当前会议进行状态 
     * @param date 参数
     * @param startDate 参数
     * @param endDate 参数
     * @return 某个会议进行状态
     */
    private String getStatus(String date, String startDate, String endDate) {
        long dateVal = DateUtils.getDateFromString(date, "yyyy-MM-dd HH:mm").getTime();
        long startVal = DateUtils.getDateFromString(startDate, "yyyy-MM-dd HH:mm").getTime();
        long endVal = DateUtils.getDateFromString(endDate, "yyyy-MM-dd HH:mm").getTime();
        if (dateVal < startVal) { //查询时间在数据开始时间之前
            return "未开始";
        } else if (dateVal > endVal) {
            return "已结束";
        }
       return "进行中";
    }  
}
