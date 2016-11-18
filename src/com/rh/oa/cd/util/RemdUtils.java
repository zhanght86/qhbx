package com.rh.oa.cd.util;

import com.rh.core.base.Bean;
import com.rh.oa.cd.AutoBean;

/**
 * 催办帮助类
 * 
 * @author cuihf
 *
 */
public class RemdUtils {
    
    /**
     * 增加自动催办信息
     * @param autoBean 自动催办信息
     * @return 自动催办信息
     */
    public static final Bean add(AutoBean autoBean) {
        return CdUtils.add(autoBean, CdUtils.TYPE_AUTO_REMD);
    }
    
    /**
     * 结束自动催办
     * @param autoBean 自动催办信息
     * @return 自动催办信息
     */
    public static final Bean finish(AutoBean autoBean) {
        return CdUtils.finish(autoBean, CdUtils.TYPE_AUTO_REMD);
    }
    
}
