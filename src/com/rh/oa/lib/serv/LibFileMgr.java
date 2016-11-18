package com.rh.oa.lib.serv;

import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.bean.SqlBean;

/**
 * 
 * @author yangjy
 * 
 */
public class LibFileMgr {
    private static final String SERV_ID = "OA_LIB_FILE";

    /**
     * 
     * @param itemId 文库记录
     * @return 取得文件列表
     */
    public static List<Bean> getFileList(String itemId) {
        SqlBean query = new SqlBean();
        query.set("ITEM_ID", itemId);

        return ServDao.finds(SERV_ID, query);
    }
}
