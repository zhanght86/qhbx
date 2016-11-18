package com.rh.bn.sync.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rh.core.base.Bean;
import com.rh.core.serv.ServDao;

/**
 * 百年人寿项目机构数据缓存类，主要用来过滤同步的部门、用户信息
 * @author Tanyh 20151013
 *
 */
public class BnOrgCacheUtil {

    private static Map<String, String> orgMap = null;
    
    /**
     * 判断是否存在该机构
     * @param orgCode 机构编码
     * @return
     */
    public static boolean containsOrg(String orgCode) {
        if (orgMap == null) {
            orgMap = setUpOrgMap();
        }
        if (orgMap.containsKey(orgCode)) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * 初始化机构缓存
     * @return
     */
    private static Map<String, String> setUpOrgMap() {
        Map<String, String> orgData = new HashMap<String, String>();
        //获取机构信息
        List<Bean> orgList = ServDao.finds("SY_ORG_DEPT", " and DEPT_TYPE=2 and S_FLAG=1 ");
        if (orgList != null) {
            for (Bean org : orgList) {
                orgData.put(org.getStr("DEPT_CODE"), org.getStr("DEPT_CODE"));
            }
        }
        return orgData;
    }
    /**
     * 更新机构缓存
     * @param orgCode
     */
    public static void updateOrgMap(String orgCode) {
        if (orgMap == null) {
            orgMap = setUpOrgMap();
        }
        orgMap.put(orgCode, orgCode);
    }
}
