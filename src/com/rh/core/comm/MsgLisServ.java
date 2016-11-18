package com.rh.core.comm;

import java.io.Serializable;

import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.util.msg.MsgCenter;

/**
 * 消息监听服务
 * @author wanghg
 */
public class MsgLisServ extends CommonServ implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 5639008740980250026L;

    /**
     * 修改后重新加载监听
     * @param paramBean 参数Bean
     * @param outBean 输出信息
     */
    protected void afterSave(ParamBean paramBean, OutBean outBean) {
        if (outBean.isOk()) {
            MsgCenter.getInstance().init();
        }
    }

    /**
     * 修改后重新加载监听
     * @param paramBean 参数Bean
     * @param outBean 输出信息
     */
    protected void afterDelete(ParamBean paramBean, OutBean outBean) {
        MsgCenter.getInstance().init();
    }
}
