package com.rh.core.comm.txl;

import org.apache.log4j.Logger;

import com.rh.core.base.Bean;

/**
 * 通讯录保密级别抽象类
 * @author ZJW
 * 
 */
public abstract class AbstractTxlSecurer implements TxlSecurer {

    /** 日志 **/
    protected Logger log = Logger.getLogger(getClass());

    /**
     * 控制方式的类别为共享给个人
     */
    public static final String SHARE_TYPE_USER = "1";
    /**
     * 控制方式的类别为共享给部门
     */
    public static final String SHARE_TYPE_ORG = "2";
    /**
     * 控制方式的类别为共享给角色
     */
    public static final String SHARE_TYPE_ROLE = "3";
    /**
     * 控制共享内容为简历
     */
    public static final String SHARE_TYPE_RESUME = "4";
    /**
     * 共享联系人信息中间表
     */
    public static final String SY_COMM_ADDRESS_ASSIST = "SY_COMM_ADDRESS_ASSIST";

    /**
     * 系统配置中控制用户的信息项
     */
    public static final String SY_COMM_ADDRESS_CTR_INFO = "SY_COMM_ADDRESS_CTR_INFO";

    /**
     * 默认开放对象
     */
    public static final String SY_COMM_ADDRESS_OPEN_OBJ = "SY_COMM_ADDRESS_OPEN_OBJ";

    /**
     * 获得通讯录信息重组后的User信息
     * @param userBean 查看用户数据载体
     * @return Bean 重组后的UserInfo
     */
    @Override
    public abstract Bean getSecuredUserInfo(Bean userBean);

}
