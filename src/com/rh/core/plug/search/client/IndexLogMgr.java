package com.rh.core.plug.search.client;

import org.apache.commons.lang.exception.ExceptionUtils;

import com.rh.core.base.Bean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.bean.SqlBean;
import com.rh.core.util.Constant;

/**
 * 增加索引失败记录
 * @author yangjy
 * 
 */
public class IndexLogMgr {
    public static final String SY_PLUG_SEARCH_INDEX_LOG = "SY_PLUG_SEARCH_INDEX_LOG";

    /**
     * @param searchDefine 查询定义Bean
     * @param dataBean 数据Bean
     * @param e 异常
     */
    public static void addIndexLog(Bean searchDefine, Bean dataBean, Throwable e) {
        Bean logBean = new Bean();
        logBean.set("SEARCH_ID", searchDefine.getStr("SEARCH_ID"));
        logBean.set("SERV_ID", searchDefine.getStr("SERV_ID"));
        logBean.set("DATA_ID", dataBean.getId());
        logBean.set("LOG_TYPE", 2); // 索引失败

        String msg = ExceptionUtils.getStackTrace(e);
        if (msg.length() > 600) {
            msg = msg.substring(0, 600);
        }

        logBean.set("LOG_MSG", msg);
        ServDao.save(SY_PLUG_SEARCH_INDEX_LOG, logBean);
    }

    /**
     * 
     * @param servId 服务ID
     * @param dataId 数据ID
     */
    public static void updateStateToOk(String servId, String dataId) {
        SqlBean sql = new SqlBean();
        sql.and("SERV_ID", servId);
        sql.and("DATA_ID", dataId);
        
        ParamBean param = new ParamBean();
        param.set("INDEX_STATE", Constant.YES_INT);
        ServDao.updates(SY_PLUG_SEARCH_INDEX_LOG, param, sql);
    }
}
