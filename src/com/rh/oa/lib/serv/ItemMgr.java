package com.rh.oa.lib.serv;

import com.rh.core.base.Bean;
import com.rh.core.serv.ServDao;

/**
 * 
 * @author yangjy
 *
 */
public class ItemMgr {
    private static final String SERV_ID = "OA_LIB_ITEM";
    


    /**
     * 
     * @param dataId 主键
     * @return 根据主键返回查询记录
     */
    public static Bean find(String dataId) {
        return ServDao.find(SERV_ID, dataId);
    }
}
