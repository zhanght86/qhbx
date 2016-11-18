package com.rh.oa.mt;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.org.UserBean;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.bean.SqlBean;
import com.rh.core.util.Constant;
import com.rh.core.util.DateUtils;
import com.rh.core.util.Lang;

/**
 * 我的会议通知
 * @author ruaho_hdy
 *
 */
public class ReturnNoticeServ extends CommonServ {
    
    /**我参加的会议通知信息*/
    private List<Bean> myConfereeList = new ArrayList<Bean>();
    
    /**我的会议待办显示*/
    private static final String OA_MT_MEETING_LIST = "OA_MT_MEETING_LIST";
    /**审批通过*/
    private static final String STATUS_PASS = "2";
    /**已回执*/
    private static final String RETURN_SATUS_PASS = "1";
    /**派他人*/
    private static final String RETURN_SATUS_SENDOTHER = "2";
    /**未回执*/
    private static final String RETURN_SATUS_NO = "0";
    
    @Override
    protected void beforeQuery(ParamBean paramBean) {
        if (paramBean.getServId().equals(OA_MT_MEETING_LIST)) {
            String thisDateTime = DateUtils.getDateTimeHm();
            paramBean.set("_extWhere", " and STATUS = '" + STATUS_PASS + "' and END_TIME > '" + thisDateTime + "'"
                    + getMyMeetingNoticeSql());
        } else {
            paramBean.set("_extWhere", getMyMeetingNoticeSql());
        }
    }
    
    @Override
    protected void afterQuery(ParamBean paramBean, OutBean outBean) {
        List<Bean> outList = outBean.getDataList();
        for (Bean b : outList) {
            for (Bean c : myConfereeList) {
                if (b.getId().equals(c.getStr("MEETING_ID"))) {
                    b.set("RETURN_SATUS", c.getStr("RETURN_SATUS"));
                }
                b.set("SHOW_FLAG", showListFlag(b.getStr("STATUS"), b.getStr("RETURN_SATUS")));
            }
        }
    }
    
    /**
     * 显示状态文字
     * @param status 审批状态
     * @param noticeFlag 是否发送通知
     * @return 状态文字
     */
    private String showListFlag(String status, String noticeFlag) {
        //审批通过
        if (status.equals(STATUS_PASS)) {
            //发送通知
            if (noticeFlag.equals(RETURN_SATUS_PASS)) {
                return "已回执";
            } else if (noticeFlag.equals(RETURN_SATUS_SENDOTHER)) {
                return "派他人";
            } else if (noticeFlag.equals(RETURN_SATUS_NO)) {
                return "未回执";
            }
        }
        return "已取消";
    }
    
    @Override
    protected void afterByid(ParamBean paramBean, OutBean outBean) {
        //存在委托用户
        String agentUserCode = paramBean.getStr(Constant.AGENT_USER);
        if (StringUtils.isNotBlank(agentUserCode)) {
            outBean.set(Constant.AGENT_USER, agentUserCode);
        }
        outBean.set("RETURN_NOTICE_PK", Lang.getUUID());
        super.afterByid(paramBean, outBean);
    }
    
    /**
     * 获取所需sql语句
     * @return 字符串
     */
    private String getMyMeetingNoticeSql() {
        SqlBean myConfereeIds = new SqlBean();
        UserBean user = Context.getUserBean();
        StringBuffer mysqlString = new StringBuffer();
        myConfereeIds.selects("MEETING_ID,RETURN_SATUS"); //.and("USER_CODE", user.getCode())
        myConfereeIds.and("S_FLAG", 1).and("USER_CODE", user.getCode());
        myConfereeList = ServDao.finds("OA_MT_CONFEREE", myConfereeIds);
        mysqlString.append(" and").append(" MEETING_ID").append(" in").append(" (");
        mysqlString.append(getMeetingIds(myConfereeList)).append(") ");
        return mysqlString.toString();
    }
    
    /**
     * 获取我的参加的会议通知code值
     * @param list 我参加的会议铜活字数据
     * @return 我参加的会议code值
     */
    private String getMeetingIds(List<Bean> list) {
        StringBuffer myMeetingIds = new StringBuffer();
        for (Bean b : list) {
            myMeetingIds.append(",").append("'").append(b.getStr("MEETING_ID")).append("'");
        }
        return myMeetingIds.toString().length() > 0 ? myMeetingIds.toString().substring(1) : "''";
    }
}
