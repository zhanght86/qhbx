package com.rh.oa.ldr;

/**
 * 
 * 领导活动模块常量类
 * @author yangjy
 *
 */
public class LeaderActConstant {
    /**领导活动安排服务**/
    public static final  String LDR_ACT = "OA_LDR_ACTION";
    /** 领导活动安排的领导表名称 */
    public static final  String LDR_ACT_USERS = "OA_LDR_ACT_USERS";
    
    /** 审核状态：未审核 **/
    public static final int CHK_STATE_WEISHENHE = 1;
    /** 审核状态：送审核 **/
    public static final int CHK_STATE_SONGSHENHE = 5;
    /** 审核状态：审核通过 **/
    public static final int CHK_STATE_PASS = 10;
    /** 审核状态：审核不通过 **/
    public static final int CHK_STATE_NOTPASS = 15;
    /** 审核状态：不审核 **/
    public static final int CHK_STATE_NOTSHENHE = 20;
    
    /**系统配置：领导活动是否需要审核？false为不需要审核，true未需要审核**/
    public static final String CONF_NEED_CHECK = "OA_LDR_ACTION_NEED_CHECK";
}
