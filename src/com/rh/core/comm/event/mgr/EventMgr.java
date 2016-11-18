package com.rh.core.comm.event.mgr;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.org.UserBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServMgr;

/**
 * 
 * 用于实现新鲜事
 * 
 */
public class EventMgr {

    /**
     * 保存事件
     * @param group - 事件分组
     * @param servId - 服务
     * @param dataId - 数据项
     * @param action - 操作
     * @param dataDisName - 数据项显示名称
     * @param dataOwner - 数据项作者(目标数据的S_USER)
     */
    public static void save(String group, String servId, String dataId, String action, String dataDisName,
            String dataOwner) {
        // 更新事件
        // String ip = RequestUtils.getIpAddr(Context.getRequest());
        if (null == servId || 0 == servId.length()) {
            new RuntimeException("servId can not be null!");
        }
        if (null == dataId || 0 == dataId.length()) {
            new RuntimeException("dataId can not be null!");
        }
        UserBean user = Context.getUserBean();

        Bean event = new Bean();
        event = new Bean();
        event.set("EVENT_GROUP", group);
        event.set("SERV_ID", servId);
        event.set("DATA_ID", dataId);
        event.set("USER_CODE", user.getCode());
        event.set("ACT_CODE", action);
        event.set("DATA_DIS_NAME", dataDisName);
        if (null != dataOwner && 0 < dataOwner.length()) {
            event.set("DATA_OWNER", dataOwner);
        }
        ServDao.save(ServMgr.SY_COMM_EVENT, event);
    }

}
