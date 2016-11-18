package com.rh.oa.comm;

import org.apache.commons.lang.StringUtils;

import com.rh.core.comm.todo.TodoUtils;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;

/**
 * 用于外部系统获取OA系统的待办条数
 * @author chensheng
 *
 */
public class TodoCountServ extends CommonServ {
    
    /**
     * 指定用户获取待办条数
     * @param paramBean 参数
     * @return 返回待办条数
     */
    public OutBean getTodoCount(ParamBean paramBean) {
        OutBean outBean = new OutBean();
        String userCode = paramBean.getStr("userCode");
        if (StringUtils.isNotBlank(userCode)) {
            // 获取待办条数
            outBean.set("TODO_COUNT", TodoUtils.getToDoCountBean(userCode, "").getInt(0));
        } else {
            log.error("参数userCode没有传！");
        }
        return outBean;
    }
    
}
