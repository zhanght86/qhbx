package com.rh.oa.gw.util;

import com.rh.core.base.Context;
import com.rh.core.org.UserBean;

/**
 * 
 * @author yangjy
 * 
 */
public class GwRightUtils {
    /** 系统配置：公司公文文件管理员角色 **/
    private static final String ROLE_ADMIN = "OA_GW_GW_ROLE_ADMIN";

    /** 系统配置：部门公文文件管理员角色 **/
    private static final String ROLE_DEPT_ADMIN = "OA_GW_GW_ROLE_DEPT_ADMIN";

    /**
     * @param userBean 用户
     * @return 指定用户是否是公司文件管理员
     */
    public boolean isAdmin(UserBean userBean) {
        String roleCode = getAdminRole();
        return userBean.existInRole(roleCode);
    }

    /**
     * 
     * @param userBean 用户
     * @return 指定用户是否是公司文件管理员
     */
    public static boolean isDeptAdmin(UserBean userBean) {
        String roleCode = getDeptAdminRole();
        return userBean.existInRole(roleCode);
    }

    /**
     * 
     * @return 公司文件管理员角色名称
     */
    public static String getAdminRole() {
        return Context.getSyConf(ROLE_ADMIN, "");
    }

    /**
     * 
     * @return 部门文件管理员角色名
     */
    public static String getDeptAdminRole() {
        return Context.getSyConf(ROLE_DEPT_ADMIN, "");
    }
}
