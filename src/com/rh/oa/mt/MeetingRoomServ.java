package com.rh.oa.mt;

import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.dict.DictMgr;
import com.rh.core.util.Constant;
import com.rh.oa.mt.util.MeetingRoomConstant;

/**
 * 会议室查询动态列表服务
 * @author hdy
 *
 */

public class MeetingRoomServ  extends CommonServ {
	
	@Override
    protected void afterQuery(ParamBean paramBean, OutBean outBean) {
	    List<Bean> dicList = DictMgr.getItemList(MeetingRoomConstant.OA_MT_MEETINGROOM_MR_TYPE_DIC_CODE);  //会议室类型分类
	    
	    List<Bean> floorList = outBean.getList(Constant.RTN_DATA);
	    
        for (Bean floor : floorList) {
            if (floor.isNotEmpty("IMAGE")) {
                String imgName = floor.getStr("IMAGE");
                imgName = imgName.substring(0, imgName.indexOf(","));
                floor.set("IMAGE", imgName);
            }
            
            StringBuffer sb = new StringBuffer(); // 追加记录会议室备注信息

            int meetingCount = ServDao.count(MeetingRoomConstant.OA_MT_MEETINGROOM_CODE,
                    new Bean().set("FLOOR_ID", floor.getStr("FLOOR_ID"))); // 拿到某一楼层会议室的List
            sb.append(floor.getStr("NAME") + "会议室共有" + meetingCount + "个，其中有"); // 拼接备注信息
            for (Bean dic : dicList) {
                List<Bean> meetingRoomList = ServDao.finds(MeetingRoomConstant.OA_MT_MEETINGROOM_CODE,
                        new Bean().set("FLOOR_ID", floor.getStr("FLOOR_ID")).set("MR_TYPE", dic.getStr("ITEM_CODE")));
                sb.append(meetingRoomList.size() + "个" + dic.getStr("ITEM_NAME") + "，");
            }
            //覆盖备注信息
            if (sb.toString().endsWith("，")) {
                floor.set("MEMO", sb.substring(0, sb.lastIndexOf("，")) + "。");
            }
        }
    }
}
