package com.rh.oa.mt;

import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServMgr;
import com.rh.core.serv.dict.DictMgr;
import com.rh.core.util.Constant;
import com.rh.core.util.DateUtils;

/**
 * 
 * @author wangchen
 * 
 */

public class ConfereeServ extends CommonServ {
    
    /**一天的毫秒量*/
    private static final long DAY_IN_MILLSECONDS = 86400000;
    
    @Override
    protected void beforeQuery(ParamBean paramBean) {
        if (paramBean.getStr("_extWhere").isEmpty()) {
            StringBuffer whereStr = new StringBuffer();
            whereStr.append(" and USER_CODE='@USER_CODE@'");
            whereStr.append(" and S_FLAG=1");
            whereStr.append(" and MEETING_ID in ");
            whereStr.append(" (");
            whereStr.append(" select MEETING_ID from OA_MT_MEETING where substr(END_TIME,1,16) > '");
            whereStr.append(DateUtils.getDateTimeHm());
            whereStr.append("' )");
            whereStr.append(" order by S_ATIME DESC");
            paramBean.set("_searchWhere", whereStr.toString());
        }
    }

    @Override
    protected void afterQuery(ParamBean paramBean, OutBean outBean) {
        setConfereeDisplayInfo(outBean);
    }

    /**
     * 拼装数据到前台展示bean中（后台数据库利用了四个空字段）
     * 
     * @param outBean
     *            分页查询后的数据结果bean
     * */
    public void setConfereeDisplayInfo(Bean outBean) {
        List<Bean> confereeList = outBean.getList(Constant.RTN_DATA);
        for (Bean confereeBean : confereeList) {
            ParamBean param = new ParamBean("OA_MT_MEETING", ServMgr.ACT_BYID, confereeBean.getStr("MEETING_ID"));
            OutBean meetingBean = ServMgr.act(param);
            // 四个空字段赋值
            confereeBean.set("MEETING_TITLE_V", meetingBean.getStr("TITLE"));
            confereeBean.set("MEETING_PLACE_V", DictMgr.getFullName("OA_MT_MEETINGROOM_DIC",
                        meetingBean.getStr("PLACE")));
            confereeBean.set("MEETING_START_TIME_V", meetingBean
                    .getStr("BEGIN_TIME"));
            confereeBean.set("MEETING_END_TIME_V", meetingBean
                    .getStr("END_TIME"));
            
            //是否需要关注标识
            String warnFlag = "";
            if (DateUtils.getDiffTime(DateUtils.getDateTimeHm(), meetingBean
                    .getStr("BEGIN_TIME"), DateUtils.FORMAT_DATETIME_HM) <= DAY_IN_MILLSECONDS
                    && DateUtils.getDiffTime(DateUtils.getDateTimeHm(),
                            meetingBean.getStr("END_TIME"),
                            DateUtils.FORMAT_DATETIME_HM) > 0) {
                warnFlag = Constant.YES;
            } else {
                warnFlag = Constant.NO;
            }
            confereeBean.set("warnFlag", warnFlag);
        }
    }
}
