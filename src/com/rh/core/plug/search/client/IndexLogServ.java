package com.rh.core.plug.search.client;

import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServDefBean;
import com.rh.core.serv.ServMgr;
import com.rh.core.serv.ServUtils;
import com.rh.core.serv.bean.SqlBean;

/**
 * 
 * @author yangjy
 * 
 */
public class IndexLogServ extends CommonServ {

    /**
     * 
     * @param param 参数Bean
     * @return 执行结果
     */
    public OutBean reindex(ParamBean param) {
        OutBean out = new OutBean();
        String ids = param.getId();
        SqlBean sql = new SqlBean();
        sql.andIn("LOG_ID", ids.split(","));
        List<Bean> logList = ServDao.finds(IndexLogMgr.SY_PLUG_SEARCH_INDEX_LOG, sql);
        for (Bean log : logList) {
            Bean searchDef = ServDao.find(ServMgr.SY_SERV_SEARCH, log.getStr("SEARCH_ID"));
            ServDefBean sdb = ServUtils.getServDef(log.getStr("SERV_ID"));
            SqlBean indexSql = new SqlBean();
            indexSql.and(sdb.getPKey(), log.getStr("DATA_ID"));
            ServUtils.doIndex(searchDef, 1000, indexSql, true);
        }
        out.setOk();
        return out;
    }
}
