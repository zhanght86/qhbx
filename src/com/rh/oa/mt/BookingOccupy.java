package com.rh.oa.mt;

import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.comment.CommentServ;
import com.rh.core.util.Constant;
import com.rh.oa.mt.util.MeetingRoomConstant;

/**
 * 会议室预定被占用时预定查看列表详细
 * @author hdy
 * 
 */
public class BookingOccupy extends CommentServ {

    @Override
    protected void beforeQuery(ParamBean paramBean) {
        paramBean.setQueryNoPageFlag(true);
    }
    
    @Override
    protected void afterQuery(ParamBean paramBean, OutBean outBean) {
        List<Bean> outBeanList = outBean.getList(Constant.RTN_DATA);
        for (int i = 0; i < outBeanList.size(); i++) {
            String formitSatrtTime = MeetingRoomConstant.getFormitDate(outBeanList.get(i)
                    .getStr("START_TIME"), "yyyy-MM-dd HH:mm");
            String formitEndTime = MeetingRoomConstant.getFormitDate(outBeanList.get(i)
                    .getStr("END_TIME"), "yyyy-MM-dd HH:mm");
            String formitOccupyTime = formitSatrtTime + "<br/>"
                    + formitEndTime;
            outBeanList.get(i).set("MEMO", formitOccupyTime);
        }
    }
}
