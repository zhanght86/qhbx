package com.rh.core.comm.news.serv;

import com.rh.core.serv.ServDao;
import com.rh.core.serv.bean.SqlBean;

/**
 * 信息权限
 * @author zhl
 * 
 */
public class NewsAclMgr {

    /**
     * 信息权限服务
     */
     public static final String SY_COMM_NEWS_ACL = "SY_COMM_NEWS_ACL";
     /**
      * 本系统
      */
     public static final int ACL_TYPE_SCOPE = 1;
     /**
      * 公共角色
      */
     public static final String RPUB = "RPUB";
   
    /**
     * 清除已有的数据
     * @param newsId 信息主键
     * @param aclType 授权类型
     */
    public static void delAclByType(String newsId , int aclType) {
        SqlBean delAclSql = new SqlBean();
        delAclSql.set("DATA_ID", newsId);
        delAclSql.set("ACL_TYPE", aclType);
        ServDao.deletes(SY_COMM_NEWS_ACL, delAclSql);
    }

}
