package com.rh.core.plug.search.client;

import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServMgr;

/**
 * 
 * @author yangjy
 * 
 */
public class SearchJobLogMgr {
    private static final String SERV_ID = "SY_PLUG_SEARCH_JOB_LOG";

    /**
     * @param searchId 索引定义ID
     * @param servId 服务ID
     * @param lastData 最后一条数据
     * @param count 新增索引数量
     * @param elapsedTime 花费时间
     * @return 增加数据结果
     */
    public static OutBean addLog(String searchId, String servId, String lastData, int count, long elapsedTime) {
        ParamBean param = new ParamBean();
        param.set("SEARCH_ID", searchId);
        param.set("SERV_ID", servId);
        param.set("LAST_DATA", lastData);
        param.set("IDX_COUNT", count);
        param.set("ELAPSED_TIME", elapsedTime);

        return ServMgr.act(SERV_ID, ServMgr.ACT_SAVE, param);
    }
}
