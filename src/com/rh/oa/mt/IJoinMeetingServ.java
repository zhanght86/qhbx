package com.rh.oa.mt;

import java.util.Date;

import com.rh.core.serv.ParamBean;
import com.rh.core.serv.comment.CommentServ;
import com.rh.core.util.DateUtils;

/**
 * 我待参加的会议
 * @author hdy
 *
 */
public class IJoinMeetingServ extends CommentServ {
    
    /**待我参加的会议*/
    private final String joining = "JOINING";
    /**我已参加的会议*/
    private final String joined = "JOINED";
    /**删除标志，1标示未删除*/
    private final String sFlag = "1";
    /**回执单 确定参加会议*/
    private final String returnStatus = "1";
    /**会议审批通过的*/
    private final String status = "2";
    
    @Override
    protected void beforeQuery(ParamBean paramBean) {
       //过滤出我要参加的会议
       String seachDate = DateUtils.getStringFromDate(new Date(), "yyyy-MM-dd HH:mm"); 
       String childWhere = "";
       String seachWhere = "";
       //如果为[我待参加的会议]
       if (paramBean.getStr("JOIN").equals(joining)) {
           childWhere = "(select MEETING_ID from OA_MT_CONFEREE where USER_CODE = '@USER_CODE@' and S_FLAG = '"
                   + sFlag + "' and SET_TIME_OK is not null)";
           seachWhere = " and STATUS = '" + status + "' and MEETING_ID in " + childWhere 
                   + " and substr(END_TIME,1,16) > '" + seachDate + "'";
       //如果为我[已参加的会议]
       } else if (paramBean.getStr("JOIN").equals(joined)) {
           childWhere = "(select MEETING_ID from OA_MT_CONFEREE where USER_CODE = '@USER_CODE@' and S_FLAG = '"
                   + sFlag + "' and RETURN_SATUS = '" + returnStatus + "' and SET_TIME_OK is not null)";
           seachWhere = " and STATUS = '" + status + "' and MEETING_ID in " + childWhere 
                   + " and substr(END_TIME,1,16) < '" + seachDate + "'"; 
       }
       paramBean.set("_extWhere", seachWhere + " and S_FLAG = '" + sFlag + "'");
    }
}
