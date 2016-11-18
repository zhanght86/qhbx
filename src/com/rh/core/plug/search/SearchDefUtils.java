package com.rh.core.plug.search;

import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServDefBean;
import com.rh.core.serv.ServMgr;
import com.rh.core.serv.bean.SqlBean;

/**
 * 
 * @author yangjy
 * 
 */
public class SearchDefUtils {

    /**
     * 
     * @param searchId 搜索定义ID
     * @return 搜索定义Bean
     */
    public static Bean getSearchDefBean(String searchId) {
        return ServDao.find(ServMgr.SY_SERV_SEARCH, searchId, true);
    }

    /**
     * 获取搜索定义信息，如果同一个服务被定义多次，则取列表的第一条。
     * @param servDefBean 服务定义Bean
     * @return 全文检索定义信息，包含关联检索设定
     */
    public static Bean getFirstSearchDefBean(ServDefBean servDefBean) {
        SqlBean sql = new SqlBean();
        sql.and("SERV_ID", servDefBean.getSrcId());
        sql.orders("SORT_NUM asc");

        List<Bean> list = ServDao.finds(ServMgr.SY_SERV_SEARCH, sql);
        if (list.size() > 0) {
            return list.get(0);
        }

        sql.clears().and("SERV_ID", servDefBean.getId());

        list = ServDao.finds(ServMgr.SY_SERV_SEARCH, sql);
        if (list.size() > 0) {
            return list.get(0);
        }

        return null;
    }
}
