package com.rh.core.comm.todo;

import java.util.List;

import org.apache.commons.lang.time.DurationFormatUtils;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.base.TipException;
import com.rh.core.comm.entity.StateBase;
import com.rh.core.comm.workday.WorkTime;
import com.rh.core.org.UserBean;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDefBean;
import com.rh.core.serv.ServMgr;
import com.rh.core.serv.ServUtils;
import com.rh.core.util.Constant;
import com.rh.core.util.DateUtils;

/**
 * 代办服务类
 * 
 * @author Kevin Liu
 * 
 */
public class TodoServ extends StateBase {
    /** 主委托服务 */
    private static final String AGENT_MAIN_SERV = "SY_ORG_USER_TYPE_AGENT";
    
    /**
     * 查询前添加查询条件
     * 
     * @param paramBean 
     */
    public void beforeQuery(ParamBean paramBean) { 
        StringBuilder strWhere = new StringBuilder();
        //查询委托
        if (paramBean.containsKey("agentFlag") && paramBean.getBoolean("agentFlag")) {
            String servCode = paramBean.getServId();
            //指定待办类型
            OutBean agtWhereBean = ServMgr.act(AGENT_MAIN_SERV, "getTodoAgentWhereByUserCode", paramBean);
            if (!agtWhereBean.isOk()) {
                throw new TipException("获取委办记录失败");
            }
            strWhere.append(agtWhereBean.getData());
            paramBean.setServId(servCode);
        //查询本人
        } else {
            String currUserCode = Context.getUserBean().getCode();
            //指定本人
            strWhere.append(" and OWNER_CODE = '" + currUserCode + "'");
        }
        strWhere.append(" and TODO_CATALOG in (1 , 3)");
        
        String extWhere = paramBean.getStr("_extWhere");
        if (extWhere.startsWith("{")) {
            strWhere.append(getTodoCodeWhere(paramBean)); // 指定待办类型
            extWhere = strWhere.toString();
        } else {
            extWhere = extWhere + strWhere.toString();
        }    
        paramBean.set("_extWhere", extWhere);
    }
    
    /**
     * 查询后添加查询条件
     * 
     * @param paramBean 
     * @param outBean 查询结果
     */
    public void afterQuery(ParamBean paramBean, OutBean outBean) {
        List<Bean> dataList = outBean.getList(Constant.RTN_DATA);
        if (dataList.size() > 0) {
            for (Bean data : dataList) {
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
            }
        }
    }

    /**
	 * 查看首页代办时，对含内容的代办查看完毕后转成已办操作
	 * 
	 * @param paramBean
	 *            传入的参数Bean
	 * @return outBean 返回的Bean
	 */
	public OutBean endReadCon(ParamBean paramBean) {
		int res = TodoUtils.endById(paramBean);
		OutBean outBean = new OutBean();
		if (res == 1) {
			outBean.setOk(Context.getSyMsg("SY_ADD_OK"));
		} else {
		    outBean.setError(Context.getSyMsg("SY_ADD_ERROR"));
		}
		return outBean;
	}

	/**
	 * 取当前用户的待办列表以及分组合计待办数量
	 * @param paramBean  参数信息（不需要参数信息）
	 * @return 待办的数量及内容
	 */
	public OutBean getTodo(ParamBean paramBean) {
		UserBean userBean = Context.getUserBean();
		if (userBean == null) {
		    return new OutBean(); //没有用户信息则直接返回
		}
		ServDefBean servDef = ServUtils.getServDef(paramBean.getServId());
		ParamBean listParamBean = new ParamBean(paramBean.getServId());
		listParamBean.setSelect(" TODO_TITLE,TODO_URL,TODO_CODE ,DEPT_CODE,TODO_CODE_NAME,"
				+ "SEND_USER_CODE,TODO_SEND_TIME,S_EMERGENCY,TODO_OBJECT_ID1");
	    listParamBean.setShowNum(paramBean.get("rownum", servDef.getPageCount()));  //最多返回服务设定的条数
		listParamBean.setQueryPageOrder("TODO_SEND_TIME DESC");
		return query(listParamBean);
	}
	
	/**
     * 取当指定用户或者当前用户的待办数量
     * @param paramBean  参数信息，不需要参数，自动获取当前用户的记录数
     * @return 待办的数量
     */
    public OutBean getTodoCount(ParamBean paramBean) {
        OutBean outBean = new OutBean();
        UserBean userBean = Context.getUserBean();
        if (userBean != null) {
            outBean.setData(TodoUtils.getToDoCountBean(userBean.getCode(), paramBean.getStr("I_MENU_IDS")));
        }
        return outBean;
    }
    /**
     * 取当指定用户或者当前用户的待办数量
     * @param paramBean  参数信息，不需要参数，自动获取当前用户的记录数
     * @return 待办的数量
     */
    public OutBean getTodoCountMb(ParamBean paramBean) {
        OutBean outBean = new OutBean();
        UserBean userBean = Context.getUserBean();
        if (userBean != null) {
            outBean.setData(TodoUtils.getToDoCountBeanMb(userBean.getCode()));
        }
        return outBean;
    }
    
    /**
     * 
     * @param paramBean 待办的参数Bean
     * @return 处理结果，新增待办的详细信息，包含主键。
     */
    public OutBean addToDo(ParamBean paramBean) {
        return new OutBean(TodoUtils.insert(new TodoBean(paramBean))).setOk();
    }
    
    /**
     * 按待办主键结束待办
     * @param paramBean 包含待办ID（_PK_）值的paramBean
     * @return 处理结果。操作成功则_MSG_="OK"。
     */
    public OutBean endToDo(ParamBean paramBean) {
        TodoUtils.endById(paramBean);
        OutBean outBean = new OutBean();
        outBean.setOk();
        return outBean;
    }
    
    /**
     * 按待办主键删除待办
     * @param paramBean 包含待办ID（_PK_）值的paramBean
     * @return 处理结果。操作成功则_MSG_="OK"。
     */
    public OutBean destroyToDo(ParamBean paramBean) {
        OutBean outBean = new OutBean();
        TodoUtils.destroyById(paramBean);
        outBean.setOk();
        return outBean;
    }
}
