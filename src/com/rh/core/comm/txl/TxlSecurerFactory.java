package com.rh.core.comm.txl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rh.core.base.Context;
import com.rh.core.util.lang.Assert;
import com.rh.core.util.lang.ObjectCreator;

/**
 * 通讯录密级控制器工厂，生成通讯录密级控制器接口对象。
 * @author ZJW
 *
 */
public class TxlSecurerFactory {
    
    /** log */
    private static Log log = LogFactory.getLog(TxlSecurerFactory.class);

    /**
     * @param ctrClsName 配置中的实现类类名
     * @return 通讯录控制对应的实现类
     */
    public static TxlSecurer getTxlSender(String ctrClsName) {
        Assert.notNull(ctrClsName);
        String clsName = Context.getSyConf(ctrClsName, "");
        if (clsName.length() == 0) {
            log.error(Context.getSyMsg("SY_COMM_ADDD_CLASS_ERROR", ctrClsName));
            return null;
        }

        return (TxlSecurer) ObjectCreator.create(TxlSecurer.class, clsName);
    }
}
