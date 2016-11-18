package com.rh.core.comm.todo;

import java.util.List;
import org.apache.commons.lang.time.DurationFormatUtils;
import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.comm.workday.WorkTime;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.bean.SqlBean;
import com.rh.core.util.Constant;
import com.rh.core.util.DateUtils;
import com.rh.core.util.Strings;

/**
 * 代办服务类
 * 
 * @author Kevin Liu
 * 
 */
public class TodoHisServ extends TodoServ {
    
    /**
     * 查询前添加查询条件
     * 
     * @param paramBean 
     */
    public void beforeQuery(ParamBean paramBean) { 
       super.beforeQuery(paramBean);
    }
    
    /**
     * 查询后添加查询条件
     * 
     * @param paramBean 
     * @param outBean 查询结果
     */
    public void afterQuery(ParamBean paramBean, OutBean outBean) {
        List<Bean> dataList = outBean.getList(Constant.RTN_DATA);
        String dataIdStr = "";
        
        if (dataList.size() > 0) {
            for (Bean data : dataList) {
		//超时时间的处理
                WorkTime workTime = new WorkTime();
                String sendTime = data.getStr("TODO_SEND_TIME"); // 发送时间
                String deadLine = data.getStr("TODO_DEADLINE1"); // 规定期限
                if (sendTime.length() > 0 && deadLine.length() > 0) {
                    String currTime = DateUtils.getDatetime(); // 当前时间
                    //long stdWasteTime = workTime.calWorktime(Context.getCmpy(), sendTime, deadLine); // 规定耗时
                    long realWasteTime = workTime.calWorktime(Context.getCmpy(), sendTime, currTime); // 实际耗时
                    data.set("TODO_WASTETIME_S", DurationFormatUtils.formatDuration(realWasteTime * 60000,
                            "dd'天'HH'小时'mm'分钟'")); // 节点耗时
                    long overTimeByNorm = DateUtils.getDiffTime(deadLine, currTime); // 按正常时算
                    // long overTimeByWork = realWasteTime - stdWasteTime; //按工作时算
                    if (overTimeByNorm > 0) {
                        data.set("TODO_OVERTIME_S", DurationFormatUtils.formatDuration(overTimeByNorm,
                                "'超时'dd'天'HH'小时'mm'分钟'")); // 超出时间
                        data.set("overTimeFlag", "true"); // 超时标识，展示时用
                    } else {
                        data.set("TODO_OVERTIME_S", DurationFormatUtils.formatDuration(Math.abs(overTimeByNorm),
                                "'剩余'dd'天'HH'小时'mm'分钟'")); // 剩余时间
                    }
                }
				
                // 流程节点状态信息的处理
                if (data.isNotEmpty("TODO_OBJECT_ID1")) {
                    dataIdStr = Strings.addValue(dataIdStr, data.getStr("TODO_OBJECT_ID1"));
                }
            }
            
            // 查询ENTITY表的S_WF_USER_STATE字段
            Bean entityBean = null;
            if (!dataIdStr.isEmpty()) {
                String queryOdeptCode = Context.getUserBean().getODeptCode();
                SqlBean sql = new SqlBean();
                sql.and("S_FLAG", Constant.YES_INT);
                sql.and("QUERY_ODEPT", queryOdeptCode);
                sql.andIn("DATA_ID", dataIdStr.split(Constant.SEPARATOR));
                sql.selects("DATA_ID, S_WF_USER_STATE, S_WF_STATE");
                List<Bean> entityList = ServDao.finds("SY_COMM_ENTITY", sql);
                if (entityList.size() > 0) {
                    entityBean = new Bean();
                    for (Bean res : entityList) {
                        entityBean.set(res.getStr("DATA_ID"), res);
                    }
                }
            }

            // 连接ENTITY表的S_WF_USER_STATE字段
            for (Bean data : dataList) {
                String dataId = data.getStr("TODO_OBJECT_ID1");
                String wfUserState = "";
                int wfState = 1;
                if (!dataId.isEmpty()) {
                    if (entityBean != null && entityBean.contains(dataId)) {
                        Bean res = entityBean.getBean(dataId);
                        if (res != null) {
                            wfUserState = res.getStr("S_WF_USER_STATE");
                            wfState = res.getInt("S_WF_STATE");
                        }
                    }
                }
                data.set("S_WF_USER_STATE", wfUserState);
                data.set("S_WF_STATE", wfState);
            }
            entityBean = null;
        }        
    }
}
