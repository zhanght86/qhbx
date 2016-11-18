package com.rh.oa.cd.util;

import com.rh.core.base.Bean;
import com.rh.oa.cd.AutoBean;

/**
 * 督办帮助类
 * @author cuihf
 *
 */
public class SupeUtils {
    /**
     * 增加自动督办信息
     * @param autoBean 自动督办信息
     * @return 自动督办信息
     */
    public static final Bean add(AutoBean autoBean) {
        return CdUtils.add(autoBean, CdUtils.TYPE_AUTO_SUPE);
    }
    
    /**
     * 结束自动督办
     * @param autoBean 自动督办信息
     * @return 自动督办信息
     */
    public static final Bean finish(AutoBean autoBean) {
        return CdUtils.finish(autoBean, CdUtils.TYPE_AUTO_SUPE);
    }
    
}
