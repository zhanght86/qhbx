package com.rh.oa.gw.util;

import com.rh.core.base.Context;
import com.rh.core.util.lang.ObjectCreator;
import com.rh.oa.gw.GwSealProcess;

/**
 * 公文模块常用方法
 * @author yangjy
 * 
 */
public class GwUtils {
    private static final String GW_SEAL_IMPL = "OA_SEAL_IMPL_CLS.gw";

    /**
     * 
     * @return 印章系统实现类
     */
    public static GwSealProcess createGwSeal() {
        String gwSealCls = Context.getSyConf(GW_SEAL_IMPL, "com.rh.oa.zh.seal.GwZhJdSealProcess");
        
        return ObjectCreator.create(GwSealProcess.class, gwSealCls);
    }
}
