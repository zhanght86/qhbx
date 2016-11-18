package com.rh.bnpt.serv;

import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServMgr;

/**
 * 加班申请
 * @author jason
 *
 */
public class ScExtraLeaveApplyServ extends CommonServ {

	/** 服务主键：加班申请 */
    private static final String SERV_ID = "SC_EXTRA_WORK_APPLY";
    /** 加班类型：双休 2 */
    private static final String APPLY_TYPE_WEEKENDS = "2";
	/**
     * 办结之后将日期插入  日程中
     * @param paramBean 入参
     */
    public void afterFinish(ParamBean paramBean) {
        
       String processId = paramBean.getStr("PI_ID");
       ParamBean query = new ParamBean(SERV_ID, ServMgr.ACT_FINDS);
                 query.set("S_WF_INST", processId);
       OutBean outBean = ServMgr.act(query);
       List<Bean> list = outBean.getDataList();
       if (list.size() > 0) {
           Bean bean = list.get(0);
           String bTime     = bean.getStr("APPLY_B_TIME");
           String eTime     = bean.getStr("APPLY_E_TIME");
           String applyUser = bean.getStr("APPLY_USER");
           String applyType = bean.getStr("APPLY_TYPE"); //加班类型
           String exbTime   = bean.getStr("EXCHANGE_B_TIME");
           String exeTime   = bean.getStr("EXCHANGE_E_TIME");
//加班不需要添加到日程管理中 2013-08-15 20:09        
           //TODO 插入个人日程中，并设置日程公开方式
//           Bean dataBean = new Bean();
//                dataBean.set("CAL_TITLE", "加班");         //标题
//                dataBean.set("CAL_TYPE", 2);			   //日程类型 工作任务
//                dataBean.set("CAL_START_TIME", bTime);     //开始时间
//                dataBean.set("CAL_END_TIME", eTime);       //结束时间
//                dataBean.set("CAL_CONTENT", "加班");       //日程内容
//                dataBean.set("DONE_USERS", applyUser);     //所属用户
//                dataBean.set("S_USER", applyUser);         //所属用户
//           ServDao.save("SY_COMM_CAL", dataBean);
//           if (applyType.equals(APPLY_TYPE_WEEKENDS)) { 	//倒休
//        	   dataBean.set("CAL_TYPE", 3);			   		//日程类型 其它
//               dataBean.set("CAL_START_TIME", exbTime);     //开始时间
//               dataBean.set("CAL_END_TIME", exeTime);       //结束时间
//               dataBean.set("CAL_CONTENT", "倒休");         //日程内容
//               ServDao.save("SY_COMM_CAL", dataBean);
//           }
           //TODO 发送至HR系统
       }
    }
	

}
