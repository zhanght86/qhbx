package com.rh.oa.cd;

import com.rh.core.base.Bean;
import com.rh.oa.cd.util.CdUtils;

/**
 * 督办的后台线程，处理自动起草督办单
 * @author cuihf
 *
 */
public class SupeJob extends AbstractCdJob {

    /**
     * 获取催督办类型
     * @return 催办类型
     */
    protected int getAutoType() {
        return CdUtils.TYPE_AUTO_SUPE;
    }

    /**
     * 创建督办单
     * @param autoBean 自动督办的配置信息
     * @param dataBean 相关信息
     * 
     */
    protected void create(Bean autoBean, Bean dataBean) {
        //TODO 待实现
        /**
         * 起草督办单
         */
        /**
         * 驱动流程，送给待督办人
         */
        /**
         * 关联督办单与文件信息
         */
    }

    
    public void interrupt() {
        // TODO Auto-generated method stub
        
    }

}
