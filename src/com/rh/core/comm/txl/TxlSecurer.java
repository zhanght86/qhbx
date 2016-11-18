package com.rh.core.comm.txl;

import com.rh.core.base.Bean;

/**
 * 通讯录保密级别接口
 * @author ZJW
 * 
 */
public interface TxlSecurer {

    /**
     * 获得通讯录信息重组后的User信息
     * @param userBean 查看用户数据载体
     * @return Bean 重组后的UserInfo
     */
    Bean getSecuredUserInfo(Bean userBean);

}
